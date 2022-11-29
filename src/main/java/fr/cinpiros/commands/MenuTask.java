package fr.cinpiros.commands;


import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MenuTask implements Listener {
    static public Component invName = Component.text("Task");
    private final Player player;

    public MenuTask(Player player) {
        this.player = player;
    }
    public void openMenu() {
        Inventory inv  = Bukkit.createInventory(this.player, 9*6, invName);

        inv.setItem(11, CreateItem.getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter du blé : 0/1000"));
        inv.setItem(12, CreateItem.getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter des patate : 0/1000"));
        inv.setItem(13, CreateItem.getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter des carrote : 0/1000"));

        player.openInventory(inv);
    }
}
