package fr.twitmund.modSuite.commands.mute;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.MuteEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.MuteEntityRepository;
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

import java.util.List;

public class MuteCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        Player sender = (Player) commandSender;
        if (!sender.hasPermission(PermissionEnum.PERMISSION_MUTE.getValue())) {
            sender.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 2) {
            sender.sendMessage(LangEnum.USAGE.getValue().replace("%usage%", "/mute <player> <reason>"));
            return false;
        }

        StringBuilder reason = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            reason.append(" ").append(args[i]);
        }

        try{
            Player target = Bukkit.getPlayer(args[0]);
            PlayerEntity authorEntity = PlayerEntityRepository.getPlayerByName(sender.getName());
            PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(args[0]);
            if(MuteEntityRepository.isPlayerMuted(targetEntity).getValue()){
                sender.sendMessage(LangEnum.MUTE_PLAYER_ALREADY_MUTED.getValue().replace("%player%", args[0]));
                return false;
            }
            if((targetEntity == null && target == null)){
                sender.sendMessage(LangEnum.PLAYER_NOT_FOUND.getValue().replace("%player%", args[0]));
                return false;
            }
            Pair<MuteEntity, String> result = MuteEntityRepository.createMuteEntity(reason.toString(), authorEntity, targetEntity).orElseThrow();
            sender.sendMessage(result.getValue().replace("%player%", targetEntity.getPlayer_Name()).replace("%reason%", reason.toString()));
            if (target != null) {
                target.sendMessage(LangEnum.MUTE_MESSAGE.getValue().replace("%reason%", reason.toString()));
            }
            DiscordWebHookSender.sendWebhook(MessageType.MUTE, authorEntity.getPlayer_Name(), targetEntity.getPlayer_Name(), reason.toString());
        } catch (Exception e){
            Main.getInstance().getLogger().severe("Error while creating warn entity: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> PlayerEntityRepository.getAllPlayersNames();
            case 2 -> List.of("<Reason>");
            default -> List.of();
        };
    }
}
