package com.minecubedmc.features;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class NoXPDrop implements Listener {

    @EventHandler
    public void blockBreakListener(final BlockBreakEvent event){
        if (event.isCancelled()) return;
    
        final Block block = event.getBlock();

        if (block.getType().equals(Material.DEEPSLATE_EMERALD_ORE)){
            event.setExpToDrop(0);
        }
    }

}
