package fr.twitmund.modSuite.db.repository;

import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.entity.WarnEntity;

import java.util.List;

public class WarnEntityRepository {

    private static Dao<WarnEntity, Integer> warnEntityDao = Main.getWarnEntityDao();


    /**
     * Create warn entity
     * @param warnEntity WarnEntity
     */
    public static void createWarnEntity(WarnEntity warnEntity) {
        try {
            warnEntityDao.create(warnEntity);
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not create warn entity: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Create warn entity
     * @param warnReason String
     * @param playerAuthor PlayerEntity
     * @param playerWarned PlayerEntity
     */
    public static void createWarnEntity(String warnReason, PlayerEntity playerAuthor, PlayerEntity playerWarned) {
        WarnEntity warnEntity = new WarnEntity();
        warnEntity.setWarnReason(warnReason);
        warnEntity.setWarnDate(System.currentTimeMillis());
        warnEntity.setPlayerAuthor(playerAuthor);
        warnEntity.setPlayerWarned(playerWarned);
        createWarnEntity(warnEntity);
    }

    public static List<WarnEntity> getWarnEntityByPlayerName(PlayerEntity playerEntity) {

        try {
            return warnEntityDao.queryForEq("playerWarned_id", playerEntity.getId());
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Could not get warn entity by query: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
