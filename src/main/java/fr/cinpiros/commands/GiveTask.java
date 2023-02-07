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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GiveTask extends UtilsDatabase {
    private final Player player;
    private final CommandSender sender;

    public GiveTask(Player player, CommandSender sender) {
        this.player = player;
        this.sender = sender;
    }

    public boolean giveTask(final String task_id) {
        final Plugin plugin = SmcTask.getInstance();

        final String prefix = getPrefix();

        Inventory inv = this.player.getInventory();
        ItemStack item;
        ItemMeta meta;
        List<Component> lore = new ArrayList<>();

        int task_money_reward;
        String rarity_name;
        String rarity_color;

        try (
                Connection conn = getConnection()
                ){
            PreparedStatement psSelectTask = conn.prepareStatement(selectTask(prefix, task_id));

            ResultSet rsTask = psSelectTask.executeQuery();

            if (!rsTask.next()) {
                this.sender.sendMessage("&4Error: &ctask not found");
                return true;
            }
            Material itemMaterial = Material.matchMaterial(rsTask.getString(2));

            if (itemMaterial != null) {
                item = new ItemStack(itemMaterial);
                meta = item.getItemMeta();
            } else {
                this.sender.sendMessage("&4Error: &cMaterial not found");
                return false;
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

            int taskInstanceID;



            PreparedStatement statementInsertTaskInstance = conn.prepareStatement(insertTaskInstance(prefix, task_id), Statement.RETURN_GENERATED_KEYS);

            int affectedRows = statementInsertTaskInstance.executeUpdate();

            if (affectedRows == 0) {
                sender.sendMessage("[smcTask] Error no task instance created on give task");
                return true;
            }
            try (ResultSet generatedKeys = statementInsertTaskInstance.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taskInstanceID = generatedKeys.getInt(1);
                } else {
                    sender.sendMessage("[smcTask] Error can't get task instance id on give task");
                    return true;
                }
            }

            NamespacedKey key = new NamespacedKey(plugin, "instance_task_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, taskInstanceID);


            PreparedStatement psSelectConditionDescription = conn.prepareStatement(selectConditionDescription(prefix, task_id));
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            PreparedStatement conditionInstanceInsert = conn.prepareStatement(insertConditionInstance(prefix));

            while (rsConditionDescription.next()) {
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
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

            lore.add(Component.text("    "));

            PreparedStatement psSelectTaskDescription = conn.prepareStatement(selectTaskDescription(prefix, task_id));
            ResultSet rsTask_description = psSelectTaskDescription.executeQuery();

            boolean haveDescription = false;

            while (rsTask_description.next()) {
                haveDescription = true;
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                rsTask_description.getString(1)))
                        .decoration(TextDecoration.ITALIC, false)
                        .colorIfAbsent(TextColor.color(255, 255, 255)));
            }
            if (haveDescription) {
                lore.add(Component.text("    "));
            }


            int index_reward_line = 0;
            if (task_money_reward != 0) {
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                "&eReward: &6" + task_money_reward + " &eMoney"))
                        .decoration(TextDecoration.ITALIC, false));
            } else {
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&eReward:"))
                        .decoration(TextDecoration.ITALIC, false));
                index_reward_line = lore.size() - 1;
            }

            PreparedStatement psSelectTaskRewardItem = conn.prepareStatement(selectTaskRewardItem(prefix, task_id));
            ResultSet rsTaskRewardItem = psSelectTaskRewardItem.executeQuery();

            boolean haveRewardItem = false;

            while (rsTaskRewardItem.next()) {
                haveRewardItem = true;
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                "&f" + rsTaskRewardItem.getString(1).toLowerCase().replace('_', ' ') +
                                        "&f: &3" + rsTaskRewardItem.getInt(2)))
                        .decoration(TextDecoration.ITALIC, false));
            }
            if (haveRewardItem) {
                lore.add(Component.text("    "));
            }

            PreparedStatement psSelectTaskRewardCommand = conn.prepareStatement(selectTaskRewardCommandDescription(prefix, task_id));
            ResultSet rsTaskRewardCommand = psSelectTaskRewardCommand.executeQuery();

            boolean haveRewardCommand = false;

            while (rsTaskRewardCommand.next()) {
                haveRewardCommand = true;
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        rsTaskRewardCommand.getString(1))).decoration(TextDecoration.ITALIC, false));
            }
            if (haveRewardCommand) {
                lore.add(Component.text("    "));
            }

            PreparedStatement psSelectTaskRewardJobsExp = conn.prepareStatement(selectTaskRewardJobsExp(prefix, task_id));
            ResultSet rsTaskRewardJobsExp = psSelectTaskRewardJobsExp.executeQuery();

            boolean haveRewardJobsExp = false;

            while (rsTaskRewardJobsExp.next()) {
                haveRewardJobsExp = true;
                lore.add(Component.text(rsTaskRewardJobsExp.getString(1))
                        .color(TextColor
                                .fromCSSHexString(rsTaskRewardJobsExp.getString(2)))
                        .decoration(TextDecoration.ITALIC, false)
                        .append(Component
                                .text(ChatColor
                                        .translateAlternateColorCodes('&', "&f: " +
                                                rsTaskRewardJobsExp.getInt(3) + " &3fame"))
                                .decoration(TextDecoration.ITALIC, false)));
            }
            if (haveRewardJobsExp) {
                lore.add(Component.text("    "));
            }

            if (index_reward_line != 0 && !haveRewardItem && !haveRewardCommand && !haveRewardJobsExp) {
                lore.remove(index_reward_line);
            }

            lore.add(Component.text(rarity_name)
                    .color(TextColor.fromCSSHexString(rarity_color))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false));

            meta.lore(lore);

            item.setItemMeta(meta);


            inv.addItem(item);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
