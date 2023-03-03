package com.minecubedmc.features;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBarrierPlacement implements Listener {

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event){
        final Material type = event.getBlock().getType();
        final Player player = event.getPlayer();

        if ( type.equals(Material.BARRIER) && !player.hasPermission("group.helper") ){
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot place that here."));

        }
    }
}
