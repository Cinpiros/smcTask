package fr.cinpiros.commands;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

            PreparedStatement psSelectConditionDescription = conn.prepareStatement("SELECT " +
                    prefix+"condition.description FROM "+prefix+"task_condition INNER JOIN "+prefix+"condition ON " +
                    prefix+"task_condition.FK_condition_condition_id = "+prefix+"condition.condition_id WHERE " +
                    prefix+"task_condition.FK_task_id = '"+task_id+"';");
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            while (rsConditionDescription.next()) {
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        rsConditionDescription.getString(1)))
                        .decoration(TextDecoration.ITALIC, false));
            }

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

            if (task_money_reward != 0) {
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                        "&eReward: &6"+task_money_reward+" &eMoney"))
                        .decoration(TextDecoration.ITALIC, false));
            } else {
                lores.add(Component.text(ChatColor.translateAlternateColorCodes('&', "&eReward:"))
                        .decoration(TextDecoration.ITALIC, false));
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
