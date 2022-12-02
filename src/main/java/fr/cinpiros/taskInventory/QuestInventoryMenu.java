package fr.cinpiros.taskInventory;

import fr.cinpiros.commands.GiveTaskClasseur;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class QuestInventoryMenu {
    static public Component invName = Component.text(GiveTaskClasseur.itemName+" de quête");
    private final Player player;

    public QuestInventoryMenu(Player player) {
        this.player = player;
    }

    public void openMenu() {
        Inventory inv  = Bukkit.createInventory(player, 9*6, invName);

        player.openInventory(inv);

    }
}
