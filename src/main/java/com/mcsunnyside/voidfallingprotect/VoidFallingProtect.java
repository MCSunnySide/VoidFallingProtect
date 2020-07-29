package com.mcsunnyside.voidfallingprotect;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class VoidFallingProtect extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event){
        if(event instanceof PlayerTeleportEvent){
            return;
        }
        if(event.getTo() == null){
            return;
        }
        if (event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            return;
        }
        if (event.getTo().getBlockY() >= 0) {
            return;
        }
        if (event.getFrom().getBlockY() < 0) {
            return;
        }
        if (!event.getFrom().getBlock().getType().isSolid()) {
            return;
        }
        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(this, () -> teleportToSafeLoc(event.getPlayer()), 1);
    }

    public void teleportToSafeLoc(Player player) {
        getLogger().warning("Prevent player " + player.getName() + " fell out of the world.");
        Location location = player.getLocation();
        boolean safeFound = true;
        while (location.getBlock().getType() != Material.AIR || location.add(0, 1, 0).getBlock().getType() != Material.AIR) {
            if (location.getBlockY() > 254) {
                safeFound = false;
                break;
            }
            location = location.add(0, 1, 0);
        }
        if (safeFound) {
            player.teleport(location);
        } else {
            player.teleport(player.getWorld().getSpawnLocation());
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("message", "Unsafe movement detected, we teleport you to a safe location to avoid you drop into the void!")));
    }
}
