package fr.cinpiros.task;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.exeption.TaskCreateException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskCreator extends UtilsDatabase{
    public ItemStack createTask(final String task_id) throws SQLException, TaskCreateException {
        final String prefix = super.prefix;

        ItemStack item;
        ItemMeta meta;
        List<Component> lore = new ArrayList<>();

        int task_money_reward;
        String rarity_name;
        String rarity_color;

        try (Connection conn = getConnection()){
            PreparedStatement psSelectTask = conn.prepareStatement(selectTask(prefix, task_id));

            ResultSet rsTask = psSelectTask.executeQuery();

            if (!rsTask.next()) {
                throw new TaskCreateException("task not found: "+task_id);
            }
            Material itemMaterial = Material.matchMaterial(rsTask.getString(2));

            if (itemMaterial != null) {
                item = new ItemStack(itemMaterial);
                meta = item.getItemMeta();
            } else {
                throw new TaskCreateException("material not found: "+task_id);
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
                throw new TaskCreateException("task instance not created: "+task_id);
            }
            try (ResultSet generatedKeys = statementInsertTaskInstance.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taskInstanceID = generatedKeys.getInt(1);
                } else {
                    throw new TaskCreateException("task generated key missing: "+task_id);
                }
            }

            NamespacedKey key = new NamespacedKey(SmcTask.getSmcTaskInstance(), "instance_task_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, taskInstanceID);


            PreparedStatement psSelectConditionDescription = conn.prepareStatement(selectConditionDescription(prefix, task_id));
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            PreparedStatement conditionInstanceInsert = conn.prepareStatement(insertConditionInstance(prefix));

            while (rsConditionDescription.next()) {
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                rsConditionDescription.getString(1)
                                        .replace("%q%", "0")
                                        .replace("%t%", "0J 00:00")))
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

            return item;
        }
    }

    public ItemStack getTaskForPanel(final String task_id) throws SQLException, TaskCreateException {
        final String prefix = super.prefix;

        ItemStack item;
        ItemMeta meta;
        List<Component> lore = new ArrayList<>();

        int task_money_reward;
        String rarity_name;
        String rarity_color;

        try (Connection conn = getConnection()){
            PreparedStatement psSelectTask = conn.prepareStatement(selectTask(prefix, task_id));

            ResultSet rsTask = psSelectTask.executeQuery();

            if (!rsTask.next()) {
                throw new TaskCreateException("task not found: "+task_id);
            }
            Material itemMaterial = Material.matchMaterial(rsTask.getString(2));

            if (itemMaterial != null) {
                item = new ItemStack(itemMaterial);
                meta = item.getItemMeta();
            } else {
                throw new TaskCreateException("material not found: "+task_id);
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
                throw new TaskCreateException("task instance not created: "+task_id);
            }
            try (ResultSet generatedKeys = statementInsertTaskInstance.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taskInstanceID = generatedKeys.getInt(1);
                } else {
                    throw new TaskCreateException("task generated key missing: "+task_id);
                }
            }

            NamespacedKey key = new NamespacedKey(SmcTask.getSmcTaskInstance(), "instance_task_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, taskInstanceID);


            PreparedStatement psSelectConditionDescription = conn.prepareStatement(selectConditionDescription(prefix, task_id));
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            PreparedStatement conditionInstanceInsert = conn.prepareStatement(insertConditionInstance(prefix));

            while (rsConditionDescription.next()) {
                lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                rsConditionDescription.getString(1)
                                        .replace("%q%", "0")
                                        .replace("%t%", "0J 00:00")))
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

            return item;
        }
    }

    public ItemStack getTaskForInventory(final Integer task_instance_id) throws SQLException, TaskCreateException {
        final String prefix = super.prefix;

        ItemStack item;
        ItemMeta meta;
        List<Component> lore = new ArrayList<>();

        final int task_money_reward;
        final String rarity_name;
        final String rarity_color;
        final String task_id;

        try (Connection conn = getConnection()) {
            PreparedStatement psSelectTaskId = conn.prepareStatement(selectTaskId(prefix, task_instance_id));
            ResultSet rsTaskId = psSelectTaskId.executeQuery();

            if (rsTaskId.next()) {
                task_id = rsTaskId.getString(1);
            } else {
                throw new TaskCreateException("task id not found with this task instance");
            }



            PreparedStatement psSelectTask = conn.prepareStatement(selectTask(prefix, task_id));

            ResultSet rsTask = psSelectTask.executeQuery();

            if (!rsTask.next()) {
                throw new TaskCreateException("task not found: " + task_id);
            }
            Material itemMaterial = Material.matchMaterial(rsTask.getString(2));

            if (itemMaterial != null) {
                item = new ItemStack(itemMaterial);
                meta = item.getItemMeta();
            } else {
                throw new TaskCreateException("material not found: " + task_id);
            }

            NamespacedKey key = new NamespacedKey(SmcTask.getSmcTaskInstance(), "instance_task_id");
            meta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, task_instance_id);

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

            PreparedStatement psSelectConditionDescription = conn.prepareStatement(selectConditionDescription(prefix, task_id));
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            PreparedStatement conditionInstanceSelect = conn.prepareStatement(selectConditionInstance(prefix, task_instance_id));

            while (rsConditionDescription.next()) {
                conditionInstanceSelect.setString(1, rsConditionDescription.getString(2));
                ResultSet rsConditionQuantity = conditionInstanceSelect.executeQuery();
                if (rsConditionQuantity.next()) {
                    if (!rsConditionQuantity.getBoolean(2)) {
                        int condition_instance_quantity = rsConditionQuantity.getInt(1);
                        lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                        rsConditionDescription.getString(1)
                                                .replace("%q%", Integer.toString(condition_instance_quantity))
                                                .replace("%t%", timeIntToString(condition_instance_quantity))))
                                .decoration(TextDecoration.ITALIC, false));
                    } else {
                        lore.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                                        rsConditionDescription.getString(3)))
                                .decoration(TextDecoration.ITALIC, false));
                    }

                } else {
                    throw new TaskCreateException("condition instance not found for task: "+
                            task_id+" condition: "+rsConditionDescription.getString(2)+
                            " task instance: "+task_instance_id);
                }
            }

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

            return item;
        }
    }


    private String timeIntToString(int quantity) {
        if (quantity == 0) {
            return "0J 00:00";
        }

        Integer jour = (int)Math.floor(quantity % 1440);
        quantity = quantity - jour * 1440;

        if (quantity == 0) {
            return jour+"J 00:00";
        }

        int heure = (int)Math.floor(quantity % 60);
        int minute = quantity - heure *60;

        String heureString;
        String minuteString;

        if (heure < 10) {
            heureString = "0"+heure;
        } else {
            heureString = Integer.toString(heure);
        }
        if (minute < 10) {
            minuteString = "0"+minute;
        } else {
            minuteString = Integer.toString(minute);
        }

        return jour+"J "+heureString+":"+minuteString;
    }
}
