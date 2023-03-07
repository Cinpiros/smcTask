package fr.cinpiros.handlers;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.inventory.OpenInventory;
import fr.cinpiros.inventory.SaveInventory;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.sql.*;


public class InventoryHandler implements Listener {

    public InventoryHandler(SmcTask plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


    /*@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onTaskInventoryClick(InventoryClickEvent event) {
        //Player player = (Player) event.getWhoClicked();

        if (!event.getView().title().equals(OpenInventory.invTaskName)) {
            return;
        }
        Inventory inv  = event.getClickedInventory();

        if (inv == null) {
            return;
        }
        ItemStack item = inv.getItem(event.getSlot());
        Component sqlrequesttest = Component.text("Task").color(NamedTextColor.AQUA);

        if (item == null || Objects.equals(item.getItemMeta().displayName(), sqlrequesttest)) {
            return;
        }

        event.setCancelled(true);
    }*/

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onTaskPanelClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(OpenInventory.invPanelName)) {
            return;
        }

        Inventory inv = event.getClickedInventory();


        if (inv == null) {
            return;

        }
        if (inv.getType() == InventoryType.PLAYER) {
            return;
        }
        /*if (event.getRawSlot() != event.getSlot()) {
            return;
        }*/



        ItemStack item = event.getCurrentItem();



        if (item == null) {
            event.setCancelled(true);
            return;
        }
        ItemMeta meta = item.getItemMeta();
        NamespacedKey instance_key = new NamespacedKey(SmcTask.getSmcTaskInstance(), "instance_task_id");


        if (inv.getType() == InventoryType.CHEST) {
            if (meta.getPersistentDataContainer().has(instance_key)) {
                event.setCancelled(true);
                return;
            }
        }


        NamespacedKey key = new NamespacedKey(SmcTask.getSmcTaskInstance(), "task_id");

        if (!meta.getPersistentDataContainer().has(key)) {
            event.setCancelled(true);
            return;
        }



        Player player = (Player) event.getWhoClicked();
        String uuid = player.getUniqueId().toString();
        UtilsDatabase database = new UtilsDatabase();

        int player_daily_task;

        try (Connection conn = database.getConnection()){
            PreparedStatement psSelectDailyTask = conn.prepareStatement("SELECT daily_pick_up_task FROM "+database.prefix +
                    "player_daily_task WHERE uuid = '"+uuid+"';");
            ResultSet rsSelectDailyTask = psSelectDailyTask.executeQuery();

            if (rsSelectDailyTask.next()) {
                player_daily_task = rsSelectDailyTask.getInt(1);
            } else {
                event.setCancelled(true);
                return;
            }
        } catch (SQLException e) {
            event.setCancelled(true);
            e.printStackTrace();
            return;
        }

        if (player_daily_task <= 0) {
            event.setCancelled(true);
            return;
        }


        String task_id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        int taskInstanceID;

        try (Connection conn = database.getConnection()){

            PreparedStatement statementInsertTaskInstance = conn.prepareStatement("INSERT INTO " +
                    database.prefix + "task_instance (FK_task_id) VALUE ('" + task_id + "');", Statement.RETURN_GENERATED_KEYS);

            int affectedRows = statementInsertTaskInstance.executeUpdate();

            if (affectedRows == 0) {
                Bukkit.getLogger().warning("[SmcTask] Error task instance not created when pick up task on panel for user: "+uuid+" and task_id: "+task_id);
                event.setCancelled(true);
                return;
            }
            try (ResultSet generatedKeys = statementInsertTaskInstance.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    taskInstanceID = generatedKeys.getInt(1);
                } else {
                    Bukkit.getLogger().warning("[SmcTask] Error can't get generated task_instance when pick up task on panel for user: "+uuid+" and task_id: "+task_id);
                    event.setCancelled(true);
                    return;
                }
            }


            meta.getPersistentDataContainer().set(instance_key, PersistentDataType.INTEGER, taskInstanceID);

            meta.getPersistentDataContainer().remove(key);
            item.setItemMeta(meta);


            PreparedStatement psSelectConditionDescription = conn.prepareStatement("SELECT "+
                    database.prefix + "task_condition.FK_condition_condition_id FROM " +
                    database.prefix + "task_condition INNER JOIN " + database.prefix + "condition ON " +
                    database.prefix + "task_condition.FK_condition_condition_id = " + database.prefix + "condition.condition_id WHERE " +
                    database.prefix + "task_condition.FK_task_id = '" + task_id + "';");
            ResultSet rsConditionDescription = psSelectConditionDescription.executeQuery();

            PreparedStatement conditionInstanceInsert = conn.prepareStatement("INSERT INTO " +
                    database.prefix + "condition_instance (FK_task_instance_id, FK_condition_condition_id, FK_task_id) VALUE ("+taskInstanceID+", ?, '"+task_id+"');");

            while (rsConditionDescription.next()) {
                conditionInstanceInsert.setString(1, rsConditionDescription.getString(1));
                conditionInstanceInsert.addBatch();
            }
            conditionInstanceInsert.executeBatch();


            player_daily_task = player_daily_task - 1;

            PreparedStatement psUpdatePlayerDailyTask = conn.prepareStatement("UPDATE "+database.prefix+
                    "player_daily_task SET daily_pick_up_task = "+player_daily_task+" WHERE uuid = '"+uuid+"';");
            int affectedRow = psUpdatePlayerDailyTask.executeUpdate();

            if (affectedRow == 0) {
                event.setCancelled(true);
                Bukkit.getLogger().warning("[SmcTask] Error daily task not updated when pick up task on panel for user: "+uuid);
                return;
            }

        }catch (SQLException e) {
            event.setCancelled(true);
            e.printStackTrace();
            return;
        }

        //change info of number of task that player can get
        //delete task for panel task inventory
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCloseTaskInventory(InventoryCloseEvent event) {
        if (!event.getView().title().equals(OpenInventory.invTaskName)) {
            return;
        }
        Player player = (Player) event.getPlayer();
        Inventory inv = event.getInventory();

        new SaveInventory().saveTaskInventory(inv, player);

        /*try {
            new SaveInventory().saveTaskInventory(inv, player);
        } catch (SaveInventoryException e){
            Bukkit.getLogger().warning(e.getMessage());
            player.sendMessage("Erreur lors de la syncronisation de l'inventaire de task merci de contacter un administrateur");
        }*/



    }

}
