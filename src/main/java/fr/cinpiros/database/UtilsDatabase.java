package fr.cinpiros.database;

import fr.cinpiros.SmcTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;

public class UtilsDatabase {
    public final String prefix;
    public final Plugin plugin;


    public UtilsDatabase() {
        plugin = SmcTask.getSmcTaskInstance();
        prefix = plugin.getConfig().getString("database.prefix");
    }
    public Connection getConnection() {
        FileConfiguration conf = this.plugin.getConfig();
        String host = conf.getString("database.host");
        String user = conf.getString("database.user");
        String password = conf.getString("database.password");
        String database = conf.getString("database.database");

        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    @Deprecated
    public void closeConnection(Connection conn) {
        try {
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
