package megaaa.ovh.eplplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class AfkCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1) {
                return false;
            }
            Player p = (Player) sender;
            TextComponent component;
            TextColor color = p.displayName().color();
            if (args[0].equals("off")) {
                p.playerListName(Component.text(p.getName()).color(color));
                p.displayName(Component.text(p.getName()).color(color));
                p.sendMessage(Component.text("Mode AFK désactivé").color(TextColor.color(255, 70, 38)));
            } else {
                component = Component.text(String.format("[AFK] %s", p.getName()));
                p.displayName(component.color(color));
                p.playerListName(component.color(color));
                p.sendMessage(Component.text("Mode AFK activé").color(TextColor.color(87, 255, 83)));
            }
            return true;
        }
        return false;
    }
}
