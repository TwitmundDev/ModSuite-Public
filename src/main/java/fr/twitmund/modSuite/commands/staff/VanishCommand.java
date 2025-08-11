package fr.twitmund.modSuite.commands.staff;

import fr.twitmund.modSuite.manager.PlayerManager;
import fr.twitmund.modSuite.manager.staff.VanishManager;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VanishCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission(PermissionEnum.PERMISSION_VANISH_COMMAND.getValue())) {
            player.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if(VanishManager.getVanishedPlayers() == null || VanishManager.getVanishedPlayers().isEmpty() || !VanishManager.isVanished(player)) {
            VanishManager.setVanished(player, true);
            player.sendMessage(LangEnum.VANISH_ON.getValue());
        }
        else {
            VanishManager.setVanished(player, false);
            player.sendMessage(LangEnum.VANISH_OFF.getValue());
        }
        return false;
    }
}
