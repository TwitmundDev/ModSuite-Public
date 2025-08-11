package fr.twitmund.modSuite.listener;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.entity.TempBanEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.BlackListEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.db.repository.TempBanEntityRepository;
import fr.twitmund.modSuite.manager.staff.VanishManager;
import fr.twitmund.modSuite.utils.ChatUtil;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.TimeUtil;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    //TODO add wehbhook for ban evading
    //TODO add notify for ban evading with ip address
    @EventHandler
    public void createPlayerEntityInDb(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //System.out.println(player.getUniqueId());
        System.out.println("A");
        if (!(PlayerEntityRepository.doPlayerExistInDb(player.getName()))) {
            try {
                PlayerEntityRepository.createPlayerEntity(player.getAddress().getAddress().getHostAddress(), player.getName());

            } catch (Exception e) {
                Main.getInstance().getLogger().log(Level.SEVERE, "Error while creating player entity: " + e);
            }
        }
    }


    @EventHandler
    public void playerJoinOnBan(PlayerJoinEvent event) {
        System.out.println("B");
        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntityRepository.getPlayerByName(player.getName());
        Pair<BanEntity, Boolean> banInfo = BanEntityRepository.isPlayerBanned(playerEntity);
        if (!banInfo.getValue() || banInfo.getKey() == null) {
            return;
        }
        boolean isBanned = banInfo.getValue();
        String banReason = banInfo.getKey().getBanReason();
        System.out.println(banInfo.getKey().getPlayerAuthor().getPlayer_Name());
        String banAuthor = banInfo.getKey().getPlayerAuthor().getPlayer_Name();
        int banId = banInfo.getKey().getBan_id();

        if (isBanned) {
            player.kickPlayer(LangEnum.BAN_MESSAGE.getValue()
                    .replace("%reason%", banReason)
                    .replace("%author%", banAuthor)
                    .replace("%ban_id%", String.valueOf(banId)));
            ChatUtil.chatNotify(LangEnum.BAN_TRIED_JOIN.getValue().replace("%player%", player.getName()), PermissionEnum.PERMISSION_NOTIFY);
        }
    }

    @EventHandler
    public void playerJoinOnTempBan(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntityRepository.getPlayerByName(player.getName());
        Pair<TempBanEntity, Boolean> banInfo = TempBanEntityRepository.isPlayerBanned(playerEntity);
        if (!banInfo.getValue() || banInfo.getKey() == null) {
            return;
        }
        boolean isBanned = banInfo.getValue();
        String banReason = banInfo.getKey().getBanReason();
        System.out.println(banInfo.getKey().getPlayerAuthor().getPlayer_Name());
        String banAuthor = banInfo.getKey().getPlayerAuthor().getPlayer_Name();
        // todo fix that String durationRemaining = TimeUtil.formatTime(banInfo.getKey().getRevokeDate() - System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String durationRemaining = sdf.format(new Date(banInfo.getKey().getRevokeDate()));
        int banId = banInfo.getKey().getTempBan_id();

        if (isBanned) {
            player.kickPlayer(LangEnum.TEMPBAN_MESSAGE.getValue()
                    .replace("%reason%", banReason)
                    .replace("%author%", banAuthor)
                    .replace("%ban_id%", String.valueOf(banId))
                    .replace("%duration%", durationRemaining));
            ChatUtil.chatNotify(LangEnum.BAN_TRIED_JOIN.getValue().replace("%player%", player.getName()), PermissionEnum.PERMISSION_NOTIFY);
        }
    }

    @EventHandler
    public void playerLeaveOnBan(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerEntity playerEntity = PlayerEntityRepository.getPlayerByName(player.getName());
        Pair<BanEntity, Boolean> banInfo = BanEntityRepository.isPlayerBanned(playerEntity);
        if (!banInfo.getValue() || banInfo.getKey() == null) {
            return;
        }
        boolean isBanned = banInfo.getValue();

        if (isBanned) {
            event.setQuitMessage("");
        }
    }

    //TODO add wehbhook for maintenance
    @EventHandler
    public void playerJoinOnMaintenance(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Main.isMaintenanceStatus()) {
            if (!player.hasPermission(PermissionEnum.PERMISSION_MAINTENANCE_BYPASS.getValue())) {
                event.setJoinMessage(null);
                player.kickPlayer(LangEnum.MAINTENANCE_KICK_MESSAGE.getValue());
            }
        }
    }

    @EventHandler
    public void playerLeaveOnMaintenance(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (Main.isMaintenanceStatus()) {
            if (!player.hasPermission(PermissionEnum.PERMISSION_MAINTENANCE_BYPASS.getValue())) {
                event.setQuitMessage(null);
            }
        }
    }

    @EventHandler
    public void playerJoinHideVanished(AsyncPlayerPreLoginEvent event) {
        Player player = Bukkit.getPlayer(event.getUniqueId());
        Map<Player, Boolean> vanishedPlayers = VanishManager.getVanishedPlayers();
        for (Map.Entry<Player, Boolean> entry : vanishedPlayers.entrySet()) {
            if (entry.getValue() && entry.getKey() != null && player != null) {
                player.hidePlayer(Main.getInstance(), entry.getKey());
            }
        }
    }

    @EventHandler
    public void onPlayerConnectWithNewIp(AsyncPlayerPreLoginEvent event) {
        PlayerEntity playerEntity = PlayerEntityRepository.getPlayerByName(event.getName());
        String currentIp = event.getAddress().getHostAddress();
        if (playerEntity == null) {
            // Le joueur n'existe pas, on le crée avec l'IP actuelle
            PlayerEntityRepository.createPlayerEntity(currentIp, event.getName());
            return;
        }
        // Vérifie si l'IP a changé
        if (!playerEntity.getPlayer_IP().equals(currentIp)) {
            playerEntity.setPlayer_IP(currentIp);
            if (!PlayerEntityRepository.updatePlayerEntity(playerEntity)){
             Main.getInstance().getLogger().log(Level.SEVERE, "Error while updating player entity IP:" + playerEntity.getPlayer_Name());
            }
        }
        // Vérifie si le joueur est banni
        Pair<BanEntity, Boolean> banInfo = BanEntityRepository.isPlayerBanned(playerEntity);
    }

//    @EventHandler
//    public void playerTryJoinBlackList(AsyncPlayerPreLoginEvent event) {
//        if(BlackListEntityRepository.isPlayerBlackListed(event.getName())){
//            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, LangEnum.BAN_MESSAGE.getValue());
//            ChatUtil.notifyStaff(LangEnum.BLACKLIST_PLAYER_TRY_TO_CONNECT.getValue()
//                    .replace("%player%", event.getName())
//                    .replace("%ip%", event.getAddress().getHostAddress()));
//        }
//    }


}
