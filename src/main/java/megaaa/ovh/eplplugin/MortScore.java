package megaaa.ovh.eplplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MortScore implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1) {
                return false;
            }
            Player player = (Player) sender;
            if (args[0].equals("on")) {
                Objects.requireNonNull(player.getScoreboard().getObjective("Morts")).setDisplaySlot(DisplaySlot.SIDEBAR);
            } else if (args[0].equals("off")) {
                player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            }
            return true;
        }
        return false;
    }
}
