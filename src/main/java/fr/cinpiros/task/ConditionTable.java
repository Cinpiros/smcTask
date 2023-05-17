package fr.cinpiros.task;

import fr.cinpiros.database.RedisSync;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Class used to save the list of condition of all player and push the data (number) of condition in redis every 10 seconds
 */
public class ConditionTable {

    /**
     * Table to save all action during 10 sec then push in redis
     * - fist hashmap = player -> hashmap element
     * - second hashmap = element -> entry contition
     * - Map entry = conditionId -> number
     */
    private final HashMap<UUID, HashMap<String, Map.Entry<String, Integer>>> mapPlayerCondition = new HashMap<>();
    private final Timer timer = new Timer();

    /**
     * Get the element hashmap of a player by uuid
     * @param uuid get element hashmap from player uuid
     * @return HashMap<String, Map.Entry<String, Integer> the element hashmap of a player
     */
    public HashMap<String, Map.Entry<String, Integer>> getMapElement(UUID uuid) {
        return this.mapPlayerCondition.get(uuid);
    }

    /**
     * get the entry in a element of a player hashmap
     * @param uuid of the player
     * @param element key
     * @return Map.Entry<String, Integer> or null
     */
    public Map.Entry<String, Integer> getElement(UUID uuid, String element) {
        HashMap<String, Map.Entry<String, Integer>> userConditionData = this.mapPlayerCondition.get(uuid);
        if (userConditionData == null) {
            return null;
        }
        return userConditionData.get(element);
    }

    /**
     * Add a new player and list of element in list condition if player already exist replace the old data
     * @param uuid of the player
     * @param data element hashmap of the player
     */
    public void addOrRemplacePlayer(UUID uuid, HashMap<String, Map.Entry<String, Integer>> data) {
        this.mapPlayerCondition.put(uuid, data);
    }

    /**
     * Add or remplace element to the hashmap of a player
     * @param uuid of the player
     * @param element key to add or use
     * @param data value to add or remplace
     * @return true if user exist in condition list hashmap et value is added or remplaced or false if user do not exist and data not added
     */
    public boolean addOrRemplaceElementToPLayer(UUID uuid, String element, Map.Entry<String, Integer> data) {
        HashMap<String, Map.Entry<String, Integer>> userConditionData = this.mapPlayerCondition.get(uuid);
        if (userConditionData == null) {
            return false;
        }
        userConditionData.put(element, data);
        this.mapPlayerCondition.put(uuid, userConditionData);
        return true;
    }

    /**
     * reset to 0 the number of a condition (after push to redis)
     * @param uuid of the player
     * @param element key
     * @param conditionId key
     * @return true if reset false if not
     */
    public boolean resetConditionData(UUID uuid, String element, String conditionId) {
        HashMap<String, Map.Entry<String, Integer>> userConditionData = this.mapPlayerCondition.get(uuid);
        if (userConditionData == null) {
            return false;
        }
        Map.Entry<String, Integer> entry = userConditionData.get(element);
        if (entry == null) {
            return false;
        }
        entry.setValue(0);
        userConditionData.put(element, entry);
        this.mapPlayerCondition.put(uuid, userConditionData);
        return true;
    }

    /**
     * remove player from list player condition
     * @param uuid of the player to remove
     */
    public void deletePlayerByUUID(UUID uuid) {
        this.mapPlayerCondition.remove(uuid);
    }

    public void DelayedTaskSystem() {
        timer.schedule(new PushData(), 10000, 10000);
    }

    /**
     * intern class used to push data to redis every 10 seconds
     */
    private class PushData extends TimerTask {
        public void run() {
            RedisSync.syncLocalCondition();
        }
    }
}
