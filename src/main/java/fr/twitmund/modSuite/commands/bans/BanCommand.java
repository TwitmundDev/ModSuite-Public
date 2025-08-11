package fr.twitmund.modSuite.commands.bans;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.discordWebhook.DiscordWebHookSender;
import fr.twitmund.modSuite.utils.discordWebhook.MessageType;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BanCommand implements TabExecutor {

    //TODO add wehbhook to send message to discord when a player is banned
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player author = (Player) commandSender;
        if (!author.hasPermission(PermissionEnum.PERMISSION_BAN.getValue())) {
            author.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 2) {
            commandSender.sendMessage(LangEnum.USAGE.getValue()
                    .replace("%usage%", "/ban <player> <reason>"));
            return false;
        }


        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        try {
            Player target = Bukkit.getPlayer(args[0]);
            PlayerEntity authorEntity = PlayerEntityRepository.getPlayerByName(author.getName());
            PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(args[0]);
            if ((targetEntity == null && target == null)) {
                commandSender.sendMessage(LangEnum.PLAYER_NOT_FOUND.getValue().replace("%player%", args[0]));
                return false;
            }
            if (BanEntityRepository.isPlayerBanned(targetEntity).getValue()) {
                commandSender.sendMessage(LangEnum.BAN_PLAYER_ALREADY_BANNED.getValue().replace("%player%", args[0]));
                return false;
            }
            Pair<BanEntity, String> result = BanEntityRepository.createBanEntity(reason.toString(), authorEntity, targetEntity).orElseThrow();
            author.sendMessage(result.getValue().replace("%player%", targetEntity.getPlayer_Name()).replace("%reason%", reason.toString()));

            if (target != null) {
                target.kickPlayer(LangEnum.BAN_MESSAGE.getValue()
                        .replace("%reason%", reason.toString())
                        .replace("%author%", author.getName())
                        .replace("%ban_id%",String.valueOf(result.getKey().getBan_id())));
            }
            DiscordWebHookSender.sendWebhook(MessageType.BAN, authorEntity.getPlayer_Name(), targetEntity.getPlayer_Name(), reason.toString());

        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Error while creating warn entity: " + e);
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> PlayerEntityRepository.getAllPlayersNames();
            case 2 -> List.of("<Reason>");
            default -> List.of("");
        };
    }
}
