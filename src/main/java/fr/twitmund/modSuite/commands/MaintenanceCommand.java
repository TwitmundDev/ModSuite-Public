package fr.twitmund.modSuite.commands;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("modSuite.maintenance")) {
            sender.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(LangEnum.MAINTENANCE_USAGE.getValue());
            return false;
        }
        if (args[0].equalsIgnoreCase("on")) {
            if (Main.isMaintenanceStatus()) {
                sender.sendMessage(LangEnum.MAINTENANCE_ALREADY_ON.getValue());
                return false;
            }
            Main.setMaintenanceStatus(true);
            sender.sendMessage(LangEnum.MAINTENANCE_ON.getValue());
            kickNonBypass();
        } else if (args[0].equalsIgnoreCase("off")) {
            if (!Main.isMaintenanceStatus()) {
                sender.sendMessage(LangEnum.MAINTENANCE_ALREADY_OFF.getValue());
                return false;
            }
            Main.setMaintenanceStatus(false);
            sender.sendMessage(LangEnum.MAINTENANCE_OFF.getValue());
        } else {
            sender.sendMessage(LangEnum.MAINTENANCE_USAGE.getValue());

        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("on");
            completions.add("off");
        }
        return completions;
    }

    public void kickNonBypass(){
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!player.hasPermission(PermissionEnum.PERMISSION_MAINTENANCE_BYPASS.getValue())) {
                player.kickPlayer(LangEnum.MAINTENANCE_KICK_MESSAGE.getValue());
            }
        }
    }

}
