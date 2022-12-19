package fr.cinpiros.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDatabase {
    private Connection conn;

    public ConnectionDatabase(Plugin plugin) {
        FileConfiguration conf = plugin.getConfig();
        String host = conf.getString("database.host");
        String user = conf.getString("database.user");
        String password = conf.getString("database.password");
        String database = conf.getString("database.database");

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Connection getConn() {
        return this.conn;
    }
}
