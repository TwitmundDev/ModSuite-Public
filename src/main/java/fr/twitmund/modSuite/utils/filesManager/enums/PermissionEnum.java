package fr.twitmund.modSuite.utils.filesManager.enums;

import fr.twitmund.modSuite.utils.HexaColor;
import fr.twitmund.modSuite.utils.filesManager.PermissionFileManager;
import org.bukkit.configuration.file.FileConfiguration;

public enum PermissionEnum {
    // PERMISSIONS
    PERMISSION_BAN("permission_ban"),
    PERMISSION_BANIP("permission_banip"),
    PERMISSION_TEMPBAN("permission_tempban"),
    PERMISSION_TEMPBANIP("permission_tempbanip"),
    PERMISSION_UNBAN("permission_unban"),
    PERMISSION_WARN("permission_warn"),
    PERMISSION_KICK("permission_kick"),
    PERMISSION_MUTE("permission_mute"),
    PERMISSION_NOTIFY("permission_notify"),
    PERMISSION_MAINTENANCE("permission_maintenance"),
    PERMISSION_MAINTENANCE_BYPASS("permission_maintenance_bypass"),
    PERMISSION_UNMUTE("permission_unmute"),
    PERMISSION_COMMAND_LOOKUP("permission_command_lookup"),
    PERMISSION_BAN_IP("permission_banIp"),
    PERMISSION_HISTORY("permission_history"),
    PERMISSION_STAFF_COMMAND("permission_staff_command"),
    PERMISSION_FREEZE_COMMAND("permission_freeze_command"),
    PERMISSION_VANISH_COMMAND("permission_vanish_command"),
    PERMISSION_BYPASS_ALTS("permission_bypass_alts"),
    PERMISSION_COMMAND_RELOAD("permission_command_reload")

    ;

    private String value;
    private final String key;

    PermissionEnum(String key) {
        this.key = key;
    }

    public String getValue() {
        if (value == null) {
            reloadValue();
        }

        return value;
    }

    public void reloadValue() {
        FileConfiguration config = PermissionFileManager.getMessagesConfig();
        String loadedValue = config.getString(key); // null if not found
        if (loadedValue != null) {
            value = HexaColor.format(loadedValue);
        } else {
            value = "Message not found for key: " + key;
        }
    }

    public static void reloadAllValues() {
        for (PermissionEnum messageEnum : values()) {
            messageEnum.reloadValue();
        }
    }

}
