package fr.cinpiros.utils;

import fr.cinpiros.database.UtilsDatabase;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Test extends UtilsDatabase {
    public boolean test() {
        try (Connection conn = getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT today FROM smctask_player_daily_task;");
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String today = rs.getDate(1).toString();
                Bukkit.getLogger().info(today);
                String date = java.time.LocalDate.now().toString();
                Bukkit.getLogger().info(date);


                if (date.equals(today)) {
                    Bukkit.getLogger().info("sfqgrthdtyj");
                } else {
                    Bukkit.getLogger().info("azertyui");
                }

            } else {
                Bukkit.getLogger().warning("rezfedsfd");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
