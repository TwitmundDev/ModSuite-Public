package fr.twitmund.modSuite.manager;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class EventManager {

    public static void register() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinListener(), Main.getInstance());
        pm.registerEvents(new PlayerChatEventListener(), Main.getInstance());
        pm.registerEvents(new StaffEvents(), Main.getInstance());
        pm.registerEvents(new ModItemInterract(), Main.getInstance());
        pm.registerEvents(new AltsManager(), Main.getInstance());
    }
}
