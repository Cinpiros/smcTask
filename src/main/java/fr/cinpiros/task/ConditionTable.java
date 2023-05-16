package fr.cinpiros.task;

import java.util.HashMap;
import java.util.UUID;

public class ConditionTable {
    private HashMap<UUID, HashMap<String, Integer>> mapPlayerCondition = new HashMap<>();

    public HashMap<String, Integer> selectElementByUUID(UUID uuid) {
        return this.mapPlayerCondition.get(uuid);
    }

    public void addElement(UUID uuid, HashMap<String, Integer> data) {
        this.mapPlayerCondition.put(uuid, data);
    }

    public void deleteElementByUUID(UUID uuid) {
        this.mapPlayerCondition.remove(uuid);
    }

    public void updateElement(UUID uuid, HashMap<String, Integer> data) {
        this.mapPlayerCondition.replace(uuid, data);
    }

    public boolean updateDataElement(UUID uuid, String typeId, Integer number) {
        HashMap<String, Integer> userConditionData = this.mapPlayerCondition.get(uuid);
        if (userConditionData == null) {
            return false;
        }
        if (!userConditionData.containsKey(typeId)) {
            return false;
        }

        userConditionData.replace(typeId, number);

        this.mapPlayerCondition.replace(uuid, userConditionData);

        return true;
    }
}
