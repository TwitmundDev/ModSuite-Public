package fr.twitmund.modSuite.utils;

import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatUtil {

    public static void chatNotify(String message, PermissionEnum permissionEnum) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(permissionEnum.getValue())) {
                player.sendMessage(message);
            }
        }
    }

    public static void sendTitle(Player player, String title, String subtitle) {
        player.sendTitle(title, subtitle, 10, 70, 20);
    }

    public static void notifyStaff(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PermissionEnum.PERMISSION_NOTIFY.getValue())) {
                player.sendMessage(message);
            }
        }
    }
}
