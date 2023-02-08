package fr.cinpiros.commands;

import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiveTaskClasseur {

    private final Player player;
    public static final Component itemName = Component.text("Classeur").color(NamedTextColor.YELLOW)
            .decoration(TextDecoration.ITALIC, false);

    public GiveTaskClasseur(Player player) {
        this.player = player;
    }

    public boolean giveClasseur(){

        Inventory inv = player.getInventory();

        inv.addItem(CreateItem.getItem(new ItemStack(Material.BOOK), itemName, "&fClique droit ouvre l'inventaire de tâche", "&fClique gauche ouvre le menu de quête"));

        return true;
    }
}
