package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

public class AFKFoxHarvestBlock implements Listener {
    
    Tweaks plugin;
    
    public AFKFoxHarvestBlock(Tweaks plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onChangeBlock(final EntityChangeBlockEvent event) {
        final Entity entity = event.getEntity();
        
        if (!(entity instanceof Fox)) {
            return;
        }
        
        final Collection<Player> players = event.getBlock().getLocation().getNearbyPlayers((event.getBlock().getWorld().getSimulationDistance() * 16) + 8);
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.setCancelled(true);
        }
    }
}
