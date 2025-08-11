package fr.twitmund.modSuite.utils.filesManager.enums;

import fr.twitmund.modSuite.utils.HexaColor;
import fr.twitmund.modSuite.utils.filesManager.LangManager;
import org.bukkit.configuration.file.FileConfiguration;

public enum WebhookMessage {

    SANCTION_TITLE("webhook_sanction_title"),
    SANCTION_DESCRIPTION("webhook_sanction_description"),

    WARN_TITLE("webhook_warn_title"),
    WARN_MESSAGE("webhook_warn_message"),

    BAN_TITLE("webhook_ban_title"),
    BAN_MESSAGE("webhook_ban_message"),

    TEMPBAN_TITLE("webhook_tempban_title"),
    TEMPBAN_MESSAGE("webhook_tempban_message"),

    UNBAN_TITLE("webhook_unban_title"),
    UNBAN_MESSAGE("webhook_unban_message"),

    MUTE_TITLE("webhook_mute_title"),
    MUTE_MESSAGE("webhook_mute_message"),

    TEMPMUTE_TITLE("webhook_tempmute_title"),
    TEMPMUTE_MESSAGE("webhook_tempmute_message"),

    UNMUTE_TITLE("webhook_unmute_title"),
    UNMUTE_MESSAGE("webhook_unmute_message"),

    MAINTENANCE_TITLE("webhook_maintenance_title"),
    MAINTENANCE_MESSAGE("webhook_maintenance_message");


    private String value;
    private final String key;

    WebhookMessage(String key) {
        this.key = key;
    }

    public String getValue() {
        if (value == null) {
            reloadValue();
        }

        return value;
    }

    public void reloadValue() {
        FileConfiguration config = LangManager.getMessagesConfig();
        String loadedValue = config.getString(key); // null if not found
        if (loadedValue != null) {
            value = HexaColor.format(loadedValue);
        } else {
            value = "Message not found for key: " + key;
        }
    }

    public static void reloadAllValues() {
        for (WebhookMessage messageEnum : values()) {
            messageEnum.reloadValue();
        }
    }
}
