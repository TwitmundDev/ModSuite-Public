package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.BanIpEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BanIpRepository {

    private static Dao<BanIpEntity, Integer> banIpEntityDao = Main.getBanIpDao();


    /**
     * Create ban Ip entity
     * @param banIpEntity BanEntity
     * @return an Optional containing a String
     */
    public static Optional<String> createBanIpEntity(BanIpEntity banIpEntity) {
        try {
            banIpEntityDao.create(banIpEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create ban entity: " + e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Get banIp entity by query
     * @param query a Map of String and Object
     * @return a List of BanEntity
     */
    public static List<BanIpEntity> getBanIpEntityBy(Map<String, Object> query) {
        try {
            return banIpEntityDao.queryForFieldValues(query);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get ban entity: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if IP address is banned
     * @param ipAddress String
     * @return boolean
     */
    public static boolean isIpBanned(String ipAddress) {
        Map<String, Object> query = new HashMap<>() {{
            put("ipAddress", ipAddress);
        }};
        List<BanIpEntity> banIpEntities = getBanIpEntityBy(query);
        return banIpEntities != null && !banIpEntities.isEmpty();
    }

}
