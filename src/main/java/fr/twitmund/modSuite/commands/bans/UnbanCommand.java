package fr.twitmund.modSuite.commands.bans;

import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnbanCommand implements TabExecutor {

    //TODO add wehbhook to send message to discord when a player is unbanned
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        Player sender = (Player) commandSender;
        if (!sender.hasPermission(PermissionEnum.PERMISSION_UNBAN.getValue())) {
            sender.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 2) {
            commandSender.sendMessage(LangEnum.USAGE.getValue()
                    .replace("%usage%", "/unban <player> <reason>"));
            return false;
        }
        PlayerEntity authorEntity = PlayerEntityRepository.getPlayerByName(sender.getName());
        PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(args[0]);
        if (targetEntity == null) {
            commandSender.sendMessage(LangEnum.PLAYER_NOT_FOUND.getValue());
            return false;
        }
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        if (BanEntityRepository.isPlayerBannedBool(targetEntity)) {
            Pair<Boolean, String> unbanInfo = BanEntityRepository.unbanPlayer(targetEntity, authorEntity, reason.toString());
            if (unbanInfo.getKey()) {
                commandSender.sendMessage(LangEnum.UNBAN_SUCCESS.getValue().replace("%player%", targetEntity.getPlayer_Name()));
            } else {
                commandSender.sendMessage(unbanInfo.getValue());
            }
        } else {
            commandSender.sendMessage(LangEnum.UNBAN_PLAYER_NOT_BANNED.getValue().replace("%player%", targetEntity.getPlayer_Name()));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {
        if (!commandSender.hasPermission(PermissionEnum.PERMISSION_UNBAN.getValue())) {
            return List.of();
        }
        return switch (args.length) {
            case 1 -> BanEntityRepository.getAllCurrentBannedPlayersUsername();
            case 2 -> List.of("<Reason>");
            default -> List.of();
        };
    }
}
