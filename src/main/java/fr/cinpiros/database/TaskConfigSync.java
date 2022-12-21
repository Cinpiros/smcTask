package fr.cinpiros.database;

import org.bukkit.plugin.Plugin;

import java.sql.Connection;

public class TaskConfigSync {
    private void pushTask(Plugin plugin, UtilsDatabase database) {
        Connection conn = database.getConnection(plugin);

        database.closeConnection(conn);
    }
}
