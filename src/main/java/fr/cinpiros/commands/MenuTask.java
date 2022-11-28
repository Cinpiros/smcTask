package fr.cinpiros.commands;


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
    private Component invName = Component.text("Task");
    private Player player;

    public MenuTask(Player player) {
        this.player = player;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().title().equals(invName)) {
            return;
        }

        event.setCancelled(true);
    }


    public void openMenu() {
        Inventory inv  = Bukkit.createInventory(this.player, 9*6,this.invName);

        inv.setItem(11, getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter du blé : 0/1000"));
        inv.setItem(12, getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter des patate : 0/1000"));
        inv.setItem(13, getItem(new ItemStack(Material.BOOK), "&9Task", "&a Clique pour récupérer la Task", "&fRécolter des carrote : 0/1000"));

        player.openInventory(inv);
    }

    private ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));

        List<Component> lores = new ArrayList<>();
        for (Component s : lores) {
            lores.add(Component.text(ChatColor.translateAlternateColorCodes('&', name)));
        }
        meta.lore(lores);

        item.setItemMeta(meta);
        return item;
    }
}
