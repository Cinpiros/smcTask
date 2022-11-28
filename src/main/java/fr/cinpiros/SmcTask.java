package fr.cinpiros;

import fr.cinpiros.commands.GiveItemTest;
import fr.cinpiros.commands.MenuTask;
import fr.cinpiros.handlers.PlayerHandler;
import fr.cinpiros.utils.CommandHandler;
import fr.cinpiros.utils.DelayedTask;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SmcTask extends JavaPlugin {
    @Override
    public void onEnable() {

        //ConfigUtil config = new ConfigUtil(this, "test.yml");
        //config.getConfig().set("hello", "world");


        getCommand("giveitemtest").setExecutor(new GiveItemTest());
        getCommand("task").setExecutor(new CommandHandler());

        new PlayerHandler(this);

        new DelayedTask(this);

        Bukkit.getLogger().info("smcTask Enable");
    }
    @Override
    public void onDisable() {
        Bukkit.getLogger().info("smcTask Disable");
    }

}