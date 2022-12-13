package fr.cinpiros.inventory;


import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TaskPanel {
    static public Component invName = Component.text("Task");
    private final Player player;

    public TaskPanel(Player player) {
        this.player = player;
    }
    public boolean openMenu() {
        Inventory inv  = Bukkit.createInventory(this.player, 9*6, invName);

        inv.setItem(11, CreateItem.getItem(new ItemStack(Material.PAPER), Component.text("Task").color(NamedTextColor.AQUA), "&a Clique pour récupérer la Task", "&fRécolter du blé : 0/1000"));
        inv.setItem(12, CreateItem.getItem(new ItemStack(Material.PAPER), Component.text("Task").color(NamedTextColor.AQUA), "&a Clique pour récupérer la Task", "&fRécolter des patate : 0/1000"));
        inv.setItem(13, CreateItem.getItem(new ItemStack(Material.PAPER), Component.text("Task").color(NamedTextColor.AQUA), "&a Clique pour récupérer la Task", "&fRécolter des carrote : 0/1000"));

        player.openInventory(inv);

        return true;
    }
}
