package fr.cinpiros.commands;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GiveTask {
    private final Player player;
    private final CommandSender sender;

    public GiveTask(Player player, CommandSender sender) {
        this.player = player;
        this.sender = sender;
    }

    public boolean giveTask(String task_id){
        UtilsDatabase database = new UtilsDatabase();

        Plugin plugin = SmcTask.getInstance();

        Connection conn = database.getConnection(plugin);
        String prefix = database.getPrefix(plugin);


        try {
            Inventory inv = this.player.getInventory();
            ItemStack item;
            ItemMeta meta;
            List<Component> lores = new ArrayList<>();

            int task_money_reward;
            String rarity_name;
            String rarity_color;


            PreparedStatement psSelectTask = conn.prepareStatement("SELECT " +
                    prefix+"task.name, "+prefix+"task.item, "+prefix+"task.color, "+prefix+"task.item_enchant_effect, " +
                    prefix+"task.reward_money, "+prefix+"rarity.name, "+prefix+"rarity.color" +
                    " FROM "+prefix+"task LEFT OUTER JOIN "+prefix+"rarity ON " +
                    prefix+"task.FK_rarity_id = "+prefix+"rarity.id WHERE "+prefix+"task.id = '"+task_id+"' LIMIT 1;");
            ResultSet rsTask = psSelectTask.executeQuery();

            if (rsTask.next()) {
                Material itemMaterial = Material.matchMaterial(rsTask.getString(2));

                if (itemMaterial != null) {
                    item = new ItemStack(itemMaterial);
                    meta = item.getItemMeta();
                } else {
                    this.sender.sendMessage("&4Error: &cMaterial not found");
                    return true;
                }

                if (rsTask.getBoolean(4)) {
                    meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                }
                meta.displayName(Component.text(rsTask.getString(1))
                        .color(TextColor.fromCSSHexString(rsTask.getString(3)))
                        .decoration(TextDecoration.ITALIC, false));

                task_money_reward = rsTask.getInt(5);
                rarity_name = rsTask.getString(6);
                rarity_color = rsTask.getString(7);
            } else {
                this.sender.sendMessage("&4Error: &ctask not found");
                return true;
            }

            int taskInstanceID;

            PreparedStatement statementInsertTaskInstance  = conn.prepareStatement("INSERT INTO "+prefix+"task_instance (FK_task_id) VALUE ('"+task_id+"');", Statement.RETURN_GENERATED_KEYS);

            int affectedRows = statementInsertTaskInstance.executeUpdate();

            if (affectedRows == 0) {
                sender.sendMessage("[smcTask] Error no task instance created on give task");
                return true;
            }
            try (ResultSet generatedKeys = statementInsertTaskInstance.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taskInstanceID = generatedKeys.getInt(1);
                    sender.sendMessage("id: "+taskInstanceID);
                }
                else {
                    sender.sendMessage("[smcTask] Error can't get task instance id on give task");
                    return true;
                }
            }

            NamespacedKey key = new NamespacedKey(plugin, "instance_task_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, taskInstanceID);


            PreparedStatement psSelectConditionDescription = conn.prepareStatement("SELECT " +
                    prefix+"condition.description, "+prefix+"task_condition.FK_condition_condition_id FROM "+prefix+"task_condition INNER JOIN "+prefix+"condition ON " +
                    prefix+"task_condition.FK_condition_condition_id = "+prefix+"condition.condition_id WHERE " +
                    prefix+"task_condition.FK_task_id = '"+task_id+"';");
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            PreparedStatement conditionInstanceInsert = conn.prepareStatement("INSERT INTO "+prefix+"condition_instance (FK_task_instance_id, FK_condition_condition_id, FK_task_id) VALUE (?, ?, ?);");

            while (rsConditionDescription.next()) {
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        rsConditionDescription.getString(1)
                                .replace("%q%", "0")
                                .replace("%t%", "Jrs: 0, H: 0, Min: 0")))
                        .decoration(TextDecoration.ITALIC, false));

                conditionInstanceInsert.setInt(1, taskInstanceID);
                conditionInstanceInsert.setString(2, rsConditionDescription.getString(2));
                conditionInstanceInsert.setString(3, task_id);
                conditionInstanceInsert.addBatch();
            }
            conditionInstanceInsert.executeBatch();

            lores.add(Component.text("    "));

            PreparedStatement psSelectTaskDescription = conn.prepareStatement("SELECT lore FROM " +
                    prefix+"task_description WHERE FK_task_id = '"+task_id+"' ORDER BY id;");
            ResultSet rsTask_description = psSelectTaskDescription.executeQuery();

            boolean haveDescription = false;

            while (rsTask_description.next()) {
                haveDescription = true;
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        rsTask_description.getString(1)))
                        .decoration(TextDecoration.ITALIC, false)
                        .colorIfAbsent(TextColor.color(255, 255, 255)));
            }
            if (haveDescription) {
                lores.add(Component.text("    "));
            }


            int index_reward_line = 0;
            if (task_money_reward != 0) {
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        "&eReward: &6"+task_money_reward+" &eMoney"))
                        .decoration(TextDecoration.ITALIC, false));
            } else {
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&eReward:"))
                        .decoration(TextDecoration.ITALIC, false));
                index_reward_line = lores.size() - 1;
            }

            PreparedStatement psSelectTaskRewardItem = conn.prepareStatement("SELECT item, quantity FROM " +
                    prefix+"task_reward_item WHERE FK_task_id = '"+task_id+"' ORDER BY id;");
            ResultSet rsTaskRewardItem = psSelectTaskRewardItem.executeQuery();

            boolean haveRewardItem = false;

            while (rsTaskRewardItem.next()) {
                haveRewardItem = true;
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        "&f" + rsTaskRewardItem.getString(1).toLowerCase().replace('_', ' ') +
                                "&f: &3"+rsTaskRewardItem.getInt(2)))
                        .decoration(TextDecoration.ITALIC, false));
            }
            if (haveRewardItem) {
                lores.add(Component.text("    "));
            }

            PreparedStatement psSelectTaskRewardCommand = conn.prepareStatement("SELECT description FROM " +
                    prefix+"task_reward_command WHERE FK_task_id = '"+task_id+"' ORDER BY id;");
            ResultSet rsTaskRewardCommand = psSelectTaskRewardCommand.executeQuery();

            boolean haveRewardCommand = false;

            while (rsTaskRewardCommand.next()) {
                haveRewardCommand = true;
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        rsTaskRewardCommand.getString(1))).decoration(TextDecoration.ITALIC, false));
            }
            if (haveRewardCommand) {
                lores.add(Component.text("    "));
            }

            PreparedStatement psSelectTaskRewardJobsExp = conn.prepareStatement("SELECT " +
                    prefix+"jobs.name, "+prefix+"jobs.color, "+prefix+"task_reward_jobs_exp.exp FROM " +
                    prefix+"task_reward_jobs_exp INNER JOIN "+prefix+"jobs ON " +
                    prefix+"task_reward_jobs_exp.FK_jobs_id = "+prefix+"jobs.id WHERE " +
                    prefix+"task_reward_jobs_exp.FK_task_id = '"+task_id+"';");
            ResultSet rsTaskRewardJobsExp = psSelectTaskRewardJobsExp.executeQuery();

            boolean haveRewardJobsExp = false;

            while (rsTaskRewardJobsExp.next()) {
                haveRewardJobsExp = true;
                lores.add(Component.text(rsTaskRewardJobsExp.getString(1))
                        .color(TextColor
                        .fromCSSHexString(rsTaskRewardJobsExp.getString(2)))
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component
                                .text(ChatColor
                                        .translateAlternateColorCodes('&', "&f: " +
                                                rsTaskRewardJobsExp.getInt(3)+" &3fame"))
                                .decoration(TextDecoration.ITALIC, false)));
            }
            if (haveRewardJobsExp) {
                lores.add(Component.text("    "));
            }

            if (index_reward_line != 0 && !haveRewardItem && !haveRewardCommand && !haveRewardJobsExp) {
                lores.remove(index_reward_line);
            }

            lores.add(Component.text(rarity_name)
                    .color(TextColor.fromCSSHexString(rarity_color))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false));

            meta.lore(lores);

            item.setItemMeta(meta);


            inv.addItem(item);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(conn);
        }
        return true;
    }
}
