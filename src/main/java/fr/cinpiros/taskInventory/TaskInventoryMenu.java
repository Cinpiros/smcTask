package fr.cinpiros.taskInventory;

import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class TaskInventoryMenu {

    static public Component invName = Component.text(GiveTaskClasseur.itemName);
    private final Player player;

    public TaskInventoryMenu(Player player) {
        this.player = player;
    }

    public void openMenu() {
        Inventory inv  = Bukkit.createInventory(player, 9*6, invName);

        player.openInventory(inv);

    }
}
