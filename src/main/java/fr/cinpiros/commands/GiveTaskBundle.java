package fr.cinpiros.commands;

import fr.cinpiros.utils.CreateItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GiveTaskBundle {

    private final Player player;

    public GiveTaskBundle(Player player) {
        this.player = player;
    }

    public boolean giveBundle(){

        Inventory inv = player.getInventory();

        inv.addItem(CreateItem.getItem(new ItemStack(Material.BUNDLE), "&ePochette de tâche", "&f Clique droit ouvre l'inventaire de tâche", "&fClique gauche ouvre le menu de quête"));

        return true;
    }
}
