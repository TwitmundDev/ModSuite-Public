package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.MuteEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;

import java.util.*;

public class MuteEntityRepository {
    private static Dao<MuteEntity, Integer> muteEntityDao = Main.getMuteEntityDao();

    /**
     * Create mute entity
     *
     * @param muteEntity MuteEntity
     * @return an Optional containing a String
     */
    public static Optional<String> createMuteEntity(MuteEntity muteEntity) {
        try {
            muteEntityDao.create(muteEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create mute entity: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Create mute entity
     *
     * @param reason String
     * @param author PlayerEntity
     * @param target PlayerEntity
     * @return an Optional containing a Pair of MuteEntity and String
     */
    public static Optional<Pair<MuteEntity, String>> createMuteEntity(String reason, PlayerEntity author, PlayerEntity target) {
        MuteEntity muteEntity = new MuteEntity();
        muteEntity.setMuteReason(reason);
        muteEntity.setMuteDate(System.currentTimeMillis());
        muteEntity.setPlayerAuthor(author);
        muteEntity.setPlayerMuted(target);
        if (isPlayerMuted(target).getValue()) {
            return Optional.of(new Pair<>(null, LangEnum.MUTE_PLAYER_ALREADY_MUTED.getValue()));
        } else {
            createMuteEntity(muteEntity);
            return Optional.of(new Pair<>(muteEntity, LangEnum.MUTE_SUCCESS.getValue()));
        }
    }

    /**
     * Get mute entity by query
     *
     * @param query a Map of String and Object
     * @return a List of MuteEntity
     */
    public static List<MuteEntity> getMuteEntityBy(Map<String, Object> query) {
        try {
            return muteEntityDao.queryForFieldValues(query);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get mute entity: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get mute entity by player
     *
     * @param player PlayerEntity
     * @return a List of MuteEntity
     */
    public static List<MuteEntity> getMuteEntityByPlayer(PlayerEntity player) {
        Map<String, Object> query = new HashMap<>() {{
            put("playerMuted_id", player.getId());
        }};
        List<MuteEntity> muteEntities = getMuteEntityBy(query);
        return muteEntities == null || muteEntities.isEmpty() ? null : muteEntities;
    }

    /**
     * Check if player is muted
     * @param player PlayerEntity
     * @return a Pair of MuteEntity and Boolean
     */
    public static Pair<MuteEntity, Boolean> isPlayerMuted(PlayerEntity player) {
        Pair<MuteEntity, Boolean> isMuted = null;
        List<MuteEntity> muteEntities = getMuteEntityByPlayer(player);
        if (muteEntities == null) {
            isMuted = new Pair<>(null, false);
            return isMuted;
        }

        for (MuteEntity muteEntity : muteEntities) {
           if(muteEntity.isRevoked()){
                isMuted = new Pair<>(muteEntity, false);
           }
           else {
               isMuted = new Pair<>(muteEntity, true);
           }
        }
        return isMuted;
    }

    /**
     * Check if player is muted
     * @param player PlayerEntity
     * @return boolean
     */
    public static boolean isPlayerMutedBool(PlayerEntity player) {
        return isPlayerMuted(player).getValue();
    }

    /**
     * Get current mute entity
     * @param player PlayerEntity
     * @return MuteEntity or null
     */
    public static MuteEntity getCurrentMuteEntity(PlayerEntity player) {
        Pair<MuteEntity, Boolean> muteInfo = isPlayerMuted(player);
        if (!muteInfo.getValue()) {
            return null;
        }
        return muteInfo.getKey();
    }

    /**
     * Revoke mute entity
     * @param muteEntity MuteEntity
     * @param reason String
     * @param author PlayerEntity
     * @return an Optional of String
     */
    public static Optional<String> revokeMuteEntity(MuteEntity muteEntity, String reason, PlayerEntity author) {
        MuteEntity currentMuteEntity = muteEntity;
        currentMuteEntity.setRevoked(true);
        currentMuteEntity.setRevokeReason(reason);
        currentMuteEntity.setRevokeDate(System.currentTimeMillis());
        currentMuteEntity.setRevokeAuthor(author);
        try {
            muteEntityDao.update(muteEntity);
            return Optional.of(LangEnum.MUTE_SUCCESS.getValue());
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not revoke mute entity: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Get all current mutes
     * @return a List of MuteEntity
     */
    public static List<MuteEntity> getAllCurrentMutes() {
        List<MuteEntity> muteEntities = new ArrayList<>();
        try {
            for (MuteEntity muteEntity : muteEntityDao.queryForAll()) {
                if (!muteEntity.isRevoked()) {
                    muteEntities.add(muteEntity);
                }
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get all current mutes: " + e.getMessage());
            e.printStackTrace();
        }
        return muteEntities;
    }


    /**
     * Get all current muted players
     * @return a List of String
     */
    public static List<String> getAllCurrentMutedPlayersUsername() {
        List<String> mutedEntities = new ArrayList<>();
        for (MuteEntity muteEntity : getAllCurrentMutes()) {
            mutedEntities.add(muteEntity.getPlayerMuted().getPlayer_Name());
        }
        return mutedEntities;
    }


}
