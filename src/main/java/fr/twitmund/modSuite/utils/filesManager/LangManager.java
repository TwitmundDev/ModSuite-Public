package fr.twitmund.modSuite.utils.filesManager;

import fr.twitmund.modSuite.Main;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class LangManager {

    private static String fileName = "lang/"+Main.getInstance().getConfig().getString("lang")+".yml";
    private static File messagesFile;

    @Getter
    private static FileConfiguration messagesConfig;

    public static void loadMessagesConfig() {
        try {
            System.out.println("Loaded messages file" + fileName);
            messagesFile = new File(Main.getInstance().getDataFolder(), fileName);
        } catch (Exception e) {
            Main.getInstance().getLogger().log(Level.SEVERE, "Could not load messages file: " + e.getMessage());
            e.printStackTrace();
        }

        if (!messagesFile.exists()) {
            createDefaultMessagesFile();
        }

        System.out.println("Loaded messages file");
        System.out.println(messagesFile.getAbsolutePath());

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
        for (String s : messagesConfig.getKeys(false)) {
            System.out.println("Loaded valuse: " +s + " : " + messagesConfig.getString(s));
        }
    }

    private static void createDefaultMessagesFile() {
        try {
            System.out.println("Created default messages file");
            messagesFile.getParentFile().mkdirs();
            Main.getInstance().saveResource(fileName, false);
        } catch (Exception e) {
            //System.err.println("Could not create default messages file: " + e.getMessage());
            Main.getInstance().getLogger().log(Level.SEVERE, "Could not create default messages file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
