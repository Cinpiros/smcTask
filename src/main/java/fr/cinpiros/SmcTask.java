package fr.cinpiros;

import fr.cinpiros.commands.GiveItemTest;
import fr.cinpiros.commands.MenuTask;
import fr.cinpiros.commands.Test;
import fr.cinpiros.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SmcTask extends JavaPlugin {
    @Override
    public void onEnable() {

        getCommand("test").setExecutor(new Test());
        getCommand("giveitemtest").setExecutor(new GiveItemTest());
        getCommand("menutask").setExecutor(new MenuTask(this));

        new PlayerHandler(this);

        Bukkit.getLogger().info("smcTask Enable");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }

}