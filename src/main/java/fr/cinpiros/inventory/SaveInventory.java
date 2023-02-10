package fr.cinpiros.inventory;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SaveInventory extends UtilsDatabase {

    public void saveTaskInventory(Inventory inv, final Player player) {

        try (Connection conn = getConnection()) {
            String uuid = player.getUniqueId().toString();

            PreparedStatement psDeletePlayerTaskInventory = conn.prepareStatement(deletePlayerTaskInventory(getPrefix(), uuid));
            psDeletePlayerTaskInventory.executeUpdate();

            Map<Integer, Integer> saveInv = new HashMap<>();
            NamespacedKey key = new NamespacedKey(SmcTask.getInstance(), "instance_task_id");
            Inventory player_inv = player.getInventory();


            for (int slot = 0; slot < inv.getSize(); slot++) {
                ItemStack curent_item = inv.getItem(slot);
                if (curent_item == null) {
                    continue;
                }
                if (!curent_item.getItemMeta().getPersistentDataContainer().has(key)) {
                    player_inv.addItem(curent_item);
                    continue;
                }
                saveInv.put(slot, curent_item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.INTEGER));
            }


            PreparedStatement psInsertPlayerTaskInventory = conn.prepareStatement(insertPlayerTaskInventory(getPrefix()));

            for (Integer slot : saveInv.keySet()) {
                psInsertPlayerTaskInventory.setInt(1, saveInv.get(slot));
                psInsertPlayerTaskInventory.setString(2, uuid);
                psInsertPlayerTaskInventory.setInt(3, slot);
                psInsertPlayerTaskInventory.addBatch();
            }
            psInsertPlayerTaskInventory.executeBatch();



            /*PreparedStatement psSelectPlayerTaskInventory = conn.prepareStatement(selectPlayerTaskInventory(getPrefix(), uuid));
            ResultSet rsPlayerTaskInventory = psSelectPlayerTaskInventory.executeQuery();
            Map<Integer, Integer> playerInventoryRSMap = new HashMap<>();

            while (rsPlayerTaskInventory.next()) {
                playerInventoryRSMap.put(rsPlayerTaskInventory.getInt(2), rsPlayerTaskInventory.getInt(1));
            }

            NamespacedKey key = new NamespacedKey(SmcTask.getInstance(), "instance_task_id");
            Inventory player_inv = player.getInventory();

            for (int slot = 0; slot < inv.getSize(); slot++) {
                Integer rs_task_instance_id = playerInventoryRSMap.get(slot);

                ItemStack curent_item = inv.getItem(slot);

                if (curent_item == null) {
                    if (rs_task_instance_id != null) {
                        PreparedStatement psDeleteTaskInstancePlayerTaskInventory
                                = conn.prepareStatement(deletePlayerTaskInventoryTaskInstance(getPrefix(), uuid, slot));
                        int countDelete = psDeleteTaskInstancePlayerTaskInventory.executeUpdate();
                        if (countDelete == 0) {
                            throw new SaveInventoryException("task not remove on local inventory slot = null and"+
                                    " database inventory have task for uuid: "+uuid+" and slot: "+slot);
                        }
                    }
                    continue;
                }

                if (!curent_item.getItemMeta().getPersistentDataContainer().has(key)) {
                    player_inv.addItem(curent_item);
                    continue;
                }

                Integer item_task_instance_id = curent_item.getItemMeta()
                        .getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

                if (rs_task_instance_id == null) {
                    PreparedStatement psInsertPlayerTaskInventoryTaskInstance = conn.
                            prepareStatement(insertPlayerTaskInventoryTaskInstance(getPrefix(), item_task_instance_id, uuid, slot));
                    int countInsert = psInsertPlayerTaskInventoryTaskInstance.executeUpdate();
                    if (countInsert == 0) {
                        throw new SaveInventoryException("task not Insert in db with id: "+
                                item_task_instance_id+" uuid: "+uuid+" slot: "+slot);
                    }
                    continue;
                }

                if (!(rs_task_instance_id.equals(item_task_instance_id))) {
                    PreparedStatement psUpdatePlayerTaskInventoryTaskInstance = conn.
                            prepareStatement(updatePlayerTaskInventoryTaskInstance(getPrefix(), item_task_instance_id, uuid, slot));
                    int updateCount = psUpdatePlayerTaskInventoryTaskInstance.executeUpdate();
                    if (updateCount == 0) {
                        throw new SaveInventoryException("task not Update in db with id: "+
                                item_task_instance_id+" uuid: "+uuid+" slot: "+slot);
                    }
                }
            }*/


        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
