package fr.twitmund.modSuite.listener;

import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.MuteEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEventListener implements Listener {

    @EventHandler
    public void onPlayerChatWhileMuted(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntityRepository.getPlayerByName(player.getName());
        if (MuteEntityRepository.isPlayerMutedBool(playerEntity)) {
            event.setCancelled(true);
            player.sendMessage(LangEnum.MUTE_PLAYER_TRY_CHAT.getValue());
        }
    }

}
