package fr.cinpiros.utils;

import org.bukkit.Bukkit;

import java.util.Random;

public class Test {
    public boolean test() {
        for (int ct = 0; ct< 10; ct++) {
            int random = (new Random().nextInt(10))+1;
            Bukkit.getLogger().info("random: "+random);
        }


        return true;
    }
}
