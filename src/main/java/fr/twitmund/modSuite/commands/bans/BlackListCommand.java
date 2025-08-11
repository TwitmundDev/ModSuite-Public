package fr.twitmund.modSuite.commands.bans;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BlackListEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.BlackListEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlackListCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player author)) {
            sender.sendMessage(LangEnum.ONLY_PLAYER_CAN_USE.getValue());
            return false;
        }
        if (!author.hasPermission("modSuite.blacklist")) {
            author.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 2) {
            author.sendMessage(LangEnum.USAGE.getValue()
                    .replace("%usage%", "/blacklist <player> <reason>"));
            return false;
        }
        String playerName = args[0];
        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        try {
            PlayerEntity playerEntityAuthor = PlayerEntityRepository.getPlayerByName(author.getName());
            BlackListEntity blackListEntity = new BlackListEntity(playerName, reason.toString(),playerEntityAuthor);

            if (BlackListEntityRepository.isPlayerBlackListed(playerName)) {
                sender.sendMessage(LangEnum.BLACKLIST_PLAYER_ALREADY_BLACKLISTED.getValue().replace("%player%", playerName));
                return false;
            }else {
                BlackListEntityRepository.createBlackListEntity(blackListEntity);
                sender.sendMessage(LangEnum.BLACKLIST_SUCCESS.getValue().replace("%player%", playerName));
                if (Bukkit.getPlayer(playerName) != null) {
                    Player targetPlayer = Bukkit.getPlayer(playerName);
                    targetPlayer.kickPlayer(LangEnum.BAN_MESSAGE.getValue().replace("%reason%", reason.toString()));
                }
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Error while processing blacklist command: " + e.getMessage());
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of("<player>", "<reason>");
    }
}
