package fr.twitmund.modSuite.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.WarnEntity;
import fr.twitmund.modSuite.utils.DateFormatter;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;

public class PaginatedGuiBan {

    private String title;
    private int row;
    private List<BanEntity> bans;


    public PaginatedGuiBan(String title, int row, List<BanEntity> warns) {
        this.title = title;
        this.row = row;
        this.bans = warns;
    }

    public PaginatedGui createGui() {
        if (bans == null  ||  bans.isEmpty()) {
            PaginatedGui gui = Gui.paginated()
                    .title(Component.text(LangEnum.GUI_NO_WARN.getValue()))
                    .rows(1)
                    .disableAllInteractions()
                    .create();
            for (int i = 0; i < 9; i++) {
                gui.setItem(i, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(LangEnum.GUI_NO_WARN.getValue()).asGuiItem());
            }
            return gui;
        }
        PaginatedGui gui = Gui.paginated()
                .title(Component.text(ChatColor.RESET +title))
                .rows(row)
                .disableAllInteractions()
                .create();
        for (BanEntity warn : bans) {
            gui.addItem(
                    ItemBuilder.from(Material.BOOK)
                            .setName(warn.getBanReason())
                            .addLore(LangEnum.GUI_WARNED_BY.getValue().replace("%author%", warn.getPlayerAuthor().getPlayer_Name()))
                            .addLore(LangEnum.GUI_DATE.getValue() + DateFormatter.formatDate(warn.getBanDate()))
                            .asGuiItem());
        }

        gui.setItem(6,1, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        gui.setItem(6,2, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        gui.setItem(6,4, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        gui.setItem(6,5, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        gui.setItem(6,6, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        gui.setItem(6,8, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());
        gui.setItem(6,9, ItemBuilder.from(Material.BLACK_STAINED_GLASS_PANE).setName(" ").asGuiItem());

        gui.setItem(6, 3, ItemBuilder.from(Material.PAPER).setName(LangEnum.GUI_PREVIOUS_PAGE.getValue()).asGuiItem(event -> gui.previous()));
        gui.setItem(6, 7, ItemBuilder.from(Material.PAPER).setName(LangEnum.GUI_NEXT_PAGE.getValue()).asGuiItem(event -> gui.next()));

        return gui;
    }
}
