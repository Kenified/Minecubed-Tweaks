package com.minecubedmc.features;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

public class CustomBlockSilktouch implements Listener {

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event){
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }
    
        final Block block = event.getBlock();
        final Material type = block.getType();

        if (!(type.equals(Material.DEAD_HORN_CORAL) || type.equals(Material.DEAD_FIRE_CORAL))){
            return;
        }
    
        final Material toolType = event.getPlayer().getInventory().getItemInMainHand().getType();

        if (!toolType.equals(Material.SHEARS)){
            return;
        }
    
        final World world = block.getWorld();
        final Location location = block.getLocation();

//        event.setCancelled(true);
//        block.setType(Material.AIR);
        event.setDropItems(false);
        world.dropItemNaturally(location, new ItemStack(type));
    }
}
