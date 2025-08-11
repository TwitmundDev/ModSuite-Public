package fr.twitmund.modSuite.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import fr.twitmund.modSuite.db.entity.MuteEntity;
import fr.twitmund.modSuite.utils.DateFormatter;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.Date;
import java.util.List;

public class PaginatedGuiMute {


    private String title;
    private int row;
    private List<MuteEntity> mutes;


    public PaginatedGuiMute(String title, int row, List<MuteEntity> mutes) {
        this.title = title;
        this.row = row;
        this.mutes = mutes;
    }

    public PaginatedGui createGui() {
        if (this.mutes == null || this.mutes.isEmpty()) {
            PaginatedGui gui = Gui.paginated()
                    .title(Component.text(LangEnum.GUI_NO_MUTES.getValue()))
                    .rows(1)
                    .disableAllInteractions()
                    .create();
            for (int i = 0; i < 9; i++) {
                gui.setItem(i, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(LangEnum.GUI_NO_MUTES.getValue()).asGuiItem());
            }

            return gui;
        }
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.RESET +title))
                .rows(row)
                .disableAllInteractions()
                .create();
        for (MuteEntity mutes : mutes) {
            gui.addItem(
                    ItemBuilder.from(Material.BOOK)
                            .setName(mutes.getMuteReason())
                            .addLore(LangEnum.GUI_WARNED_BY.getValue().replace("%author%", mutes.getPlayerAuthor().getPlayer_Name()))
                            .addLore(LangEnum.GUI_DATE.getValue().replace("%date%", DateFormatter.formatDate(mutes.getMuteDate())))
                            .addLore(mutes.getRevokeDate() == 0 ? LangEnum.GUI_IS_MUTED.getValue() + ChatColor.GREEN + "✔" :
                                    (mutes.getRevokeDate() < new Date().getTime() ? LangEnum.GUI_IS_MUTED.getValue() + ChatColor.RED + "✗" :
                                            LangEnum.GUI_UNMUTE_DATE.getValue().replace("%date%", DateFormatter.formatDate(mutes.getRevokeDate()))))
                            .addLore(mutes.getRevokeDate() != 0 && mutes.getRevokeDate() < new Date().getTime() ? LangEnum.GUI_UNMUTE_DATE.getValue().replace("%date%",DateFormatter.formatDate(mutes.getRevokeDate())) : "")
                            .asGuiItem());
        }

        for (int i = 1; i <= 9; i++) {
            if (i != 3 && i != 7) {
                gui.setItem(6, i, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
            }

        }
        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(LangEnum.GUI_PREVIOUS_PAGE.getValue()).asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(LangEnum.GUI_NEXT_PAGE.getValue()).asGuiItem(event -> gui.next()));

        return gui;
    }

}
