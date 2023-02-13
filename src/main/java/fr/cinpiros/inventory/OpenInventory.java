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
        final String prefix = super.prefix;
        final String uuid = this.player.getUniqueId().toString();
        Integer daily_pick_up_task;
        final Integer max_daily_pick_up_task;
        final int number_panel_task; // max 45 9*5

        try (Connection conn = getConnection()) {
            PreparedStatement psSelectPlayerDailyTask = conn.prepareStatement(
                    "SELECT daily_pick_up_task, max_daily_pick_up_task, today, number_panel_task FROM " +
                            prefix+"player_daily_task WHERE uuid = '"+uuid+"';"
            );
            ResultSet rsSelectPlayerDailyTask = psSelectPlayerDailyTask.executeQuery();

            if (rsSelectPlayerDailyTask.next()) {

                daily_pick_up_task = rsSelectPlayerDailyTask.getInt(1);
                max_daily_pick_up_task = rsSelectPlayerDailyTask.getInt(2);
                number_panel_task = rsSelectPlayerDailyTask.getInt(4);

                if (!rsSelectPlayerDailyTask.getDate(3).toString().equals(java.time.LocalDate.now().toString())) {
                    //delete all row where uuid random for  number_panel_task set today to today set daily_pick_up_task to max_daily_pick_up_task
                    // generate task id for number_panel_task and insert it in player_daily_task_list
                }

            } else {
                PreparedStatement psInsertPlayerDailyTask = conn.prepareStatement("INSERT INTO " +
                        prefix+"player_daily_task (uuid, daily_pick_up_task, max_daily_pick_up_task, today, number_panel_task) " +
                        "VALUE ('"+uuid+"', 5, 5, (SELECT CURDATE()), 21);");
                int checkExecute = psInsertPlayerDailyTask.executeUpdate();

                if (checkExecute < 0) {
                    Bukkit.getLogger().warning("[SmcTask] Warn daily task not inserted for user: "+uuid);
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }



        return true;






        /*
        Inventory inv = Bukkit.createInventory(this.player, 9*6, invPanelName);
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

            //for (int ct = 0; ct < ) {
            //}

        } catch (SQLException e) {
            e.printStackTrace();
        }
        player.openInventory(inv);

        return true;
        */
    }
}
