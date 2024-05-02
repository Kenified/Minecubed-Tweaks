package com.minecubedmc.features;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class NoTorchesInDeepDark implements Listener {

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event){
        final Block block = event.getBlock();
        final Material type = block.getType();

        if (
                type.equals(Material.TORCH) ||
                type.equals(Material.WALL_TORCH) ||
                type.equals(Material.LANTERN) ||
                type.equals(Material.FIRE)
        ){
            Location loc = block.getLocation();
            String biome = block.getWorld().getBiome(loc).toString();

            if (biome.equals("DEEP_DARK")) {
                event.getPlayer().sendMessage("Only soul torches seem to work here...");
                event.setCancelled(true);
            }
        }
    }
}
