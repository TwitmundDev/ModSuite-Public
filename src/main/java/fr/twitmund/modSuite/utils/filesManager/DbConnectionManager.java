package fr.twitmund.modSuite.utils.filesManager;

import fr.twitmund.modSuite.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class DbConnectionManager {
    private static String fileName = "dbCredentials.yml";
    private static File messagesFile;

    @Getter
    private static FileConfiguration messagesConfig;

    public static void loadMessagesConfig() {
        messagesFile = new File(Main.getInstance().getDataFolder(), fileName);

        if (!messagesFile.exists()) {
            createDefaultMessagesFile();
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    private static void createDefaultMessagesFile() {
        try {
            messagesFile.getParentFile().mkdirs();
            Main.getInstance().saveResource(fileName, false);
        } catch (Exception e) {
            //System.err.println("Could not create default messages file: " + e.getMessage());
            Main.getInstance().getLogger().log(Level.SEVERE, "Could not create default messages file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
