package fr.twitmund.modSuite.commands;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.DbConnection;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class ReloadCommand implements TabExecutor {

    //TODO a test reload command
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cCette commande doit être exécutée par un joueur.");
            return true;
        }
        if (!player.hasPermission(PermissionEnum.PERMISSION_COMMAND_RELOAD.getValue())) {
            player.sendMessage(LangEnum.NO_PERMISSION.getValue());
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(LangEnum.USAGE.getValue().replace("%usage%", "/reload <all|config|lang|database|permissions>"));
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "all" -> {
                reloadAll();
                player.sendMessage(LangEnum.RELOAD_ALL.getValue());
            }
            case "config" -> {
                reloadConfig();
                player.sendMessage(LangEnum.RELOAD_CONFIG.getValue());
            }
            case "lang" -> {
                reloadLang();
                player.sendMessage(LangEnum.RELOAD_LANG.getValue());
            }
            case "database" -> {
                reloadDatabase();
                player.sendMessage(LangEnum.RELOAD_DATABASE.getValue());
            }
            case "permissions" -> {
                reloadPermissions();
                player.sendMessage(LangEnum.RELOAD_PERMISSIONS.getValue());
            }
            default ->
                player.sendMessage(LangEnum.USAGE.getValue().replace("%usage%", "/reload <all|config|lang|database|permissions>"));
        }
        return true;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return Arrays.asList("all", "config", "lang", "database", "permissions");
    }

    public static void reloadLang() {
        fr.twitmund.modSuite.utils.filesManager.LangManager.loadMessagesConfig();
        for (LangEnum lang : LangEnum.values()) {
            lang.reloadValue();
        }
    }
    public static void reloadPermissions() {
        fr.twitmund.modSuite.utils.filesManager.PermissionFileManager.loadMessagesConfig();
        for (PermissionEnum permission : PermissionEnum.values()) {
            permission.reloadValue();
        }
    }
    public static void reloadConfig() {
        Main.getInstance().reloadConfig();
        Main.getInstance().getConfig().options().copyDefaults(true);
        Main.getInstance().saveDefaultConfig();
    }
    public static void reloadDatabase() {
        // Recharge le fichier de config DB
        fr.twitmund.modSuite.utils.filesManager.DbConnectionManager.loadMessagesConfig();
        fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum.reloadAllValues();
        try {
            Main.getInstance().setDbHost(fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum.DB_HOST.getValue());
            Main.getInstance().setDbPort(fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum.DB_PORT.getValue());
            Main.getInstance().setDbName(fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum.DB_NAME.getValue());
            Main.getInstance().setDbUsername(fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum.DB_USER.getValue());
            Main.getInstance().setDbPassword(fr.twitmund.modSuite.utils.filesManager.enums.DbConnectionEnum.DB_PASSWORD.getValue());
            new DbConnection();
            Main.getInstance().reloadDAOs();
        } catch (Exception e) {
            Main.getInstance().getLogger().severe("Erreur lors du reload de la base de données : " + e.getMessage());
        }
    }
    public static void reloadAll() {
        reloadConfig();
        reloadLang();
        reloadPermissions();
        reloadDatabase();
    }
}
