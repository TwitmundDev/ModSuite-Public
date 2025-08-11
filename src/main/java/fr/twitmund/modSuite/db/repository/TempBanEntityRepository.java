package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.entity.TempBanEntity;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class TempBanEntityRepository {

    private static Dao<TempBanEntity, Integer> tempBanEntityDao = Main.getTempbanDao();

    public static void createTempBanEntity(TempBanEntity tempBanEntity) {
        try {
            tempBanEntityDao.create(tempBanEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create temp ban entity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Optional<Pair<TempBanEntity, String>> createTempBanEntity(String reason, PlayerEntity author, PlayerEntity target, long duration) {
        TempBanEntity tempBanEntity = new TempBanEntity();
        tempBanEntity.setBanReason(reason);
        tempBanEntity.setBanDate(System.currentTimeMillis());
        tempBanEntity.setPlayerAuthor(author);
        tempBanEntity.setPlayerBanned(target);
        tempBanEntity.setRevokeDate(duration);
        if (isPlayerBanned(target).getValue()) {
            return Optional.of(new Pair<>(null, LangEnum.BAN_PLAYER_ALREADY_BANNED.getValue()));
        } else {
            createTempBanEntity(tempBanEntity);
            return Optional.of(new Pair<>(tempBanEntity, LangEnum.TEMPBAN_SUCCESS.getValue()));
        }
    }

    /**
     * Get ban entity by query
     * @param query a Map of String and Object
     * @return a List of TempBanEntity
     */
    public static List<TempBanEntity> getBanEntityBy(Map<String, Object> query) {
        try {
            return tempBanEntityDao.queryForFieldValues(query);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get ban entity: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }



    /**
     * Get ban entity by player
     * @param player PlayerEntity
     * @return a List of TempBanEntity
     */
    public static List<TempBanEntity> getBanEntitiesByPlayer(PlayerEntity player) {
        Map<String, Object> query = new HashMap<>(){{
            put("playerBanned_id", player.getId());
        }};
        List<TempBanEntity> banEntities = getBanEntityBy(query);
        return banEntities == null || banEntities.isEmpty() ? null : banEntities;
    }

    /**
     * Check if player is banned
     * @param player PlayerEntity
     * @return a Pair of TempBanEntity and Boolean
     */
    public static Pair<TempBanEntity, Boolean> isPlayerBanned(PlayerEntity player) {
        Pair<TempBanEntity, Boolean> isBanned = null;
        List<TempBanEntity> banEntities = getBanEntitiesByPlayer(player);
        if (banEntities == null) {
            isBanned = new Pair<>(null, false);
            return isBanned;
        }
        for (TempBanEntity banEntity : banEntities) {
            if (banEntity.isRevoked()) {
                isBanned = new Pair<>(null, false);
            }
            else if (banEntity.getRevokeDate() < System.currentTimeMillis()) {
                isBanned = new Pair<>(banEntity, true);
            }
            else {
                isBanned = new Pair<>(banEntity, true);
            }
        }
        return isBanned;
    }

    /**
     * Check if player is banned
     * @param player PlayerEntity
     * @return boolean
     */
    public static boolean isPlayerBannedBool(PlayerEntity player){
        return isPlayerBanned(player).getValue();
    }

    /**
     * Get current tempban entity
     * @param player PlayerEntity
     * @return BanEntity
     */
    public static TempBanEntity getCurrentBanEntity(PlayerEntity player) {
        Pair<TempBanEntity, Boolean> banInfo = isPlayerBanned(player);
        if (!banInfo.getValue()) {
            return null;
        }
        return banInfo.getKey();
    }

    /**
     * Update ban entity
     * @param tempBanEntity BanEntity
     * @return BanEntity
     */
    public static TempBanEntity update(TempBanEntity tempBanEntity) {
        try {
            tempBanEntityDao.update(tempBanEntity);
            return tempBanEntity;
        } catch (SQLException e) {
            Main.getInstance().getLogger().log(Level.FINE,"problème au moment de l'update: " + e);
            return null;
        }
    }

    /**
     * Unban player
     * @param target PlayerEntity
     * @param author PlayerEntity
     * @param reason String
     * @return a Pair of Boolean and String
     */
    public static Pair<Boolean, String> unbanPlayer(PlayerEntity target, PlayerEntity author,String reason) {
        Pair<TempBanEntity, Boolean> banInfo = isPlayerBanned(target);
        if (!banInfo.getValue()) {
            return new Pair<>(false, LangEnum.UNBAN_PLAYER_NOT_BANNED.getValue());
        }
        TempBanEntity tempBanEntity = getCurrentBanEntity(target);
        tempBanEntity.setRevoked(true);
        tempBanEntity.setRevokeDate(System.currentTimeMillis());
        tempBanEntity.setRevokeAuthor(author);
        tempBanEntity.setRevokeReason(reason);
        update(tempBanEntity);
        return new Pair<>(true, LangEnum.UNBAN_SUCCESS.getValue());
    }

    /**
     * Get all current bans
     * @return a List of BanEntity
     */
    public static List<TempBanEntity> getAllCurrentBans() {
        List<TempBanEntity> banEntities = new ArrayList<>();
        try {
            for (TempBanEntity tempBanEntity : tempBanEntityDao.queryForAll()) {
                if (!tempBanEntity.isRevoked()){
                    banEntities.add(tempBanEntity);
                }
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get all current bans: " + e.getMessage());
            e.printStackTrace();
        }
        return banEntities;
    }


    /**
     * Get all current banned players
     * @return a List of String
     */
    public static List<String> getAllCurrentBannedPlayersUsername(){
        List<String> bannedPlayers = new ArrayList<>();
        for (TempBanEntity tempBanEntity : getAllCurrentBans()) {
            bannedPlayers.add(tempBanEntity.getPlayerBanned().getPlayer_Name());
        }
        return bannedPlayers;
    }


}
