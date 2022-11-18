package fr.cinpiros;

import fr.cinpiros.commands.giveitemtest;
import fr.cinpiros.commands.test;
import fr.cinpiros.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class smcTask extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getLogger().info("smcTask Enable");

        getCommand("test").setExecutor(new test());
        getCommand("giveitemtest").setExecutor(new giveitemtest());

        new PlayerHandler(this);
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }

}