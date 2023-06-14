package megaaa.ovh.eplplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CoordsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            TextComponent component;
            if (p.getLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
                component = Component.text(String.format("§a%s §fest en §a%s§f, §a%s§f, §a%s§f.",
                        p.getName(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
            } else if (p.getLocation().getWorld().getEnvironment() == World.Environment.NETHER) {
                component = Component.text(String.format("§c%s §fest en §c%s§f, §c%s§f, §c%s§f.",
                        p.getName(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
            } else {
                component = Component.text(String.format("§e%s §fest en §e%s§f, §e%s§f, §e%s§f.",
                        p.getName(), p.getLocation().getBlockX(), p.getLocation().getBlockY(), p.getLocation().getBlockZ()));
            }
            Bukkit.broadcast(component);
            return true;
        }
        return false;
    }
}
