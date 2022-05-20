package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

public class CropGrowEvent implements Listener {

    Tweaks plugin;

    public CropGrowEvent (Tweaks plugin){    this.plugin = plugin;  }

    @EventHandler
    public void onSaplingGrow(BlockGrowEvent e){
        Block block = e.getBlock();
        if (block.getType().equals( Material.CARROT )){
            e.setCancelled(true);
            block.setType(Material.AIR);

            plugin.getLogger().info("Carrot sapling tried to grow!");
        }

    }
}
