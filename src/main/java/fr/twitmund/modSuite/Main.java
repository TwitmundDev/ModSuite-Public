package fr.twitmund.modSuite;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.j256.ormlite.dao.Dao;
import fr.twitmund.modSuite.db.DbConnection;
import fr.twitmund.modSuite.db.entity.*;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.manager.CommandManager;
import fr.twitmund.modSuite.manager.EventManager;
import fr.twitmund.modSuite.manager.PlayerManager;
import fr.twitmund.modSuite.utils.filesManager.DbConnectionManager;
import fr.twitmund.modSuite.utils.filesManager.LangManager;
import fr.twitmund.modSuite.utils.filesManager.PermissionFileManager;
import fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public final class Main extends JavaPlugin {

    @Setter
    @Getter
    private String dbUsername;
    @Setter
    @Getter
    private String dbPassword;
    @Setter
    @Getter
    private String dbHost;
    @Setter
    @Getter
    private String dbPort;
    @Setter
    @Getter
    private String dbName;

    private static final Logger log = Logger.getLogger("[ModSuite]");
    @Getter
    private static Main instance;
    @Getter
    private static Dao<PlayerEntity, Integer> playerEntityDao;
    @Getter
    private static Dao<WarnEntity, Integer> warnEntityDao;
    @Getter
    private static Dao<BanEntity, Integer> banEntityDao;

    @Getter
    private static Dao<BlackListEntity, Integer> blackListEntityDao;
    @Getter
    private static Dao<MuteEntity, Integer> muteEntityDao;
    @Getter
    private static Dao<BanIpEntity, Integer> banIpDao;
    @Getter
    private static Dao<TempBanEntity, Integer> tempbanDao;

    @Setter
    @Getter
    private static boolean maintenanceStatus;

    @Getter
    private static String discordWebHookUrl;

    @Getter
    @Setter
    private static Map<Player, ItemStack[]> StaffInventory;

    @Getter
    @Setter
    private static Map<Player, Boolean> StaffInventoryMode;


    @Getter
    @Setter
    public ArrayList<UUID> Moderators = new ArrayList<>();

    @Getter
    @Setter
    public HashMap<UUID, PlayerManager> players = new HashMap<>();

    @Getter
    @Setter
    public Map<UUID, Location> freeze = new HashMap<>();

    @Getter
    @Setter
    public static boolean freezeSoundEbabled;

    @Getter
    @Setter
    public static boolean discordWebhookEnabled;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        DbConnectionManager.loadMessagesConfig();
        LangManager.loadMessagesConfig();
        PermissionFileManager.loadMessagesConfig();

        maintenanceStatus = getConfig().getBoolean("maintenance_status");
        freezeSoundEbabled = getConfig().getBoolean("enable_freeze_sound");
        discordWebhookEnabled = getConfig().getBoolean("discord_webhook_enabled");
        discordWebHookUrl = getConfig().getString("discord_webhook_url");

        dbUsername = DbConnectionEnum.DB_USER.getValue();
        dbPassword = DbConnectionEnum.DB_PASSWORD.getValue();
        dbHost = DbConnectionEnum.DB_HOST.getValue();
        dbPort = DbConnectionEnum.DB_PORT.getValue();
        dbName = DbConnectionEnum.DB_NAME.getValue();
        // Plugin startup logic

        try {
            new DbConnection();
            playerEntityDao = DbConnection.getManager(PlayerEntity.class);
            warnEntityDao = DbConnection.getManager(WarnEntity.class);
            banEntityDao = DbConnection.getManager(BanEntity.class);
            muteEntityDao = DbConnection.getManager(MuteEntity.class);
            banIpDao = DbConnection.getManager(BanIpEntity.class);
            tempbanDao = DbConnection.getManager(TempBanEntity.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        EventManager.register();

        CommandManager.registerCommands();
        if (PlayerEntityRepository.getPlayerByName("Console") == null) {
            // Create console player entity
            PlayerEntityRepository.createPlayerEntity("0.0.0.0", "Console");
        }




    }

    @Override
    public void onDisable() {
        //set maintenance status to what it was b4 shutdown
        try {
            getConfig().set("maintenance_status", maintenanceStatus);
            saveConfig();
            log.info("Maintenance status saved");
        } catch (Exception e) {
            log.info("Could not save maintenance status");
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerManager playerManager = players.get(player.getUniqueId());
            if (playerManager != null) {
                playerManager.setVanished(false);
                playerManager.giveInventory();
            }
        }

        // Plugin shutdown logic
    }

    public boolean isPlayerFreezed(Player player) {
        return freeze.containsKey(player.getUniqueId());
    }

    public Map<UUID, Location> getFreezedplayers() {
        return freeze;
    }

    public boolean isFreezed(Player player) {
        return freeze.containsKey(player.getUniqueId());
    }

    public void reloadDAOs() {
        try {
            playerEntityDao = DbConnection.getManager(PlayerEntity.class);
            warnEntityDao = DbConnection.getManager(WarnEntity.class);
            banEntityDao = DbConnection.getManager(BanEntity.class);
            muteEntityDao = DbConnection.getManager(MuteEntity.class);
            banIpDao = DbConnection.getManager(BanIpEntity.class);
            tempbanDao = DbConnection.getManager(TempBanEntity.class);
        } catch (Exception e) {
            getLogger().severe("Erreur lors du reload des DAOs : " + e.getMessage());
        }
    }
}
