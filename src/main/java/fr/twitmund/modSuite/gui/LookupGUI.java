package fr.twitmund.modSuite.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.MuteEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.db.repository.WarnEntityRepository;
import fr.twitmund.modSuite.utils.DateFormatter;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;


import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class LookupGUI {


    public static Gui createGui(PlayerEntity targetEntity, Player player) {
        Gui gui = Gui.gui()
                .title(Component.text("Lookup : " + targetEntity.getPlayer_Name()))
                .rows(6)
                .create();


        GuiItem playerHead = ItemBuilder.from(Material.PLAYER_HEAD)
                .setName(targetEntity.getPlayer_Name())
                .addLore(MuteEntityRepository.getCurrentMuteEntity(targetEntity) == null ? LangEnum.GUI_IS_MUTED.getValue() + ChatColor.RED + "✗" :
                        LangEnum.GUI_IS_MUTED.getValue() + ChatColor.GREEN + "✔")
                .addLore(BanEntityRepository.getCurrentBanEntity(targetEntity) == null ? LangEnum.GUI_IS_BANNED.getValue() + ChatColor.RED + "✗" :
                        LangEnum.GUI_IS_BANNED.getValue() + ChatColor.GREEN + "✔")
                .asGuiItem( event ->{
                    event.setCancelled(true);
                });

        GuiItem warns = ItemBuilder.from(Material.WRITABLE_BOOK).setName(LangEnum.GUI_ITEM_WARN.getValue()).asGuiItem(event -> {
            new PaginatedGuiWarn(
                    LangEnum.GUI_WARN_TITLE.getValue().replace("%player%", targetEntity.getPlayer_Name()),
                    6,
                    WarnEntityRepository.getWarnEntityByPlayerName(targetEntity)).createGui().open(player);
        });
        GuiItem mutes = ItemBuilder.from(Material.WRITABLE_BOOK).setName(LangEnum.GUI_ITEM_MUTE.getValue()).asGuiItem(event -> {
            new PaginatedGuiMute(
                    LangEnum.GUI_MUTE_TITLE.getValue().replace("%player%", targetEntity.getPlayer_Name()),
                    6,
                    MuteEntityRepository.getMuteEntityByPlayer(targetEntity)).createGui().open(player);
        });

        GuiItem bans = ItemBuilder.from(Material.WRITABLE_BOOK).setName(LangEnum.GUI_ITEM_BAN.getValue()).asGuiItem(event -> {
            new PaginatedGuiBan(
                    LangEnum.GUI_BAN_TITLE.getValue().replace("%player%", targetEntity.getPlayer_Name()),
                    6,
                    BanEntityRepository.getBanEntitiesByPlayer(targetEntity)).createGui().open(player);
        });

        GuiItem alts = ItemBuilder.from(Material.PLAYER_HEAD).setName(LangEnum.GUI_ALTS_ITEM.getValue()).asGuiItem(event -> {
            AltsGUI guiAlts = new AltsGUI(PlayerEntityRepository.getAltsByPlayerEntity(targetEntity),targetEntity, player);
            guiAlts.createGui().open(player);

        });

        gui.setItem(2, 2, warns);
        gui.setItem(2, 4, mutes);
        gui.setItem(2, 5, bans);
        gui.setItem(2, 6, alts);
        gui.setItem(1, 5, playerHead);


        return gui;
    }
}
