package megaaa.ovh.eplplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Random;

public class EventListener implements Listener {

    String[] joinMessages = {"Hé mais c'est %s qui vient de se co ! Re mec !",
            "Hey %s ! Content de te voir !",
            "Salut %s !",
            "Wesh %s !",
            "Oh, c'est encore %s...",
            "Bah dis donc %s, tu bosses bien ?",
            "Les gars, les gars, %s est là !! Tu vois, tout le monde s'en fiche."};

    String[] quitMessages = {"Au plaisir %s !",
            "Devinez qui part sans dire au revoir ? C'est %s !",
            "Bye %s !",
            "Bon bah salut hein %s...",
            "%s ne manquera à personne :)",
            "Enfin %s lâche son écran !"
            };
    //int[] colors = {0xFF5555, 0xFFAA00, 0xFFFF55, 0x00AA00, 0x55FF55, 0x55FFFF, 0x00AAAA, 0x5555FF, 0xFF55FF, 0xAA00AA, 0xFFFFFF};

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TextComponent message = Component.text(String.format(joinMessages[new Random().nextInt(joinMessages.length)], event.getPlayer().getName())).color(TextColor.color(0xFFFF55));
        TextColor color = TextColor.color(new Random().nextInt(256), new Random().nextInt(256), new Random().nextInt(256));
        event.getPlayer().playerListName(event.getPlayer().playerListName().color(color));
        event.getPlayer().displayName(event.getPlayer().playerListName().color(color));
        event.getPlayer().sendPlayerListHeader(Component.text("Serveur de l'EPL").color(TextColor.color(0x1CA1FF)).decorate(TextDecoration.BOLD));
        event.joinMessage(message);
    }

    @EventHandler
    public void onLeft(PlayerQuitEvent event) {
        TextComponent message = Component.text(String.format(quitMessages[new Random().nextInt(quitMessages.length)], event.getPlayer().getName())).color(TextColor.color(0xFFFF55));
        event.quitMessage(message);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), Sound.BLOCK_BELL_USE, 1F, 0.5F);
        }
    }
}
