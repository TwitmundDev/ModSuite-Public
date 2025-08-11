package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.PlayerEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlayerEntityRepository {
    private static Dao<PlayerEntity, Integer> playerEntityDao = Main.getPlayerEntityDao();

    /**
     * Initialize the player entity repository
     * @param playerEntity PlayerEntity
     */
    public static void createPlayerEntity(PlayerEntity playerEntity) {
        //TODO
        try {
            playerEntityDao.create(playerEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create player entity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get all players
     * @return a List of PlayerEntity
     */
    public static List<PlayerEntity> getAllPlayers() {
        try {
            return playerEntityDao.queryForAll();
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get all players: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static boolean updatePlayerEntity(PlayerEntity playerEntity) {
        try {
            playerEntityDao.update(playerEntity);
            return true;
        } catch (SQLException e) {
            Main.getInstance().getLogger().severe("Could not update player entity: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all players names
     * @return a List of String
     */
    public static List<String> getAllPlayersNames() {
        try {
            List<PlayerEntity> players = playerEntityDao.queryForAll();
            List<String> playerNames = new ArrayList<>();
            for (PlayerEntity player : players) {
                if (player.getPlayer_Name() == "Console") continue; // Skip console player
                playerNames.add(player.getPlayer_Name());
            }
            return playerNames;
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get all player names: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Create a player entity with the given Name and IP
     * @param playerName String
     * @param playerIP String
     */
    public static void createPlayerEntity(String playerIP, String playerName) {
        if (!doPlayerExistInDb(playerName)) {
            PlayerEntity playerEntity = new PlayerEntity();
            playerEntity.setPlayer_IP(playerIP);
            playerEntity.setPlayer_Name(playerName);
            createPlayerEntity(playerEntity);
        }
    }


    /**
     * Get player entity by Name
     * @param playerName String
     * @return PlayerEntity
     */
    public static PlayerEntity getPlayerByName(String playerName) {
        try {
            List<PlayerEntity> players = playerEntityDao.queryForEq("player_Name", playerName);
            if (players.isEmpty()) {
                return null;
            }
            return players.get(0);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get player entity by name: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if player exist in db
     * @param playerName String
     * @return boolean
     */
    public static boolean doPlayerExistInDb(String playerName) {
        try {
            if (playerName.equals("Console")) return false;
            return !playerEntityDao.queryForEq("player_Name", playerName).isEmpty();
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not check if player exist in db: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkAltsForPlayerIP(String playerIP) {
        try {
            List<PlayerEntity> players = playerEntityDao.queryForEq("player_IP", playerIP);
            if (players.isEmpty()) {
                return false;
            }
            // On peut ajouter un contrôle pour ignorer "Console" si besoin
            for (PlayerEntity player : players) {
                if (!"Console".equals(player.getPlayer_Name())) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            Main.getInstance().getLogger().severe("Could not check for player IP: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAltsByIp(String ipAddress) {
        List<String> alts = new ArrayList<>();
        try {
            List<PlayerEntity> players = playerEntityDao.queryForEq("player_IP", ipAddress);
            for (PlayerEntity player : players) {
                if (!player.getPlayer_Name().equals("Console")) { // Skip console player
                    alts.add(player.getPlayer_Name());
                }
            }
        } catch (SQLException e) {
            Main.getInstance().getLogger().severe("Could not get alts by IP: " + e.getMessage());
            e.printStackTrace();
        }
        return alts;
    }
    /**
     * Get alts by username
     * @param username String
     * @return List of alts
     */
    public static List<String> getAltsByUsername(String username){
        List<String> alts = new ArrayList<>();
        try {
            PlayerEntity playerEntity = getPlayerByName(username);
            if (playerEntity != null) {
                String playerIP = playerEntity.getPlayer_IP();
                alts = getAltsByIp(playerIP);
            }
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get alts by username: " + e.getMessage());
            e.printStackTrace();
        }
        return alts;
    }

    /**
     * Get alts by PlayerEntity
     * @param playerEntity PlayerEntity
     * @return List of alts
     */
    public static List<String> getAltsByPlayerEntity(PlayerEntity playerEntity) {
        List<String> alts = new ArrayList<>();
        try {
            String playerIP = playerEntity.getPlayer_IP();
            alts = getAltsByIp(playerIP);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get alts by player entity: " + e.getMessage());
            e.printStackTrace();
        }
        return alts;
    }
}
