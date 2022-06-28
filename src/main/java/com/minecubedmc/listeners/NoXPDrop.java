package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class NoXPDrop implements Listener {

    private final Tweaks plugin;

    public NoXPDrop(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void blockBreakListener(BlockBreakEvent e){
        Block block = e.getBlock();

        if (block.getType().equals(Material.DEEPSLATE_EMERALD_ORE)){
            e.setExpToDrop(0);
        }
    }

}
