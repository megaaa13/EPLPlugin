package megaaa.ovh.eplplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class AfkManager implements Listener {
    static HashMap<Player, Boolean> isAFK = new HashMap<>();
    static HashMap<Player, Location> lastPosition = new HashMap<>();

    static Plugin plugin = EplPlugin.getPlugin();

    int sleepers = 0;
    float sleepRatio = 0.25F;
    static int timeForAFK = 60; // in seconds

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        Player p = event.getPlayer();
        sleepers++;

        new BukkitRunnable() {
            Player player = p;
            long currentTime = p.getWorld().getTime();
            int sleepersNeeded = Math.max(Bukkit.getOnlinePlayers().size() - countAFKs(), 1);

            @Override
            public void run() {
                if ((float) getSleepers() / sleepersNeeded > sleepRatio && currentTime > 13000) {
                    player.getWorld().setTime(currentTime + 100);
                    currentTime = player.getWorld().getTime();
                    sleepersNeeded = Math.max(Bukkit.getOnlinePlayers().size() - countAFKs(), 1);
                } else {
                    if (currentTime >= 0) {
                        for (Player p: Bukkit.getOnlinePlayers()) {
                            p.setStatistic(Statistic.TIME_SINCE_REST, 0);
                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    public void onBedLeave(PlayerBedLeaveEvent event) {
        sleepers--;
    }

    public int getSleepers() {
        return sleepers;
    }

    public int countAFKs() {
        int afks = 0;
        for (Player p: Bukkit.getOnlinePlayers()) {
            if (isAFK.get(p)) {
                afks++;
            }
        }
        return afks;
    }

    public static void toggleAfk(Player p) {
        boolean newAFK = !getAFK(p);
        isAFK.put(p, newAFK);
        TextComponent component;
        TextColor color = p.displayName().color();
        if (newAFK) {
            component = Component.text(String.format("[AFK] %s", p.getName()));
            p.displayName(component.color(color));
            p.playerListName(component.color(color));
            p.sendMessage(Component.text("Mode AFK activé").color(TextColor.color(87, 255, 83)));

            new BukkitRunnable() {
                Player player = p;

                @Override
                public void run() {
                    Location playerLocation = player.getLocation();
                    if (playerLocation.distance(getLastLocation(p)) > 5) {
                        toggleAfk(player);
                        cancel();
                    }
                    lastPosition.put(p, p.getLocation());
                }
            }.runTaskTimerAsynchronously(plugin, 0, 20);
        } else {
            p.playerListName(Component.text(p.getName()).color(color));
            p.displayName(Component.text(p.getName()).color(color));
            p.sendMessage(Component.text("Mode AFK désactivé").color(TextColor.color(255, 70, 38)));
        }
    }

    public static void startAFKChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location playerLocation;

                for (Player p: Bukkit.getOnlinePlayers()) {
                    playerLocation = p.getLocation();
                    if (playerLocation.distance(getLastLocation(p)) < 5 && !getAFK(p)) {
                        toggleAfk(p);
                    }
                    lastPosition.put(p, p.getLocation());
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L *timeForAFK);
    }

    public static boolean getAFK(Player player) {
        if (isAFK.containsKey(player)) return isAFK.get(player);
        isAFK.put(player, false);
        return false;
    }

    public static Location getLastLocation(Player player) {
        if (lastPosition.containsKey(player)) return lastPosition.get(player);
        lastPosition.put(player, player.getLocation());
        return new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY()+10, player.getLocation().getZ());
    }
}
