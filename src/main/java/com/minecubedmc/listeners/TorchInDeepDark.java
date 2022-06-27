package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class TorchInDeepDark implements Listener {

    Tweaks plugin;

    public TorchInDeepDark(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Block block = e.getBlock();
        Material type = block.getType();

        if (
                type.equals(Material.TORCH) ||
                type.equals(Material.WALL_TORCH) ||
                type.equals(Material.LANTERN) ||
                type.equals(Material.FIRE)
        ){
            Location loc = block.getLocation();
            String biome = block.getWorld().getBiome(loc).toString();

            if (biome.equals("DEEP_DARK"))
                e.setCancelled(true);
        }
    }
}
