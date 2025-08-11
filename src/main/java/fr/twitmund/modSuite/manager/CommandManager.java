package fr.twitmund.modSuite.manager;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.commands.MaintenanceCommand;
import fr.twitmund.modSuite.commands.ReloadCommand;
import fr.twitmund.modSuite.commands.bans.BanCommand;
import fr.twitmund.modSuite.commands.bans.TempBanCommand;
import fr.twitmund.modSuite.commands.bans.UnbanCommand;
import fr.twitmund.modSuite.commands.mute.MuteCommand;
import fr.twitmund.modSuite.commands.mute.UnmuteCommand;
import fr.twitmund.modSuite.commands.staff.FreezeCommand;
import fr.twitmund.modSuite.commands.staff.LookUpCommand;
import fr.twitmund.modSuite.commands.staff.StaffCommand;
import fr.twitmund.modSuite.commands.staff.VanishCommand;
import fr.twitmund.modSuite.commands.warns.WarnCommand;

public class CommandManager {
    public static void registerCommands() {
        Main.getInstance().getCommand("warn").setExecutor(new WarnCommand());
        Main.getInstance().getCommand("ban").setExecutor(new BanCommand());
        Main.getInstance().getCommand("unban").setExecutor(new UnbanCommand());
        Main.getInstance().getCommand("maintenance").setExecutor(new MaintenanceCommand());
        Main.getInstance().getCommand("mute").setExecutor(new MuteCommand());
        Main.getInstance().getCommand("unmute").setExecutor(new UnmuteCommand());
        Main.getInstance().getCommand("lookup").setExecutor(new LookUpCommand());
        Main.getInstance().getCommand("tempban").setExecutor(new TempBanCommand());
        Main.getInstance().getCommand("staff").setExecutor(new StaffCommand());
        Main.getInstance().getCommand("freeze").setExecutor(new FreezeCommand());
        Main.getInstance().getCommand("vanish").setExecutor(new VanishCommand());
        Main.getInstance().getCommand("mreload").setExecutor(new ReloadCommand());
    }
}
