package fr.twitmund.modSuite.commands.staff;

import fr.twitmund.modSuite.manager.staff.FreezeManager;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FreezeCommand implements TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission(PermissionEnum.PERMISSION_FREEZE_COMMAND.getValue())) {
            player.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (strings.length < 1) {
            player.sendMessage(LangEnum.USAGE.getValue().replace("%usage%", "/freeze <player>"));
            return false;
        }
        String targetName = strings[0];

        if (Bukkit.getPlayer(targetName) ==  null){
            player.sendMessage(LangEnum.PLAYER_NOT_FOUND.getValue().replace("%player%", targetName));
            return false;
        }
        Player target = Bukkit.getPlayer(targetName);
        if (FreezeManager.isPlayerFreezed(target)) {
            player.sendMessage("Unfreeze " + targetName);
            FreezeManager.unfreezePlayer(target);
        }else{
            player.sendMessage("Freeze " + targetName);
            FreezeManager.freezePlayer(target);
        }


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .toList();
    }
}
