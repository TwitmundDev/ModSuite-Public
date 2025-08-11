package fr.twitmund.modSuite.listener;

import dev.triumphteam.gui.guis.Gui;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.db.entity.PlayerEntity;
import fr.twitmund.modSuite.db.repository.PlayerEntityRepository;
import fr.twitmund.modSuite.manager.PlayerManager;
import fr.twitmund.modSuite.manager.staff.FreezeManager;
import fr.twitmund.modSuite.manager.staff.VanishManager;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static fr.twitmund.modSuite.gui.LookupGUI.createGui;
import static org.bukkit.entity.EntityType.EYE_OF_ENDER;

public class ModItemInterract implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();

        if (!(PlayerManager.isInMod(player))) return;
        if (!(e.getRightClicked() instanceof Player)) return;
        Player target = (Player) e.getRightClicked();
        PlayerEntity targetEntity = PlayerEntityRepository.getPlayerByName(target.getName());


        switch (player.getInventory().getItemInMainHand().getType()) {
            /**
             * InvSee
             */
            case CHEST:
                Inventory inv = Bukkit.createInventory(null, 5 * 9, ChatColor.GRAY + "Inventaire de " + ChatColor.RED + target.getDisplayName());


                for (int i = 0; i < 36; i++) {
                    if (target.getInventory().getItem(i) != null) {
                        inv.setItem(i, target.getInventory().getItem(i));
                    }
                }
                inv.setItem(36, target.getInventory().getItemInOffHand());
                inv.setItem(37, target.getInventory().getHelmet());
                inv.setItem(38, target.getInventory().getChestplate());
                inv.setItem(39, target.getInventory().getLeggings());
                inv.setItem(40, target.getInventory().getBoots());

                player.openInventory(inv);
                break;

            /**
             * Instant kill
             */
            case STICK:
                target.damage(target.getMaxHealth());
                break;

            /**
             * @TODO Freeze
             */
//            case SLIME_BALL:
////                if (FreezeManager.isPlayerFreezed(target)) {
////                    FreezeManager.unfreezePlayer(target);
////                    target.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 60.0f, 4f);
////                    target.sendTitle(LangEnum.UNFREEZE_PLAYER_MESSGE.getValue(), "", 1, 10, 20);
////                    player.sendMessage(LangEnum.UNFREEZE_SUCCESS.getValue().replace("%player%", target.getName()));
////                } else {
////                    FreezeManager.freezePlayer(target);
////                    player.sendMessage(LangEnum.FREEZE_SUCCESS.getValue().replace("%player%", target.getName()));
////                }
//
//                /** if (Main.getInstance().isFreezed(target)){
//                 player.sendMessage(Main.getInstance().elementa+"Unfreeze " + target.getName());
//                 target.sendTitle(ChatColor.RED +"VOUS AVEZ ÉTÉ UNFREEZE","",1,10,20);
//                 target.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP , 60.0f, 4f);
//                 Main.getInstance().freeze.remove(target.getUniqueId());
//                 }else{
//                 target.playSound(player.getLocation(), Sound.ENTITY_WOLF_HURT , 60.0f, 4f);
//                 target.sendTitle(ChatColor.RED +"VOUS AVEZ ÉTÉ FREEZE A","",1,10,20);
//                 player.sendMessage(Main.getInstance().elementa+"Freeze " + target.getName());
//                 Main.getInstance().freeze.put(target.getUniqueId(), target.getLocation());
//                 }**/
//                player.performCommand("freeze " + target.getName());
//                break;
//
//
//                /**
//                 * @TODO FAIRE LE FREEZE SANS LE BUG QUI EXECUTE LE CODE 2 FOIS
//                 */

            case BOOK:
                Gui gui = createGui(targetEntity, player);
                gui.open(player);
                break;
            case END_ROD:
                target.setVelocity(new Vector(0,4f,0));
                break;


            default:
                break;
        }


    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();

        if (!(PlayerManager.isInMod(player))) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR) return;


        switch (player.getInventory().getItemInMainHand().getType()) {

            case ENDER_EYE:
                List<Player> list = new ArrayList<>(Bukkit.getOnlinePlayers());
                list.remove(player);

                if (list.size() == 0) {
                    player.sendMessage(LangEnum.NO_OTHER_PLAYER.getValue());
                    return;
                }
                Player target = list.get(new Random().nextInt(list.size()));
                player.teleport(target.getLocation());
                player.sendMessage(LangEnum.TELEPORTED_TO.getValue().replace("%player%", target.getDisplayName()));

                break;


            case BLAZE_POWDER:
                if (VanishManager.isVanished(player)){
                    VanishManager.setVanished(player, false);
                    player.sendMessage(LangEnum.VANISH_OFF.getValue());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 60.0f, 4f);
                } else {
                    VanishManager.setVanished(player, true);
                    player.sendMessage(LangEnum.VANISH_ON.getValue());
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 60.0f, 4f);
                }
                break;
            default:
                break;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        for (Player players : Bukkit.getOnlinePlayers()) {
            if (PlayerManager.isInMod(player)) {
                PlayerManager pm = PlayerManager.getFromPlayer(players);
                if (pm.isVanished()) {
                    player.hidePlayer(players);
                }

            }
        }


    }

    public void FreezePlayer(Player player) {
        Main.getInstance().freeze.put(player.getUniqueId(), player.getLocation());
    }

    public void UnFreezePlayer(Player player) {
        Main.getInstance().freeze.remove(player.getUniqueId(), player.getLocation());
    }

}
