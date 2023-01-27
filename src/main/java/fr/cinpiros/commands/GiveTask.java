package fr.cinpiros.commands;

import fr.cinpiros.SmcTask;
import fr.cinpiros.database.UtilsDatabase;
import fr.cinpiros.utils.CreateItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GiveTask {
    private final Player player;

    public GiveTask(Player player) {
        this.player = player;
    }

    public boolean giveTask(String taskId){
        UtilsDatabase database = new UtilsDatabase();
        Plugin plugin = SmcTask.getInstance();
        Connection conn = database.getConnection(plugin);
        String prefix = database.getPrefix(plugin);
        try {
            PreparedStatement PSselectTask = conn.prepareStatement("SELECT "+prefix+"task.id, "+prefix+"task.name, "+prefix+"task.item, "+prefix+"task.color, "+prefix+"task.item_enchant_effect, "+prefix+"task.reward_money, "+prefix+"rarity.name, "+prefix+"rarity.color" +
                    " FROM "+prefix+"task LEFT OUTER JOIN "+prefix+"rarity ON "+prefix+"task.FK_rarity_id = "+prefix+"rarity.id WHERE "+prefix+"task.id = 'test' LIMIT 1;");
            ResultSet RStask = PSselectTask.executeQuery(); //task_description.lore task_reward_item.item task_reward_item.quantity task_reward_jobs_exp.FK_jobs_id task_reward_jobs_exp.exp jobs.name jobs.color rarity.color task_condition.FK_condition_condition_id condition.description
            if (RStask.next()) {
                String task_id = RStask.getString(1);
                String task_name = RStask.getString(2);
                String task_item = RStask.getString(3);
                String task_color = RStask.getString(4);
                Boolean isEnchant = RStask.getBoolean(5);
                Integer task_money_reward = RStask.getInt(6);
                String rarity_name = RStask.getString(7);
                String rarity_color = RStask.getString(8);
            } else
            ArrayList<String> Task_description = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.closeConnection(conn);
        }
        Inventory inv = player.getInventory();
        Material item = Material.matchMaterial();
        if (item != null) {
            inv.addItem(CreateItem.getItem(new ItemStack(item), Component.text("Task").color(NamedTextColor.AQUA), "&a Clique pour récupérer la Task", "&fRécolter du blé : 0/1000"));
        }
        return true;
    }
}
