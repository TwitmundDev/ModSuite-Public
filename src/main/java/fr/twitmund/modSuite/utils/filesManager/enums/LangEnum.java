package fr.twitmund.modSuite.utils.filesManager.enums;

import fr.twitmund.modSuite.utils.HexaColor;
import fr.twitmund.modSuite.utils.filesManager.DbConnectionManager;
import fr.twitmund.modSuite.utils.filesManager.LangManager;
import org.bukkit.configuration.file.FileConfiguration;

public enum LangEnum {

    PLAYER_NOT_FOUND("player_not_found"),

    NO_PERMISSION("no_permission"),
    USAGE("usage"),
    INVALID_NUMBER("invaild_number"),
    INVALID_DURATION("invalid_duration"),

    MAINTENANCE_ON("maintenance_on"),
    MAINTENANCE_OFF("maintenance_off"),
    MAINTENANCE_ALREADY_OFF("maintenance_already_off"),
    MAINTENANCE_ALREADY_ON("maintenance_already_on"),
    MAINTENANCE_USAGE("maintenance_off"),
    MAINTENANCE_KICK_MESSAGE("maintenance_kick_message"),

    WARN_SUCCESS("warn_success"),
    WARN_TITLE("warn_title_notify"),
    WARN_SUBTITLE("warn_subtitle_notify"),
    WARN_MESSAGE("warn_message"),

    BAN_SUCCESS("ban_success"),
    BAN_PLAYER_ALREADY_BANNED("ban_player_already_banned"),
    BAN_MESSAGE("ban_message"),
    BAN_TRIED_JOIN("notify_ban_try_to_connect"),

    TEMPBAN_SUCCESS("tempban_success"),
    TEMPBAN_MESSAGE("tempban_message"),


    MUTE_SUCCESS("mute_success"),
    MUTE_PLAYER_ALREADY_MUTED("mute_player_already_muted"),
    MUTE_MESSAGE("mute_message"),
    MUTE_PLAYER_TRY_CHAT("mute_player_try_to_chat"),

    UNBAN_SUCCESS("unban_success"),
    UNBAN_PLAYER_NOT_BANNED("unban_player_not_banned"),

    GUI_WARN_TITLE("gui_warn_title"),
    GUI_DATE("gui_date"),
    GUI_PREVIOUS_PAGE("gui_previous_page"),
    GUI_NEXT_PAGE("gui_next_page"),

    GUI_WARNED_BY("gui_warned_by"),
    GUI_NO_WARN("gui_no_warn"),
    GUI_ITEM_WARN("gui_item_warn"),

    GUI_MUTE_TITLE("gui_mute_title"),
    GUI_MUTED_BY("gui_muted_by"),
    GUI_NO_MUTES("gui_no_mutes"),
    GUI_ITEM_MUTE("gui_item_mute"),

    GUI_UNMUTE_DATE("gui_unmute_date"),
    GUI_IS_MUTED("gui_is_muted"),

    GUI_IS_BANNED("gui_is_banned"),
    GUI_UNBAN_DATE("gui_unban_date"),


    GUI_LOOKUP_TITLE("gui_lookup_title"),


    GUI_ITEM_BAN("gui_item_ban"),
    GUI_BAN_TITLE("gui_ban_title"),

    GUI_ALTS_TITLE("gui_title_alts"),
    GUI_ALTS_ITEM("gui_item_alts"),
    GUI_NO_ALTS("gui_no_alts"),


    GUI_BACK("gui_back"),

    STAFF_MODE_ON("staff_mode_on"),
    STAFF_MODE_OFF("staff_mode_off"),


    NO_OTHER_PLAYER("no_other_player"),
    TELEPORTED_TO("teleported_to"),

    FREEZE_MESSAGE("freeze_message"),

    FREEZE_PLAYER_TITLE("freeze_player_title"),
    FREEZE_PLAYER_SUBTITLE("freeze_player_submessage"),
    FREEZE_PLAYER_MESSAGE("freeze_player_message"),
    FREEZE_SUCCESS("freeze_success"),

    UNFREEZE_PLAYER_MESSGE("unfreeze_player_message"),
    UNFREEZE_SUCCESS("unfreeze_success"),

    VANISH_ON("vanish_on"),
    VANISH_OFF("vanish_off"),


    BLACKLIST_PLAYER_ALREADY_BLACKLISTED("blacklist_player_already_blacklisted"),
    BLACKLIST_PLAYER_NOT_BLACKLISTED("blacklist_player_not_blacklisted"),
    BLACKLIST_SUCCESS("blacklist_success"),
    BLACKLIST_REMOVE("blacklist_remove_success"),
    ONLY_PLAYER_CAN_USE("only_player_can_use"),
    BLACKLIST_PLAYER_TRY_TO_CONNECT("blacklist_player_try_to_connect"),


    NOT_ALLOWED_ALTS_MESSAGE("not_allowed_alts_message"),
    NOTIFY_ALTS("notify_alts"),
    RELOAD_ALL("reload_all"),
    RELOAD_CONFIG("reload_config"),
    RELOAD_LANG("reload_lang"),
    RELOAD_DATABASE("reload_database"), RELOAD_PERMISSIONS("reload_permissions"),;


    private String value;
    private final String key;

    LangEnum(String key) {
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
        for (LangEnum messageEnum : values()) {
            messageEnum.reloadValue();
        }
    }
}
