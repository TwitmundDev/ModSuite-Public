package fr.twitmund.modSuite.manager;

import fr.twitmund.modSuite.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {
    private Player player;
    @Getter
    private ItemStack[] items = new ItemStack[40];
    @Getter
    private boolean Vanished;


    public PlayerManager(Player player) {
        this.player = player;
        Vanished = false;
    }

    public static boolean isInMod(Player player) {
        return Main.getInstance().Moderators.contains(player.getUniqueId());

    }


    public void init() {
        Main.getInstance().players.put(player.getUniqueId(), this);
    }

    public void destroy() {
        Main.getInstance().players.remove(player.getUniqueId());
    }

    public static PlayerManager getFromPlayer(Player player) {
        return Main.getInstance().players.get(player.getUniqueId());

    }

    public void setVanished(boolean vanished) {
        Vanished = vanished;
        if (vanished) {
            Bukkit.getOnlinePlayers().forEach(players -> {
                players.hidePlayer(Main.getInstance(), player);
            });
        } else {
            Bukkit.getOnlinePlayers().forEach(players -> {
                players.showPlayer(Main.getInstance(), player);
            });

        }
    }

    public void saveInventory() {
        //player.sendMessage("save inv");
        for (int i = 0; i < 36; i++) {
            ItemStack item = player.getInventory().getItem(i);
            if (item != null) {
                items[i] = item;
            }
        }
        items[36] = player.getInventory().getHelmet();
        items[37] = player.getInventory().getChestplate();
        items[38] = player.getInventory().getLeggings();
        items[39] = player.getInventory().getBoots();


        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
    }

    public void giveInventory() {
        //player.sendMessage("give inv");
        player.getInventory().clear();
        for (int i = 0; i < 36; i++) {
            ItemStack item = items[i];
            if (item != null) {
                player.getInventory().setItem(i, item);
            }
        }
        player.getInventory().setHelmet(items[36]);
        player.getInventory().setChestplate(items[37]);
        player.getInventory().setLeggings(items[38]);
        player.getInventory().setBoots(items[39]);
    }
}
