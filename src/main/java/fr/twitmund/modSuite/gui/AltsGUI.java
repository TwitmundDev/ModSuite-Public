package fr.twitmund.modSuite.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.utils.DateFormatter;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AltsGUI {

    private final List<String> alts;
    private final PlayerEntity targetEntity;
    private final Player player;

    public AltsGUI(List<String> alts, PlayerEntity targetEntity, Player player) {
        this.alts = alts;
        this.targetEntity = targetEntity;
        this.player = player;
    }

    public @NotNull Gui createGui() {
        Gui gui = Gui.gui()
                .title(Component.text(LangEnum.GUI_ALTS_TITLE.getValue().replace("%player%", targetEntity.getPlayer_Name())))
                .rows(6)
                .create();

        if (alts.size() == 0 || alts.isEmpty() || alts == null) {
            gui.addItem(
                    ItemBuilder.from(Material.BARRIER)
                            .setName(LangEnum.GUI_NO_ALTS.getValue())
                            .asGuiItem());
            gui.addItem(
                    ItemBuilder.from(Material.BARRIER)
                            .name(Component.text(LangEnum.GUI_BACK.getValue()))
                            .asGuiItem(event -> {
                                gui.close(player);
                                LookupGUI.createGui(targetEntity, player).open(player);

                            }));
        }

        for (int i = 0; i < alts.size(); i++) {
            String alt = alts.get(i);
            gui.addItem(
                    ItemBuilder.from(Material.PLAYER_HEAD)
                            .setName(alt)
                            .asGuiItem());
        }

        return gui;
    }
}
