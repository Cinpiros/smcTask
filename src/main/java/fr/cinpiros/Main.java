package fr.cinpiros;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        System.out.println("Start smcTask");
    }
    @Override
    public void onDisable() {
        System.out.println("stop smcTask");
    }

}