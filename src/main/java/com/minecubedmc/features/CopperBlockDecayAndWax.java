package com.minecubedmc.features;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class CopperBlockDecayAndWax implements Listener {

    @EventHandler
    public void onCopperDecay(final BlockFormEvent event){
        final Material material = event.getBlock().getType();
//        if (material.toString().contains("COPPER")){
//            e.setCancelled(true);
//        }
        if (material.getKey().toString().contains("copper")){
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onCopperWax(final PlayerInteractEvent e){
        //Ignore left click
        if (e.getAction().isLeftClick()){
            return;
        }
    
        final Block block = e.getClickedBlock();
        if (block == null){
            return;
        }
    
        final Material blockType = block.getType();
        final Material itemType = e.getMaterial();

        //If block formed contains "copper" cancel the event
        if (blockType.getKey().toString().contains("copper") && itemType.equals(Material.HONEYCOMB)){
            e.setCancelled(true);
        }
    }

}
