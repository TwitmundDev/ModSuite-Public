package fr.twitmund.modSuite.commands.bans;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.entity.TempBanEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.db.repository.TempBanEntityRepository;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.TimeUtil;
import fr.twitmund.modSuite.utils.discordWebhook.DiscordWebHookSender;
import fr.twitmund.modSuite.utils.discordWebhook.MessageType;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TempBanCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {
        Player author = (Player) sender;
        long time = 0;

        if (!sender.hasPermission(PermissionEnum.PERMISSION_TEMPBAN.getValue())) {
            author.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 4) {
            sender.sendMessage(LangEnum.USAGE.getValue()
                    .replace("%usage%", "/tempban <player> <number> <time> <reason>"));
            return false;
        }
        try {
            time = Long.parseLong(args[1]);
        } catch (NumberFormatException e) {
            author.sendMessage(LangEnum.INVALID_NUMBER.getValue());
            return false;
        }
        if (time <= 0) {
            author.sendMessage(LangEnum.INVALID_NUMBER.getValue());
            return false;
        }
        switch (args[2].toUpperCase()) {
            case "MINUTE","M"   -> time = System.currentTimeMillis() + (time * TimeUtil.MINUTE.toMillis());
            case "HOUR","H"     -> time = System.currentTimeMillis() + (time * TimeUtil.HOUR.toMillis());
            case "DAY","D"      -> time = System.currentTimeMillis() + (time * TimeUtil.DAY.toMillis());
            case "WEEK","W"     -> time = System.currentTimeMillis() + (time * TimeUtil.WEEK.toMillis());
            case "MONTH","MO"   -> time = System.currentTimeMillis() + (time * TimeUtil.MONTH.toMillis());
            case "YEAR","Y"     -> time = System.currentTimeMillis() + (time * TimeUtil.YEAR.toMillis());
            default -> {
                author.sendMessage(LangEnum.INVALID_DURATION.getValue());
                return false;
            }
        }


        StringBuilder reason = new StringBuilder();
        for (int i = 3; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        try {
            Player target = Bukkit.getPlayer(args[0]);
            PlayerEntity authorEntity = PlayerEntityRepository.getPlayerByName(author.getName());
            PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(args[0]);
            if ((targetEntity == null && target == null)) {
                author.sendMessage(LangEnum.PLAYER_NOT_FOUND.getValue().replace("%player%", args[0]));
                return false;
            }
            if (TempBanEntityRepository.isPlayerBanned(targetEntity).getValue()) {
                author.sendMessage(LangEnum.BAN_PLAYER_ALREADY_BANNED.getValue().replace("%player%", args[0]));
                return false;
            }
            Pair<TempBanEntity, String> result = TempBanEntityRepository.createTempBanEntity(reason.toString(), authorEntity, targetEntity, time).orElseThrow();
            author.sendMessage(result.getValue().replace("%player%", targetEntity.getPlayer_Name()).replace("%reason%", reason.toString()));

            if (target != null) {
                target.kickPlayer(LangEnum.BAN_MESSAGE.getValue()
                        .replace("%reason%", reason.toString())
                        .replace("%author%", author.getName())
                        .replace("%ban_id%",String.valueOf(result.getKey().getTempBan_id())));
            }
            DiscordWebHookSender.sendWebhook(MessageType.TEMPBAN, authorEntity.getPlayer_Name(), targetEntity.getPlayer_Name(), reason.toString());

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
            case 2 -> List.of("<Number>");
            case 3 -> List.of("MINUTE", "HOUR", "DAY", "WEEK", "MONTH", "YEAR" , "M", "H", "D", "W", "MO", "Y");
            case 4 -> List.of("<Reason>");
            default -> List.of("");
        };
    }

}
