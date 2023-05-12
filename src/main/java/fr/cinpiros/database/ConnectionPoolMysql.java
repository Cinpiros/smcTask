package fr.cinpiros.database;

import com.mysql.cj.jdbc.MysqlDataSource;
import fr.cinpiros.SmcTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPoolMysql {
    public final String prefix;
    private final int MAX_CONNECTIONS;
    private final DataSource dataSource;
    private final List<Connection> availableConnections = new ArrayList<>();
    private final List<Connection> usedConnections = new ArrayList<>();

    private ConnectionPoolMysql() {
        Plugin plugin = SmcTask.getSmcTaskInstance();
        FileConfiguration conf = plugin.getConfig();

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL("jdbc:mysql://"+conf.getString("database.host")+"/"+conf.getString("database.database"));
        mysqlDataSource.setUser(conf.getString("database.user"));
        mysqlDataSource.setPassword(conf.getString("database.password"));

        this.MAX_CONNECTIONS = conf.getInt("database.max_pool_connection");
        this.prefix = conf.getString("database.prefix");

        this.dataSource = mysqlDataSource;
    }

    /**
     *  La méthode create() est une méthode statique qui crée une instance de la classe ConnectionPool et initialise le pool de connexions avec MAX_CONNECTIONS
     *  @return ConnectionPoolMysql object
     *  */
    public static ConnectionPoolMysql create() {
        ConnectionPoolMysql pool = new ConnectionPoolMysql();
        for (int i = 0; i < pool.MAX_CONNECTIONS; i++) {
            try {
                pool.availableConnections.add(pool.dataSource.getConnection());
            } catch (SQLException e) {
                SmcTask.disablePlugin(e.getMessage());
            }

        }
        return pool;
    }

    /**
     * La méthode getConnection() retourne une connexion disponible dans le pool, en la retirant de la liste des connexions disponibles et en l'ajoutant à la liste des connexions utilisées.
     * @return Connection une des connection disponible
     * @throws SQLException when availableConnections is empty
     */
    public synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            throw new SQLException("All connections are in use");
        }
        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    /**
     * La méthode releaseConnection() libère une connexion utilisée en la retirant de la liste des connexions utilisées et en la remettant dans la liste des connexions disponibles.
     * @param connection close connection
     * @throws SQLException when connection not in use
     */
    public synchronized void releaseConnection(Connection connection) throws SQLException {
        if (!usedConnections.remove(connection)) {
            throw new SQLException("Connection not in use");
        }
        availableConnections.add(connection);
    }

    /**
     * La méthode getSize() retourne le nombre total de connexions dans le pool, c'est-à-dire la somme des connexions disponibles et utilisées.
     * @return number of aivable connections + used connections
     */
    public int getSize() {
        return availableConnections.size() + usedConnections.size();
    }

    /**
     * La méthode shutdown() ferme toutes les connexions du pool, qu'elles soient disponibles ou utilisées.
     * @throws SQLException on error closing connection
     */
    public void shutdown() throws SQLException {
        usedConnections.forEach(c -> {
            try {
                c.close();
            } catch (SQLException e) {
                SmcTask.logWarn("Error closing connection " + c);
                e.printStackTrace();
            }
        });
        availableConnections.forEach(c -> {
            try {
                c.close();
            } catch (SQLException e) {
                SmcTask.logWarn("Error closing connection " + c);
                e.printStackTrace();
            }
        });
        availableConnections.clear();
        usedConnections.clear();
    }
}
