package fr.cinpiros.inventory;

import fr.cinpiros.commands.GiveTaskClasseur;
import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.exeption.TaskCreateException;
import fr.cinpiros.task.TaskCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class OpenInventory extends UtilsDatabase {

    static public final Component invPanelName = Component.text("Task Panel")
            .color(NamedTextColor.YELLOW)
            .decoration(TextDecoration.ITALIC, false);
    static public final Component invTaskName = GiveTaskClasseur.itemName
            .append(Component.text(" Task")
                    .color(NamedTextColor.AQUA)
                    .decoration(TextDecoration.ITALIC, false));
    static public final Component invQuestName = GiveTaskClasseur.itemName
            .append(Component.text(" Quest")
                    .color(NamedTextColor.GREEN)
                    .decoration(TextDecoration.ITALIC, false));
    private final Player player;

    public OpenInventory(Player player) {
        this.player = player;
    }

    public void taskMenu() {
        Inventory inv  = Bukkit.createInventory(player, 9*3, invTaskName);
        String uuid = this.player.getUniqueId().toString();
        try (Connection conn = getConnection()) {
            PreparedStatement psSelectPlayerTaskInventory = conn.prepareStatement("SELECT FK_task_instance_id, slot FROM "+
                    super.prefix+"player_task_inventory WHERE uuid = '"+uuid+"' ORDER BY slot;");
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
        final String uuid = this.player.getUniqueId().toString();
        int daily_pick_up_task;
        final int max_daily_pick_up_task;
        int number_panel_task; // max 45 9*5
        Map<Integer, String> task_id_list = new HashMap<>();

        try (Connection conn = getConnection()) {
            PreparedStatement psSelectPlayerDailyTask = conn.prepareStatement(
                    "SELECT daily_pick_up_task, max_daily_pick_up_task, today, number_panel_task FROM " +
                            super.prefix + "player_daily_task WHERE uuid = '" + uuid + "';"
            );
            ResultSet rsSelectPlayerDailyTask = psSelectPlayerDailyTask.executeQuery();

            if (rsSelectPlayerDailyTask.next()) {

                daily_pick_up_task = rsSelectPlayerDailyTask.getInt(1);
                max_daily_pick_up_task = rsSelectPlayerDailyTask.getInt(2);
                number_panel_task = rsSelectPlayerDailyTask.getInt(4);

                if (number_panel_task > 45) {
                    number_panel_task = 45;
                }

                if (!rsSelectPlayerDailyTask.getDate(3).toString().equals(java.time.LocalDate.now().toString())) {
                    PreparedStatement psDeletePlayerDailyTaskList = conn.prepareStatement("DELETE FROM " +
                            super.prefix + "player_daily_task_list WHERE FK_uuid = '" + uuid + "';");
                    int checkDeleteExecute = psDeletePlayerDailyTaskList.executeUpdate();

                    if (checkDeleteExecute < 0) {
                        Bukkit.getLogger().warning("[SmcTask] Warn daily task list not deleted for user: " + uuid);
                        player.sendMessage("Une erreur est survenue lors de l'ouverture du paneau");
                        return true;
                    }

                    PreparedStatement psUpdatePlayerDailyTask = conn.prepareStatement("UPDATE " + super.prefix + "player_daily_task " +
                            "SET daily_pick_up_task = " + max_daily_pick_up_task + ", today = (SELECT CURDATE()) WHERE uuid = '" + uuid + "';");
                    int checkUpdateExecute = psUpdatePlayerDailyTask.executeUpdate();

                    if (checkUpdateExecute < 0) {
                        Bukkit.getLogger().warning("[SmcTask] Warn daily task not updated" +
                                " with current day and max pick up tack for user: " + uuid);
                        player.sendMessage("Une erreur est survenue lors de l'ouverture du paneau");
                        return true;
                    }
                    daily_pick_up_task = max_daily_pick_up_task;

                    task_id_list = generateRandomTask(number_panel_task, uuid);
                } else {
                    PreparedStatement psSelectPlayerDailyTaskList = conn.prepareStatement("SELECT FK_task_id, slot FROM " +
                            super.prefix + "player_daily_task_list WHERE FK_uuid = '" + uuid + "';");
                    ResultSet rsSelectPlayerDailyTaskList = psSelectPlayerDailyTaskList.executeQuery();

                    while (rsSelectPlayerDailyTaskList.next()) {
                        task_id_list.put(rsSelectPlayerDailyTaskList.getInt(2), rsSelectPlayerDailyTaskList.getString(1));
                    }
                }

            } else {
                PreparedStatement psInsertPlayerDailyTask = conn.prepareStatement("INSERT INTO " +
                        super.prefix + "player_daily_task (uuid, daily_pick_up_task, max_daily_pick_up_task, today, number_panel_task) " +
                        "VALUE ('" + uuid + "', 5, 5, (SELECT CURDATE()), 21);");
                int checkExecute = psInsertPlayerDailyTask.executeUpdate();

                if (checkExecute < 0) {
                    Bukkit.getLogger().warning("[SmcTask] Warn daily task not inserted for user: " + uuid);
                    player.sendMessage("Une erreur est survenue lors de l'ouverture du paneau");
                    return true;
                }
                number_panel_task = 21;
                daily_pick_up_task = 5;
                max_daily_pick_up_task = 5;
                task_id_list = generateRandomTask(number_panel_task, uuid);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        if (task_id_list.size() != number_panel_task - (max_daily_pick_up_task - daily_pick_up_task)) {
            Bukkit.getLogger().warning("[SmcTask] Warn daily task list not equal panel daily task for user: " + uuid);
            player.sendMessage("Une erreur est survenue lors de l'ouverture du paneau");
            return true;
        }

        Inventory inv;

        if (number_panel_task <= 9) {
            inv = Bukkit.createInventory(player, 9*2, invPanelName);
            getTaskPanel(task_id_list, inv);
            getFooterPanel(inv, daily_pick_up_task, max_daily_pick_up_task, 9);

        } else if (number_panel_task <= 18) {
            inv = Bukkit.createInventory(player, 9*3, invPanelName);
            getTaskPanel(task_id_list, inv);
            getFooterPanel(inv, daily_pick_up_task, max_daily_pick_up_task, 18);

        } else if (number_panel_task <= 27) {
            inv = Bukkit.createInventory(player, 9*4, invPanelName);
            getTaskPanel(task_id_list, inv);
            getFooterPanel(inv, daily_pick_up_task, max_daily_pick_up_task, 27);

        } else if (number_panel_task <= 36) {
            inv = Bukkit.createInventory(player, 9*5, invPanelName);
            getTaskPanel(task_id_list, inv);
            getFooterPanel(inv, daily_pick_up_task, max_daily_pick_up_task, 36);

        } else {
            inv = Bukkit.createInventory(player, 9*6, invPanelName);
            getTaskPanel(task_id_list, inv);
            getFooterPanel(inv, daily_pick_up_task, max_daily_pick_up_task, 45);
        }


        player.openInventory(inv);
        return true;
    }

    private void getTaskPanel(Map<Integer, String> task_id_list, Inventory inv) {
        try {
            TaskCreator getTask = new TaskCreator();
            for (Map.Entry<Integer, String> entry : task_id_list.entrySet()) {
                inv.setItem(entry.getKey(), getTask.getTaskForPanel(entry.getValue()));
            }

        } catch (TaskCreateException e) {
            Bukkit.getLogger().warning("[SmcTask] error a get task item for panel");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getFooterPanel(Inventory inv, int daily_pick_up_task, int max_daily_pick_up_task, int start_set_footer) {

        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta =  item.getItemMeta();
        meta.displayName(Component.text("Tache récupérer: "+daily_pick_up_task+"/"+max_daily_pick_up_task)
                .color(NamedTextColor.DARK_AQUA)
                .decoration(TextDecoration.ITALIC, false));
        item.setItemMeta(meta);


        ItemStack fillItem = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        ItemMeta fillItemMeta = fillItem.getItemMeta();
        fillItemMeta.displayName(Component.text(" "));
        fillItem.setItemMeta(fillItemMeta);



        inv.setItem(start_set_footer, fillItem);
        inv.setItem(start_set_footer+1, fillItem);
        inv.setItem(start_set_footer+2, fillItem);
        inv.setItem(start_set_footer+3, fillItem);

        inv.setItem(start_set_footer+4, item);

        inv.setItem(start_set_footer+5, fillItem);
        inv.setItem(start_set_footer+6, fillItem);
        inv.setItem(start_set_footer+7, fillItem);
        inv.setItem(start_set_footer+8, fillItem);
    }



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


    private Map<Integer, String> generateRandomTask(int numberToGenerate, String uuid) {
        Map<Integer, String> taskList = new HashMap<>();
        Map<String, Integer> mapTaskIdRarity = new LinkedHashMap<>();
        int total_rarity = 0;

        try (Connection conn = getConnection()) {
            PreparedStatement psSelectTaskIdRarityForUuid = conn.prepareStatement("SELECT " +
                    super.prefix+"task.id, "+super.prefix+"rarity.rarity FROM "+super.prefix+"task" +
                    " INNER JOIN "+super.prefix+"rarity" +
                    " ON "+super.prefix+"rarity.id = "+super.prefix+"task.FK_rarity_id" +
                    " WHERE (SELECT count("+super.prefix+"task_jobs_level.FK_task_id)" +
                    " FROM "+super.prefix+"task_jobs_level, "+super.prefix+"player_jobs_exp" +
                    " WHERE "+super.prefix+"task_jobs_level.FK_task_id =  "+super.prefix+"task.id" +
                    " AND "+super.prefix+"player_jobs_exp.uuid = '"+uuid+"'" +
                    " AND "+super.prefix+"task_jobs_level.FK_jobs_id = "+super.prefix+"player_jobs_exp.FK_jobs_id" +
                    " AND "+super.prefix+"task_jobs_level.level > "+super.prefix+"player_jobs_exp.level)" +
                    " = 0 ORDER BY "+super.prefix+"rarity.rarity DESC;");
            ResultSet rsSelectTaskIdRarityForUuid = psSelectTaskIdRarityForUuid.executeQuery();

            while (rsSelectTaskIdRarityForUuid.next()) {
                int rarity = rsSelectTaskIdRarityForUuid.getInt(2);
                mapTaskIdRarity.put(rsSelectTaskIdRarityForUuid.getString(1), rarity);
                total_rarity = total_rarity + rarity;
            }

            PreparedStatement psInsertPlayerDailyTaskList = conn.prepareStatement("INSERT INTO " +
                    super.prefix+"player_daily_task_list (FK_uuid, FK_task_id, slot) VALUE ('"+uuid+"', ?, ?)");


            for (int slot = 0; slot < numberToGenerate; slot++) {
                int random = (new Random().nextInt(total_rarity))+1;

                for(String task_id : mapTaskIdRarity.keySet()) {
                    random = random - mapTaskIdRarity.get(task_id);

                    if (random <= 0) {

                        psInsertPlayerDailyTaskList.setString(1, task_id);
                        psInsertPlayerDailyTaskList.setInt(2, slot);
                        psInsertPlayerDailyTaskList.addBatch();

                        taskList.put(slot, task_id);

                        break;
                    }
                }
            }
            int[] checkExecute = psInsertPlayerDailyTaskList.executeBatch();

            for (int check : checkExecute) {
                if (check < 0) {
                    Bukkit.getLogger().warning("[SmcTask] Error batch insert don't insert row for user: "+uuid);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return taskList;
    }
}

