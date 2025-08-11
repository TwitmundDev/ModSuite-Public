package fr.twitmund.modSuite.listener;


import fr.twitmund.modSuite.manager.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnModQuit implements Listener {

    @EventHandler
    public void onModQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (PlayerManager.isInMod(player)) {
            PlayerManager pm = PlayerManager.getFromPlayer(player);
            player.getInventory().clear();
            if (!(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            pm.giveInventory();
            pm.destroy();
        }
    }

}