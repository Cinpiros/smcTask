package fr.cinpiros.inventory;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SaveInventory extends UtilsDatabase {

    public void saveTaskInventory(Inventory inv, final Player player) {
        String uuid = player.getUniqueId().toString();
        int size = inv.getSize();
        try (Connection conn = getConnection()) {

            PreparedStatement psSelectPlayerTaskInventory = conn.prepareStatement(selectPlayerTaskInventory(getPrefix(), uuid));
            ResultSet rsPlayerTaskInventory = psSelectPlayerTaskInventory.executeQuery();
            Map<Integer, Integer> playerInventoryRSMap = new HashMap<>();

            while (rsPlayerTaskInventory.next()) {
                playerInventoryRSMap.put(rsPlayerTaskInventory.getInt(2), rsPlayerTaskInventory.getInt(1));
            }

            NamespacedKey key = new NamespacedKey(SmcTask.getInstance(), "instance_task_id");

            for (int ct = 1; ct <= size; ct++) {
                Integer rs_task_instance_id = playerInventoryRSMap.get(ct);

                if (inv.getItem(ct) == null) {
                    if (rs_task_instance_id != null) {
                        //supr item at index ct and uuid
                    }
                    continue;
                }

                if (!(Objects.requireNonNull(inv.getItem(ct)).getItemMeta().getPersistentDataContainer().has(key))) {
                    //give back the itemstack that don't have key
                    continue;
                }

                Integer item_task_instance_id = Objects.requireNonNull(inv.getItem(ct)).getItemMeta()
                        .getPersistentDataContainer().get(key, PersistentDataType.INTEGER);

                if (rs_task_instance_id == null) {
                    //insert task instance uuid slot to db
                    continue;
                }

                if (!(item_task_instance_id == rs_task_instance_id)) {
                    //update task_instance_id where uuid and slot
                }
            }


        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }


    }
}
