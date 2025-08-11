package fr.twitmund.modSuite.utils.discordWebhook;

import fr.twitmund.modSuite.utils.HexaColor;
import fr.twitmund.modSuite.utils.Pair;
import fr.twitmund.modSuite.utils.filesManager.LangManager;
import fr.twitmund.modSuite.utils.filesManager.enums.WebhookMessage;
import org.bukkit.configuration.file.FileConfiguration;

public enum MessageType {
    WARN(WebhookMessage.WARN_TITLE, WebhookMessage.WARN_MESSAGE),
    BAN(WebhookMessage.BAN_TITLE, WebhookMessage.BAN_MESSAGE),
    TEMPBAN(WebhookMessage.TEMPBAN_TITLE, WebhookMessage.TEMPBAN_MESSAGE),
    UNBAN(WebhookMessage.UNBAN_TITLE, WebhookMessage.UNBAN_MESSAGE),
    MUTE(WebhookMessage.MUTE_TITLE, WebhookMessage.MUTE_MESSAGE),
    TEMPMUTE(WebhookMessage.TEMPMUTE_TITLE, WebhookMessage.TEMPMUTE_MESSAGE),
    UNMUTE(WebhookMessage.UNMUTE_TITLE, WebhookMessage.UNMUTE_MESSAGE),
    MAINTENANCE(WebhookMessage.MAINTENANCE_TITLE, WebhookMessage.MAINTENANCE_MESSAGE);

    private String[] value;
    private final String key;
    private final String message;
    private final String title;

    MessageType(WebhookMessage title, WebhookMessage message) {
        this.key = title.name();
        this.value = new String[] {title.getValue(), message.getValue()};
        this.title = title.getValue();
        this.message = message.getValue();
    }

    public String[] getValue() {
        if (value == null) {
            reloadValue();
        }

        return value;
    }

    public void reloadValue() {
        FileConfiguration config = LangManager.getMessagesConfig();
        String loadedTitle = config.getString(title);
        String loadedMessage = config.getString(message);

        if (loadedTitle != null && loadedMessage != null) {
            value = new String[] {loadedTitle, loadedMessage};
        } else {
            value = new String[] {"Message not found for key: " + title, "Message not found for key: " + message};
        }
    }

    public static void reloadAllValues() {
        for (MessageType messageType : values()) {
            messageType.reloadValue();
        }
    }
}
