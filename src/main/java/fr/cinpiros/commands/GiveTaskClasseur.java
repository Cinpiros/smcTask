package fr.cinpiros.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GiveTaskClasseur {

    private final Player player;
    public static final Component itemName = Component.text("Workbook").color(NamedTextColor.YELLOW)
            .decoration(TextDecoration.ITALIC, false);

    public GiveTaskClasseur(Player player) {
        this.player = player;
    }

    public boolean giveClasseur(){

        Inventory inv = player.getInventory();
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        List<Component> lores = new ArrayList<>();

        meta.displayName(itemName);

        lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                "&7Clique droit ouvre l'inventaire de tâche")));
        lores.add(Component.text(ChatColor.translateAlternateColorCodes('&',
                "&7Clique gauche ouvre le menu de quête")));
        meta.lore(lores);
        item.setItemMeta(meta);

        inv.addItem( item);
        return true;
    }
}
