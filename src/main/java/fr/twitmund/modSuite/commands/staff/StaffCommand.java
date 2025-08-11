package fr.twitmund.modSuite.commands.staff;

import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.listener.StaffEvents;
import fr.twitmund.modSuite.manager.PlayerManager;
import fr.twitmund.modSuite.manager.staff.VanishManager;
import fr.twitmund.modSuite.utils.ItemBuilder;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import fr.twitmund.modSuite.utils.filesManager.enums.PermissionEnum;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;

public class StaffCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        if (!player.hasPermission(PermissionEnum.PERMISSION_STAFF_COMMAND.getValue())) {
            player.sendMessage(LangEnum.NO_PERMISSION.getValue());
        }

        if (PlayerManager.isInMod(player)) {
            PlayerManager pm = PlayerManager.getFromPlayer(player);
            Main.getInstance().getModerators().remove(player.getUniqueId());
            player.getInventory().clear();
            player.sendMessage(LangEnum.STAFF_MODE_OFF.getValue());
            if (!(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)) {
                player.setAllowFlight(false);
                player.setFlying(false);
                VanishManager.setVanished(player, false);
            }

            pm.giveInventory();
            pm.destroy();

            return false;

        }


        PlayerManager pm = new PlayerManager(player);
        pm.init();
        Main.getInstance().getModerators().add(player.getUniqueId());
        player.sendMessage(LangEnum.STAFF_MODE_ON.getValue());
        player.setAllowFlight(true);
        player.setFlying(true);
        VanishManager.setVanished(player, true);


        pm.saveInventory();

        ItemBuilder Invsee = new ItemBuilder(Material.CHEST).setName(ChatColor.AQUA + "Invsee").setLore(ChatColor.BLUE + "Permet de voir l'inventaire d'un joueur");
        ItemBuilder Reports = new ItemBuilder(Material.BOOK).setName(ChatColor.AQUA + "Reports").setLore(ChatColor.BLUE + "Voir les reports");
        ItemBuilder Freeze = new ItemBuilder(Material.SLIME_BALL).setName(ChatColor.AQUA + "Freeze").setLore(ChatColor.BLUE + "Freeze un joueur");
        ItemBuilder kbTesterx5 = new ItemBuilder(Material.BLAZE_ROD).setName(ChatColor.AQUA + "Kb x5").setLore(ChatColor.BLUE + "Kb un joueur").addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
        ItemBuilder kbTesterx10 = new ItemBuilder(Material.END_ROD).setName(ChatColor.AQUA + "Kb x10").setLore(ChatColor.BLUE + "Kb un joueur").addUnsafeEnchantment(Enchantment.KNOCKBACK, 10);
        ItemBuilder Kill = new ItemBuilder(Material.STICK).setName(ChatColor.AQUA + "Instant Killer").setLore(ChatColor.BLUE + "Tue instantanément un joueur");
        ItemBuilder randomTP = new ItemBuilder(Material.ENDER_EYE).setName(ChatColor.AQUA + "Random TP").setLore(ChatColor.BLUE + "Se téléporte aléatoirement sur un joueur");

        player.getInventory().setItem(0, Invsee.toItemStack());
        player.getInventory().setItem(1, Reports.toItemStack());
        player.getInventory().setItem(2, Freeze.toItemStack());
        player.getInventory().setItem(3, kbTesterx5.toItemStack());
        player.getInventory().setItem(4, kbTesterx10.toItemStack());
        player.getInventory().setItem(5, Kill.toItemStack());
        player.getInventory().setItem(6, randomTP.toItemStack());

        return false;
    }

}
