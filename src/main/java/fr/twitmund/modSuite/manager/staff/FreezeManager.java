package fr.twitmund.modSuite.manager.staff;

import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FreezeManager {

    @Getter
    @Setter
    static Map<Player, Location> freezedPlayer = new HashMap<>();

    public static boolean isPlayerFreezed(Player player) {
        return freezedPlayer.containsKey(player);
    }

    public static void freezePlayer(Player player) {
        if (!isPlayerFreezed(player)) {
            freezedPlayer.put(player, player.getLocation());
            player.sendMessage(LangEnum.FREEZE_PLAYER_MESSAGE.getValue());
        } else {
            player.sendMessage("You are already frozen.");
        }
    }

    public static void unfreezePlayer(Player player) {
        if (isPlayerFreezed(player)) {
            player.teleport(freezedPlayer.get(player));
            freezedPlayer.remove(player);
            player.sendMessage(LangEnum.UNFREEZE_PLAYER_MESSGE.getValue());
        } else {
            player.sendMessage("You are not frozen.");
        }
    }
}
