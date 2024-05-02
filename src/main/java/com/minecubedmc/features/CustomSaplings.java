package com.minecubedmc.features;

import com.minecubedmc.util.Cache;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Logger;

public class CustomSaplings implements Listener {

    @EventHandler
    public void onCustomSaplingPlace(BlockPlaceEvent event){
        if (event.isCancelled()){
            return;
        }

        Block block = event.getBlockPlaced();
        ItemStack eventItem = event.getItemInHand();

        if (!block.getType().equals(Material.OAK_SAPLING)) {
            return;
        }

        if (eventItem.getType().equals(Material.AIR)) {
            return;
        }

        // Red maple
        if (eventItem.isSimilar(Cache.getCustomItem("minecubed:red_maple_seed"))) {
            handlePlacement(block, Material.ACACIA_SAPLING, eventItem, event);
        }
        // Orange maple
        else if (eventItem.isSimilar(Cache.getCustomItem("minecubed:orange_maple_seed"))) {
            handlePlacement(block, Material.JUNGLE_SAPLING, eventItem, event);
        }
        // Yellow maple
        else if (eventItem.isSimilar(Cache.getCustomItem("minecubed:yellow_maple_seed"))) {
            handlePlacement(block, Material.OAK_SAPLING, eventItem, event);
        }
        // Jacaranda
        else if (eventItem.isSimilar(Cache.getCustomItem("minecubed:jacaranda_seed"))) {
            handlePlacement(block, Material.BIRCH_SAPLING, eventItem, event);
        }

        else if (eventItem.isSimilar(Cache.getCustomItem("minecubed:fir_cone"))) {
            handlePlacement(block, Material.SPRUCE_SAPLING, eventItem, event);
        }

    }

    public void handlePlacement(Block block, Material saplingType, ItemStack eventItem, BlockPlaceEvent event) {
        block.setType(saplingType);
        Sapling saplingData = (Sapling) block.getBlockData();
        saplingData.setStage(1);
        block.setBlockData(saplingData);

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        eventItem.subtract();
    }

    @EventHandler
    public void onCustomSaplingBreak(BlockBreakEvent event){
        if (event.isCancelled()){
            return;
        }

        Block block = event.getBlock();
        if (!(block.getBlockData() instanceof Sapling sapling)){
            return;
        }

        if (sapling.getStage() < 1){
            return;
        }

        Location location = block.getLocation();
        World world = location.getWorld();
        switch (block.getType()){
            case ACACIA_SAPLING -> world.dropItemNaturally(location, Cache.getCustomItem("minecubed:red_maple_seed"));
            case JUNGLE_SAPLING -> world.dropItemNaturally(location, Cache.getCustomItem("minecubed:orange_maple_seed"));
            case OAK_SAPLING -> world.dropItemNaturally(location, Cache.getCustomItem("minecubed:yellow_maple_seed"));
            case BIRCH_SAPLING -> world.dropItemNaturally(location, Cache.getCustomItem("minecubed:jacaranda_seed"));
            case SPRUCE_SAPLING -> world.dropItemNaturally(location, Cache.getCustomItem("minecubed:fir_cone"));
            default -> {
                return;
            }
        }

        event.setDropItems(false);
    }
}
