package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;

import java.util.Collection;

public class BlockAFKDispenserGrow implements Listener {
    
    @EventHandler
    public void onDispense(final BlockDispenseEvent event) {
        final Material dispensedItem = event.getItem().getType();
        
        if (!dispensedItem.equals(Material.BONE_MEAL)) {
            return;
        }
        
        //Get nearby players and if all of them are afk cancel the growth
        final Collection<Player> players = event.getBlock().getLocation().getNearbyPlayers((event.getBlock().getWorld().getSimulationDistance() * 16) + 8 );
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.setCancelled(true);
        }
    }
}
