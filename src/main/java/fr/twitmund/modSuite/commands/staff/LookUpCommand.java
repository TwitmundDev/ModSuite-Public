package fr.twitmund.modSuite.commands.staff;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.MuteEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.db.repository.WarnEntityRepository;
import fr.twitmund.modSuite.gui.LookupGUI;
import fr.twitmund.modSuite.gui.PaginatedGuiBan;
import fr.twitmund.modSuite.gui.PaginatedGuiMute;
import fr.twitmund.modSuite.gui.PaginatedGuiWarn;
import fr.twitmund.modSuite.utils.DateFormatter;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class LookUpCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, @NotNull String[] args) {
        //todo add ban + ajout de la Mise en forme du GUI
        if (!(sender instanceof Player player)) return false;
        if (!player.hasPermission(PermissionEnum.PERMISSION_COMMAND_LOOKUP.getValue())) {
            player.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return false;
        }
        if (args.length < 1) {
            player.sendMessage(LangEnum.USAGE.getValue().replace("%usage%", "/lookup <player>"));
            return false;
        }

        PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(args[0]);
        Gui gui = LookupGUI.createGui(targetEntity,player);
        gui.open(player);


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> PlayerEntityRepository.getAllPlayersNames();
            default -> List.of("");
        };
    }

}
