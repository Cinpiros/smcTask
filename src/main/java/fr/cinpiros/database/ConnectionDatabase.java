package fr.cinpiros.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDatabase {
    private final String host = "localhost:3306";
    private final String database = "smcTask";
    private final String user = "root";
    private final String password = "root";
    private Connection conn;

    public ConnectionDatabase() {}

    public Connection connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + "/" + this.database, this.user, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.conn;
    }
}
