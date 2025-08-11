package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanEntity;
import fr.twitmund.modSuite.db.entity.BanIpEntity;
import fr.twitmund.modSuite.db.entity.BlackListEntity;

import java.util.List;
import java.util.Map;

public class BlackListEntityRepository {

    private static Dao<BlackListEntity, Integer> blackListEntityDao = Main.getBlackListEntityDao();

    /**
     * Create a BlackListEntity
     * @param blackListEntity BlackListEntity
     */
    public static void createBlackListEntity(BlackListEntity blackListEntity) {
        try {
            blackListEntityDao.create(blackListEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create black list entity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get BlackListEntity by query
     * @param query a Map of String and Object
     * @return a List of BlackListEntity
     */
    public static List<BlackListEntity> getBlackListEntityBy(Map<String, Object> query) {
        try {
            return blackListEntityDao.queryForFieldValues(query);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get ban entity: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a player is blacklisted
     * @param playerName String
     * @return boolean
     */
    public static boolean isPlayerBlackListed(String playerName) {
        Map<String, Object> query = Map.of("playerName", playerName);
        List<BlackListEntity> blackListEntities = getBlackListEntityBy(query);
        return blackListEntities != null && !blackListEntities.isEmpty();
    }

    /**
     * Get the reason for blacklisting a player
     * @param playerName String
     * @return String
     */
    public static String getBlackListReason(String playerName) {
        Map<String, Object> query = Map.of("playerName", playerName);
        List<BlackListEntity> blackListEntities = getBlackListEntityBy(query);
        if (blackListEntities != null && !blackListEntities.isEmpty()) {
            return blackListEntities.get(0).getReason();
        }
        return null;

    }

    /**
     * Remove a player from the blacklist
     * @param playerName String
     */
    public static void removePlayerFromBlackList(String playerName) {
        Map<String, Object> query = Map.of("playerName", playerName);
        List<BlackListEntity> blackListEntities = getBlackListEntityBy(query);
        if (blackListEntities != null && !blackListEntities.isEmpty()) {
            try {
                blackListEntityDao.delete(blackListEntities.get(0));
            } catch (Exception e) {
                Main.getInstance().getLogger().severe("Could not remove player from blacklist: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * Get all blacklisted players
     * @return List of BlackListEntity
     */
    public static List<BlackListEntity> getAllBlackListedPlayers() {
        try {
            return blackListEntityDao.queryForAll();
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get all blacklisted players: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get all blacklisted players names
     * @return List of String
     */
    public static List<String> getAllBlackListedPlayersNames() {
        List<BlackListEntity> blackListEntities = getAllBlackListedPlayers();
        if (blackListEntities != null) {
            return blackListEntities.stream()
                    .map(BlackListEntity::getPlayerName)
                    .toList();
        }
        return List.of();
    }

    /**
     * Get all blacklisted players reasons
     * @return List of String
     */
    public static List<String> getAllBlackListedPlayersReasons() {
        List<BlackListEntity> blackListEntities = getAllBlackListedPlayers();
        if (blackListEntities != null) {
            return blackListEntities.stream()
                    .map(BlackListEntity::getReason)
                    .toList();
        }
        return List.of();
    }






}
