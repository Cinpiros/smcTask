package fr.cinpiros.inventory;

import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.exeption.TaskCreateException;
import fr.cinpiros.task.TaskCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class OpenInventory extends UtilsDatabase {

    static public final Component invPanelName = Component.text("Task Panel").color(NamedTextColor.YELLOW)
            .decoration(TextDecoration.ITALIC, false);
    static public final Component invTaskName = GiveTaskClasseur.itemName
            .append(Component.text(" Task").color(NamedTextColor.AQUA)
                    .decoration(TextDecoration.ITALIC, false));
    static public final Component invQuestName = GiveTaskClasseur.itemName
            .append(Component.text(" Quest").color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.ITALIC, false));
    static public final Component panelName = Component.text("Task");
    private final Player player;

    public OpenInventory(Player player) {
        this.player = player;
    }

    public void taskMenu() {
        Inventory inv  = Bukkit.createInventory(player, 9*3, invTaskName);
        String uuid = this.player.getUniqueId().toString();
        try (Connection conn = getConnection()) {
            PreparedStatement psSelectPlayerTaskInventory = conn.prepareStatement(selectPlayerTaskInventory(super.prefix, uuid));
            ResultSet rsPlayerTaskInventory = psSelectPlayerTaskInventory.executeQuery();
            TaskCreator getTaskInventory = new TaskCreator();
            while (rsPlayerTaskInventory.next()) {
                inv.setItem(rsPlayerTaskInventory.getInt(2),
                        getTaskInventory.getTaskForInventory(rsPlayerTaskInventory.getInt(1)));
            }
        } catch (SQLException e) {
            this.player.sendMessage("Error on task inventory load contact a administrator to fix it");
            e.printStackTrace();
        } catch (TaskCreateException e) {
            this.player.sendMessage("Error on task inventory load contact a administrator to fix it");
            Bukkit.getLogger().warning(e.getMessage());
        }


        player.openInventory(inv);
    }


    public void questMenu() {
        Inventory inv  = Bukkit.createInventory(player, 9*6, invQuestName);

        player.openInventory(inv);

    }

    public boolean panelMenu() {
        Inventory inv  = Bukkit.createInventory(this.player, 9*6, invPanelName);
        String prefix = super.prefix;
        ArrayList<String> task_id_list = new ArrayList<>();
        ArrayList<Integer> rarity_list =  new ArrayList<>();
        int total_rarity = 0;
        ArrayList<String> selected_task_id = new ArrayList<>();
        //a faire : check si le joueur a une entré dans daily task sinon ajouter faire une boucle pour récupérer 21 task et les afficher event de prise de task avec ajout task id ect
        try (Connection conn = getConnection()) {
            PreparedStatement psSelectAllTaskIdRairty = conn.prepareStatement(selectAllTaskIdRairty(prefix));
            ResultSet rsSelectAllTaskIdRairty = psSelectAllTaskIdRairty.executeQuery();

            while (rsSelectAllTaskIdRairty.next()) {
                task_id_list.add(rsSelectAllTaskIdRairty.getString(1));
                int rarity = rsSelectAllTaskIdRairty.getInt(2);
                rarity_list.add(rarity);
                total_rarity = total_rarity + rarity;
            }

            int random = (new Random().nextInt(total_rarity))+1;

            for (int ct = 0; ct < ) {
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }





        player.openInventory(inv);

        return true;
    }
}
