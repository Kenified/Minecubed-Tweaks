package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBarrierPlacement implements Listener {

    Tweaks plugin;

    public BlockBarrierPlacement(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Material type = e.getBlock().getType();
        Player player = e.getPlayer();

        if ( type.equals(Material.BARRIER) && !player.hasPermission("group.helper") ){
            e.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot place that here."));

        }
    }
}
