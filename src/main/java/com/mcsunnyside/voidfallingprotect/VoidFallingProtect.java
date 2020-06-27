package com.mcsunnyside.voidfallingprotect;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
        if(event.getPlayer().getGameMode() == GameMode.SPECTATOR){
            return;
        }
        if(event.getTo().getBlockY() >= 0){ //忽略玩家正常移动
            return;
        }
        if(event.getFrom().getBlockY() < 0){ //忽略玩家已经在低处移动
            return;
        }
        if(!event.getFrom().getBlock().getType().isSolid()){ //忽略玩家通过不完整方块正常离开世界范围
            return;
        }
        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(this,()->{
            teleportToSafeLoc(event.getPlayer());
        },1);
    }

    public void teleportToSafeLoc(Player player){
        getLogger().warning("Prevent player "+player.getName()+" fell out of the world.");
        player.sendMessage(ChatColor.YELLOW+"检测到不安全的移动，已传送你到安全的地点。");
        player.teleport(player.getWorld().getSpawnLocation());
    }
}
