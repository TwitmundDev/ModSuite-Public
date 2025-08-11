package fr.twitmund.modSuite.listener;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.BanEntityRepository;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.db.repository.TempBanEntityRepository;
import fr.twitmund.modSuite.utils.ChatUtil;
import fr.twitmund.modSuite.utils.TimeUtil;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.List;

public class AltsManager implements Listener {

    // todo finir le alt checker et a tester
    @EventHandler
    public void onPlayerJoinWithAlts(PlayerJoinEvent event) {
        System.out.println("Event triggered for player: " + event.getPlayer().getName());
        // Logic to handle player joining with alts
        // This could include checking for alts, notifying staff, etc.
        if (event.getPlayer().hasPermission(PermissionEnum.PERMISSION_BYPASS_ALTS.getValue())) {
            System.out.println("il a la permission de bypass les alts");
            return;
        }
        if (!checkAlts(event.getPlayer().getName(), event.getPlayer().getAddress().getAddress().getHostAddress())) {
            System.out.println("il n'a pas d'alt");
            // No alts found, no action needed
            return;
        }
        System.out.println("il a des alts");
        PlayerEntityRepository.createPlayerEntity(event.getPlayer().getAddress().getAddress().getHostAddress(), event.getPlayer().getName());
        PlayerEntity altAccount = PlayerEntityRepository.getPlayerByName(event.getPlayer().getName());


        String punishmentType = Main.getInstance().getConfig().getString("punishment_alts_type");
        long punishmentTime = Long.parseLong(Main.getInstance().getConfig().getString("punishment_alts_time"));
        if (punishmentType == null || punishmentType.isEmpty()) {
            ChatUtil.notifyStaff(LangEnum.NOTIFY_ALTS.getValue()
                    .replace("%player%", event.getPlayer().getName())
                    .replace("%alt_player%", getAltsByIpString(event.getPlayer().getAddress().getAddress().getHostAddress())));
            return;
        }

        PlayerEntity consoleEntity = PlayerEntityRepository.getPlayerByName("Console");
        switch (punishmentType) {
            case "kick":
                event.getPlayer().kickPlayer(LangEnum.NOT_ALLOWED_ALTS_MESSAGE.getValue());
                notifyAlts(altAccount);
                break;
            case "ban":
                // Logic to ban the player
                BanEntityRepository.createBanEntity(LangEnum.NOT_ALLOWED_ALTS_MESSAGE.getValue(),
                        consoleEntity, altAccount);
                event.getPlayer().kickPlayer(LangEnum.NOT_ALLOWED_ALTS_MESSAGE.getValue());
                notifyAlts(altAccount);
                break;
            case "tempban":
                if (punishmentTime <= 0) {
                    event.getPlayer().kickPlayer(LangEnum.NOT_ALLOWED_ALTS_MESSAGE.getValue());
                    notifyAlts(altAccount);
                    return;
                }
                // Logic to temporarily ban the player
                TempBanEntityRepository.createTempBanEntity(
                        LangEnum.NOT_ALLOWED_ALTS_MESSAGE.getValue(),
                        consoleEntity,
                        altAccount,
                        TimeUtil.parseTime(punishmentTime)); // Convert to milliseconds \\
            case "banip":
                //todo implements banip and tempbanip logic
                break;
            case "tempbanip":
                break;
            default:
                notifyAlts(altAccount);
        }

    }

    /**
     * Checks if a player has alts based on their name and IP address.
     *
     * @param playerName The name of the player.
     * @param ipAddress  The IP address of the player.
     * @return true if the player has alts, false otherwise.
     */
    public static boolean checkAlts(String playerName, String ipAddress) {
        if (playerName == null || playerName.isEmpty() || ipAddress == null || ipAddress.isEmpty()) {
            return false; // Invalid input
        }
        if (PlayerEntityRepository.doPlayerExistInDb(playerName)) {
            return PlayerEntityRepository.checkAltsForPlayerIP(ipAddress);

        }
        return false; // Player does not exist in the database
    }

    /**
     * Retrieves a list of alternate accounts associated with a given IP address.
     *
     * @param ipAddress The IP address to check for alternate accounts.
     * @return A list of player names associated with the given IP address.
     */
    public static List<String> getAltsByIp(String ipAddress) {
        return PlayerEntityRepository.getAltsByIp(ipAddress);
    }

    /**
     * Retrieves a string representation of alternate accounts associated with a given IP address.
     *
     * @param ipAddress The IP address to check for alternate accounts.
     * @return A comma-separated string of player names associated with the given IP address.
     */
    public static String getAltsByIpString(String ipAddress) {
        System.out.println(PlayerEntityRepository.getAltsByIp(ipAddress));
        return String.join(", ", PlayerEntityRepository.getAltsByIp(ipAddress));
    }

    /**
     * Retrieves a list of alternate accounts associated with a given player entity.
     *
     * @param playerEntity The player entity to check for alternate accounts.
     * @return A list of player names associated with the given player entity.
     */
    public static String getAltsByPlayerEntity(PlayerEntity playerEntity) {
        return String.join(", ", PlayerEntityRepository.getAltsByPlayerEntity(playerEntity));
    }

    /**
     * Bans a player with the specified reason, author, and target.
     *
     * @param reason The reason for the ban.
     * @param author The player who is issuing the ban.
     * @param target The player being banned.
     */
    public static void banPlayer(String reason, PlayerEntity author, PlayerEntity target) {
        BanEntityRepository.createBanEntity(reason, author, target);
    }

    /**
     * Temporarily bans a player with the specified reason, author, target, and duration.
     *
     * @param reason   The reason for the temporary ban.
     * @param author   The player who is issuing the temporary ban.
     * @param target   The player being temporarily banned.
     * @param duration The duration of the temporary ban in milliseconds.
     */
    public static void tempBanPlayer(String reason, PlayerEntity author, PlayerEntity target, long duration) {
        TempBanEntityRepository.createTempBanEntity(reason, author, target, duration);
    }

    public static void notifyAlts(PlayerEntity altAccount){
        ChatUtil.notifyStaff(LangEnum.NOTIFY_ALTS.getValue()
                .replace("%player%", altAccount.getPlayer_Name())
                .replace("%alt_player%", getAltsByPlayerEntity(altAccount)));
    }


}

