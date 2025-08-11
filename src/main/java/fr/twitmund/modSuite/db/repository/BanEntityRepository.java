package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;

import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;

public class BanEntityRepository {
    private static Dao<BanEntity, Integer> banEntityDao = Main.getBanEntityDao();

    /**
     * Create ban entity
     * @param banEntity BanEntity
     * @return an Optional containing a String
     */
    public static Optional<String> createBanEntity(BanEntity banEntity) {
        try {
            banEntityDao.create(banEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create ban entity: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Create ban entity
     * @param reason String
     * @param author PlayerEntity
     * @param target PlayerEntity
     * @return an Optional containing a Pair of BanEntity and String
     */
    public static Optional<Pair<BanEntity,String>> createBanEntity(String reason, PlayerEntity author, PlayerEntity target) {
        BanEntity banEntity = new BanEntity();
        banEntity.setBanReason(reason);
        banEntity.setBanDate(System.currentTimeMillis());
        banEntity.setPlayerAuthor(author);
        banEntity.setPlayerBanned(target);
        if (isPlayerBanned(target).getValue()) {
            return Optional.of(new Pair<>(null, LangEnum.BAN_PLAYER_ALREADY_BANNED.getValue()));
        }else {
            createBanEntity(banEntity);
            return Optional.of(new Pair<>(banEntity, LangEnum.BAN_SUCCESS.getValue()));
        }
    }

    /**
     * Create ban entity
     * @param reason String
     * @param target PlayerEntity
     * @return an Optional containing a Pair of BanEntity and String
     */
    public static Optional<Pair<BanEntity,String>> createBanEntity(String reason, PlayerEntity target) {
        BanEntity banEntity = new BanEntity();
        banEntity.setBanReason(reason);
        banEntity.setBanDate(System.currentTimeMillis());
        banEntity.setPlayerAuthor(PlayerEntityRepository.getPlayerByName("Console")); // Default author is Console
        banEntity.setPlayerBanned(target);
        if (isPlayerBanned(target).getValue()) {
            return Optional.of(new Pair<>(null, LangEnum.BAN_PLAYER_ALREADY_BANNED.getValue()));
        }else {
            createBanEntity(banEntity);
            return Optional.of(new Pair<>(banEntity, LangEnum.BAN_SUCCESS.getValue()));
        }
    }

    /**
     * Get ban entity by query
     * @param query a Map of String and Object
     * @return a List of BanEntity
     */
    public static List<BanEntity> getBanEntityBy(Map<String, Object> query) {
        try {
            return banEntityDao.queryForFieldValues(query);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get ban entity: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get ban entity by player
     * @param player PlayerEntity
     * @return a List of BanEntity
     */
    public static List<BanEntity> getBanEntitiesByPlayer(PlayerEntity player) {
        Map<String, Object> query = new HashMap<>(){{
            put("playerBanned_id", player.getId());
        }};
        List<BanEntity> banEntities = getBanEntityBy(query);
        return banEntities == null || banEntities.isEmpty() ? null : banEntities;
    }

    /**
     * Check if player is banned
     * @param player PlayerEntity
     * @return a Pair of BanEntity and Boolean
     */
    public static Pair<BanEntity, Boolean> isPlayerBanned(PlayerEntity player) {
        Pair<BanEntity, Boolean> isBanned = null;
        List<BanEntity> banEntities = getBanEntitiesByPlayer(player);
        if (banEntities == null) {
            isBanned = new Pair<>(null, false);
            return isBanned;
        }
        for (BanEntity banEntity : banEntities) {
            if (banEntity.isRevoked()) {
                isBanned = new Pair<>(null, false);
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
     * Get current ban entity
     * @param player PlayerEntity
     * @return BanEntity
     */
    public static BanEntity getCurrentBanEntity(PlayerEntity player) {
        Pair<BanEntity, Boolean> banInfo = isPlayerBanned(player);
        if (!banInfo.getValue()) {
            return null;
        }
        return banInfo.getKey();
    }

    /**
     * Update ban entity
     * @param banEntity BanEntity
     * @return BanEntity
     */
    public static BanEntity update(BanEntity banEntity) {
        try {
            banEntityDao.update(banEntity);
            return banEntity;
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
        Pair<BanEntity, Boolean> banInfo = isPlayerBanned(target);
        if (!banInfo.getValue()) {
            return new Pair<>(false, LangEnum.UNBAN_PLAYER_NOT_BANNED.getValue());
        }
        BanEntity banEntity = getCurrentBanEntity(target);
        banEntity.setRevoked(true);
        banEntity.setRevokeDate(System.currentTimeMillis());
        banEntity.setRevokeAuthor(author);
        banEntity.setRevokeReason(reason);
        update(banEntity);
        return new Pair<>(true, LangEnum.UNBAN_SUCCESS.getValue());
    }

    /**
     * Get all current bans
     * @return a List of BanEntity
     */
    public static List<BanEntity> getAllCurrentBans() {
        List<BanEntity> banEntities = new ArrayList<>();
        try {
            for (BanEntity banEntity : banEntityDao.queryForAll()) {
                if (!banEntity.isRevoked()){
                    banEntities.add(banEntity);
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
        for (BanEntity banEntity : getAllCurrentBans()) {
            bannedPlayers.add(banEntity.getPlayerBanned().getPlayer_Name());
        }
        return bannedPlayers;
    }
}
