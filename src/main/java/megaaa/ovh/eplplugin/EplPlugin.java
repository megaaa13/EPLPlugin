package megaaa.ovh.eplplugin;

import megaaa.ovh.eplplugin.customitems.MobMerger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EplPlugin extends JavaPlugin {
    public static void main(String[] args) {

    }

    @Override
    public void onEnable() {
        Objects.requireNonNull(this.getCommand("coords")).setExecutor(new CoordsCommand());
        Objects.requireNonNull(this.getCommand("afk")).setExecutor(new AfkCommand());
        Objects.requireNonNull(this.getCommand("morts")).setExecutor(new MortScore());
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        this.getServer().getPluginManager().registerEvents(new AntiLag(), this);

        MobMerger.addMergerRecipe();

        Bukkit.getConsoleSender().sendMessage("Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static EplPlugin getPlugin() {
        return EplPlugin.getPlugin(EplPlugin.class);
    }
}
