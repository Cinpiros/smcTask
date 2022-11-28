package fr.cinpiros.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CreateItem {
    static public ItemStack getItem(ItemStack item, String name, String ... lore) {
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(ChatColor.translateAlternateColorCodes('&', name)));

        List<Component> lores = new ArrayList<>();
        for (String s : lore) {
            lores.add(Component.text(ChatColor.translateAlternateColorCodes('&', s)));
        }
        meta.lore(lores);

        item.setItemMeta(meta);
        return item;
    }
}
