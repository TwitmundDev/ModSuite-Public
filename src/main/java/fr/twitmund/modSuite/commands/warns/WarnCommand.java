package fr.twitmund.modSuite.commands.warns;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.db.repository.WarnEntityRepository;
import fr.twitmund.modSuite.utils.ChatUtil;
import fr.twitmund.modSuite.utils.discordWebhook.DiscordWebHookSender;
import fr.twitmund.modSuite.utils.discordWebhook.MessageType;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WarnCommand implements TabExecutor {

    //TODO add wehbhook to send message to discord when a player is warned
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission(PermissionEnum.PERMISSION_WARN.getValue())) {
            sender.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(LangEnum.USAGE.getValue().replace("%usage%", "/warn <Player> <Reason>"));
            return false;
        }
        Player author = (Player) sender;;

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }


        try {
            Player target = Bukkit.getPlayer(args[0]);
            PlayerEntity authorEntity = PlayerEntityRepository.getPlayerByName(author.getName());
            PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(args[0]);
            if (targetEntity == null) {
                sender.sendMessage(LangEnum.PLAYER_NOT_FOUND.getValue());
                return false;
            }

            if (!(target == null)) {
                target.playSound(target.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
                target.sendMessage(LangEnum.WARN_TITLE.getValue() + " " +LangEnum.WARN_SUBTITLE.getValue().replace("%reason%", reason.toString()));
                ChatUtil.sendTitle(target, LangEnum.WARN_TITLE.getValue(), LangEnum.WARN_SUBTITLE.getValue().replace("%reason%", reason.toString()));
            }

            WarnEntityRepository.createWarnEntity(reason.toString(), authorEntity, targetEntity);
            sender.sendMessage(LangEnum.WARN_SUCCESS.getValue().replace("%player%", targetEntity.getPlayer_Name()).replace("%reason%", reason.toString()));

            DiscordWebHookSender.sendWebhook(MessageType.WARN, authorEntity.getPlayer_Name(), targetEntity.getPlayer_Name(), reason.toString());
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
            default -> List.of();
        };
    }
}
