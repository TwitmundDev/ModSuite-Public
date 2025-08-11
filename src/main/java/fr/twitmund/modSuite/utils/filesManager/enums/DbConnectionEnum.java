package fr.twitmund.modSuite.utils.filesManager.enums;

import fr.twitmund.modSuite.utils.HexaColor;
import fr.twitmund.modSuite.utils.filesManager.DbConnectionManager;
import org.bukkit.configuration.file.FileConfiguration;

public enum DbConnectionEnum {
    DB_HOST("db_host"),
    DB_PORT("db_port"),
    DB_NAME("db_name"),
    DB_USER("db_user"),
    DB_PASSWORD("db_password");


    private String value;
    private final String key;

    DbConnectionEnum(String key) {
        this.key = key;
    }

    public String getValue() {
        if (value == null) {
            reloadValue();
        }
        return value;
    }

    public void reloadValue() {
        FileConfiguration config = DbConnectionManager.getMessagesConfig();
        String loadedValue = config.getString(key); // null if not found
        if (loadedValue != null) {
            value = HexaColor.format(loadedValue);
        } else {
            value = "Message not found for key: " + key;
        }
    }

    public static void reloadAllValues() {
        for (DbConnectionEnum messageEnum : values()) {
            messageEnum.reloadValue();
        }
    }
}
