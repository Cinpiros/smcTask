package fr.cinpiros.inventory;

import fr.cinpiros.commands.GiveTaskClasseur;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class QuestInventory {
    static public Component invName = GiveTaskClasseur.itemName;
    private final Player player;

    public QuestInventory(Player player) {
        this.player = player;
    }

    public void openMenu() {
        Inventory inv  = Bukkit.createInventory(player, 9*6, invName);

        player.openInventory(inv);

    }
}
