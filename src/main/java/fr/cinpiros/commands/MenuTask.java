package fr.cinpiros.commands;


import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuTask {
    static public Component invName = Component.text("Task");
    private final Player player;

    public MenuTask(Player player) {
        this.player = player;
    }
    public boolean openMenu() {
        Inventory inv  = Bukkit.createInventory(this.player, 9*6, invName);

        inv.setItem(11, CreateItem.getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter du blé : 0/1000"));
        inv.setItem(12, CreateItem.getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter des patate : 0/1000"));
        inv.setItem(13, CreateItem.getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter des carrote : 0/1000"));

        player.openInventory(inv);

        return true;
    }
}
