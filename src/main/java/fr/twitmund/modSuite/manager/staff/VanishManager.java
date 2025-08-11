package fr.twitmund.modSuite.manager.staff;

import fr.twitmund.modSuite.Main;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class VanishManager {

    @Getter
    public static Map<Player,Boolean> vanishedPlayers = new HashMap<>();

    public static boolean isVanished(Player player) {
        return vanishedPlayers.getOrDefault(player, false);
    }
    public static void setVanished(Player player, boolean vanished) {
        vanishedPlayers.put(player, vanished);
        if (vanished) {
            for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                onlinePlayer.hidePlayer(Main.getInstance(),player);
            }
        } else {
            for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                onlinePlayer.showPlayer(Main.getInstance(),player);
            }
        }
    }
}
