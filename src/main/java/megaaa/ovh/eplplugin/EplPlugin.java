package megaaa.ovh.eplplugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EplPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("coords").setExecutor(new CoordsCommand());
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
        Bukkit.getConsoleSender().sendMessage("Plugin enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
