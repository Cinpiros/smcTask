package fr.cinpiros.database;

import fr.cinpiros.SmcTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class ConnectionPoolRedis {
    private JedisPool pool;

    /**
     * Crée une Connection pool de Jedis et retourne une instance de ConnectionPoolRedis
     * @return ConnectionPoolRedis instance
     */
    public static ConnectionPoolRedis create() {
        Plugin plugin = SmcTask.getSmcTaskInstance();
        FileConfiguration conf = plugin.getConfig();

        ConnectionPoolRedis poolRedis = new ConnectionPoolRedis();
        poolRedis.pool = new JedisPool(configPoolRedis(), conf.getString("redis.host"),
                conf.getInt("redis.port"), conf.getInt("redis.timeout"),
                conf.getString("redis.password"), conf.getInt("redis.numDatabase"));
        return poolRedis;
    }

    private static JedisPoolConfig configPoolRedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setBlockWhenExhausted(true);
        return jedisPoolConfig;
    }

    /**
     * La méthode getResource() retourne une instance de Jedis qui peut être utilisée pour interagir avec Redis.
     * @return Jedis connections instance
     */
    public Jedis getConnection() {
        return this.pool.getResource();
    }
    // exemple usage : try (Jedis jedis = pool.getResource()) {

    /**
     * destroy the connection pool to redis
     */
    public void shutdown() {
        this.pool.destroy();
    }
}
