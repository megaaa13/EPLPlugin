package megaaa.ovh.eplplugin.customitems;

import megaaa.ovh.eplplugin.EplPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MobMerger {
    private static Plugin plugin = EplPlugin.getPlugin(EplPlugin.class);
    public static void addMergerRecipe() {
        ItemStack item = new ItemStack( Material.STICK );
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(ChatColor.YELLOW + "Mob merger");
        List<String> lore = new ArrayList<>();
        lore.add("Permets de stacker les animaux!");
        im.setLore(lore);
        item.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "mobMerger");
        ShapedRecipe recipe = new ShapedRecipe(key, item);
        recipe.shape( " G ", " S ", "   " );
        recipe.setIngredient( 'G', Material.GOLD_INGOT );
        recipe.setIngredient( 'S', Material.STICK );

        Bukkit.addRecipe(recipe);
    }
}