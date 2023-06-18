package megaaa.ovh.eplplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.jetbrains.annotations.NotNull;

public class MortScore implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (args.length != 1) {
                return false;
            }
            Player player = (Player) sender;
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            if (args[0].equals("on")) {
                Scoreboard board = manager.getMainScoreboard();
                player.setScoreboard(board);
                final Objective objective = board.getObjective("Morts");
                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                objective.setDisplayName(ChatColor.RED + "Morts");
            } else if (args[0].equals("off")) {
                Scoreboard board = manager.getNewScoreboard();
                player.setScoreboard(board);
            }
            return true;
        }
        return false;
    }
}
