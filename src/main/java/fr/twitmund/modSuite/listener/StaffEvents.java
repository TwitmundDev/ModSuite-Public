package fr.twitmund.modSuite.listener;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import fr.twitmund.modSuite.Main;
import fr.twitmund.modSuite.manager.PlayerManager;
import fr.twitmund.modSuite.utils.filesManager.enums.LangEnum;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class StaffEvents implements Listener {

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        e.setCancelled(PlayerManager.isInMod(e.getPlayer()) || Main.getInstance().getFreeze().containsKey(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent e) {
        e.setCancelled(PlayerManager.isInMod(e.getPlayer()) || Main.getInstance().getFreeze().containsKey(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onBlockBroke(BlockBreakEvent e) {
        e.setCancelled(PlayerManager.isInMod(e.getPlayer()) || Main.getInstance().getFreeze().containsKey(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e) {
        e.setCancelled(PlayerManager.isInMod(e.getPlayer()) || Main.getInstance().getFreeze().containsKey(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onItemDropped(PlayerDropItemEvent e) {
        e.setCancelled(PlayerManager.isInMod(e.getPlayer()) || Main.getInstance().getFreeze().containsKey(e.getPlayer().getUniqueId()));
    }


    @EventHandler
    public void onEntityDamaged(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        e.setCancelled(PlayerManager.isInMod((Player) e.getEntity()) || Main.getInstance().isPlayerFreezed((Player) e.getEntity()));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;
        Player damager = (Player) e.getDamager();

        if (PlayerManager.isInMod(damager)) {
            e.setCancelled(damager.getInventory().getItemInHand().getType() != Material.STICK);
        }
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        e.setCancelled(PlayerManager.isInMod(e.getPlayer()) || Main.getInstance().getFreeze().containsKey(e.getPlayer()));
    }

    @EventHandler
    public void onInventoryClickedEvent(InventoryClickEvent e) {
        e.setCancelled(PlayerManager.isInMod((Player) e.getWhoClicked()));
    }


    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (Main.getInstance().getFreeze().containsKey(e.getPlayer().getUniqueId())) {
            Player player = e.getPlayer();
            if (Main.freezeSoundEbabled) {
            player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_PLACE, 60.0f, 4f);
            }
            player.sendTitle(LangEnum.FREEZE_PLAYER_TITLE.getValue(), LangEnum.FREEZE_PLAYER_SUBTITLE.getValue(), 1, 50, 100);
            player.sendMessage(LangEnum.FREEZE_PLAYER_MESSAGE.getValue());
            e.setTo(e.getFrom());
        }
    }


}
