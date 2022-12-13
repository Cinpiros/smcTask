package fr.cinpiros.commands;

import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiveTask {
    private final Player player;

    public GiveTask(Player player) {
        this.player = player;
    }

    public boolean giveTask(){

        Inventory inv = player.getInventory();

        inv.addItem(CreateItem.getItem(new ItemStack(Material.PAPER), Component.text("Task").color(NamedTextColor.AQUA), "&a Clique pour récupérer la Task", "&fRécolter du blé : 0/1000"));

        return true;
    }
}
