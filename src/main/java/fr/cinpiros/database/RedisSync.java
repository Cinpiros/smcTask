package fr.cinpiros.database;

import fr.cinpiros.SmcTask;
import fr.cinpiros.task.ConditionTable;

public class RedisSync {
    public static void syncLocalCondition() {
        ConditionTable conditionTable = SmcTask.getConditionTable();

        conditionTable.mapPlayerCondition.forEach((uuid, elements) -> {
            elements.forEach((element, condition) -> {
                if (condition.getValue() != 0) {
                    //need to add push to redis new condition -> number
                    conditionTable.mapPlayerCondition.get(uuid).get(element).setValue(0);
                }
            });
        });
    }
}
