package com.minecubedmc.features;

import com.minecubedmc.items.CustomItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class ExtraFruitDrops implements Listener {

    @EventHandler
    public void onLeafDecay(final LeavesDecayEvent event){
        final Location location = event.getBlock().getLocation();
        final Material leaf = event.getBlock().getType();
        
        switch (leaf) {
            case OAK_LEAVES, DARK_OAK_LEAVES -> {
                getDropItem(location, Material.APPLE, 10);
            }
            case BIRCH_LEAVES -> {
                getDropItem(location, "minecubed:cherry", 10);
            }
            case MANGROVE_LEAVES -> {
                getDropItem(location, "minecubed:pitaya", 8);
            }
            case JUNGLE_LEAVES -> {
                getDropItem(location, "minecubed:mango", 10);
            }
            case ACACIA_LEAVES -> {
                getDropItem(location, "minecubed:banana", 10);
            }
            case FLOWERING_AZALEA_LEAVES, AZALEA_LEAVES -> {
                getDropItem(location, "minecubed:starfruit", 10);
            }
            default -> {
            }
        }
    }
    
    private void getDropItem(Location location, Object item, int chance){
        if ( new Random().nextInt(100) + 1 > chance ) {
            return;
        }
    
        ItemStack itemStack;
        
        if (item instanceof Material) {
            itemStack = new ItemStack((Material) item);
        } else if (item instanceof String) {
            itemStack = CustomItems.getCustomItem((String) item);
        } else {
            throw new IllegalArgumentException("Item parameter must be a Material or String");
        }
        
        World world = location.getWorld();
        world.dropItemNaturally(location, itemStack);
    }
    
//    private void getDropItem(Location location, String customItemID, int chance){
//        ItemStack customItem = CustomItems.getCustomItem(customItemID);
//        dropItem(location, customItem, chance);
//    }
//
//    private void dropItem(Location location, ItemStack item, int chance) {
//        if ( new Random().nextInt(100) + 1 > chance ) {
//            return;
//        }
//
//        World world = location.getWorld();
//        world.dropItemNaturally(location, item);
//    }
    
}

