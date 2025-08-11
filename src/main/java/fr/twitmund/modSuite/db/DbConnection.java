package fr.twitmund.modSuite.db;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.*;

import java.sql.SQLException;
import java.util.logging.Level;

public class DbConnection {

    private static ConnectionSource connectionSource;

    public DbConnection() throws SQLException {
        String dbUsername = Main.getInstance().getDbUsername();
        String dbPassword = Main.getInstance().getDbPassword();
        String dbHost = Main.getInstance().getDbHost();
        String dbPort = Main.getInstance().getDbPort();
        String dbName = Main.getInstance().getDbName();

        String dbUrl = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?user=" + dbUsername + "&password=" + dbPassword + "&allowPublicKeyRetrieval=true";
        try {
            Main.getInstance().getLogger().log(Level.INFO, "Connecting to database: " + dbUrl);
            JdbcPooledConnectionSource connection = new JdbcPooledConnectionSource(dbUrl);
            connection.setTestBeforeGet(true);
            connection.setCheckConnectionsEveryMillis(30000L);
            connectionSource = connection;

            Main.getInstance().getLogger().log(Level.INFO, "Creating tables if not exist");
            TableUtils.createTableIfNotExists(connectionSource, PlayerEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, WarnEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, BanEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, TempBanEntity.class);
            TableUtils.createTableIfNotExists(connectionSource, MuteEntity.class);
            Main.getInstance().getLogger().log(Level.INFO, "Tables created successfully");
        } catch (SQLException e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Error creating tables: ", e);
            throw e;
        } catch (Exception e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Error connecting to the database: ", e);
        }
    }

    public static <T> Dao<T, Integer> getManager(Class<T> dataClass) {
        if (connectionSource == null) {
            throw new IllegalStateException("ConnectionSource is not initialized");
        }
        try {
            return DaoManager.createDao(connectionSource, dataClass);
        } catch (SQLException e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Error creating DAO: ", e);
            return null;
        }
    }
}