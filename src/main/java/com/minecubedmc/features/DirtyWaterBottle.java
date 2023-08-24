package com.minecubedmc.features;

import com.minecubedmc.util.Cache;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class DirtyWaterBottle implements Listener {
    
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        //Check if it is not in offhand
        if (event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        //Check if it is right click action
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR){
            return;
        }

        //If raycastBlock is null ignore
        final Player player = event.getPlayer();
        final Block raycastBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
        if (raycastBlock == null) {
            return;
        }

        //Ignore if the player didn't right-click on water
        if (!isWaterBlock(raycastBlock)) {
            return;
        }

        //If e item is null or isn't glass bottle return
        final ItemStack item = event.getItem();
        if (item == null ) {
            return;
        }
    
        final PlayerInventory inventory = player.getInventory();
        
        if (item.getType().equals(Material.GLASS_BOTTLE)){
            event.setCancelled(true);

            //Subtract glass bottle from main hand
            inventory.getItemInMainHand().subtract(1);

//            if (isSaltyBiome(raycastBlock.getBiome())){
//                if (inventory.addItem(saltyBottle).size() > 0) {
//                    player.getWorld().dropItem(player.getLocation(), saltyBottle);
//
//                }
//            } else {
                if (inventory.addItem(Cache.getCustomItem("minecubed:water_bottle")).size() > 0) {
                    player.getWorld().dropItem(player.getLocation(), Cache.getCustomItem("minecubed:water_bottle"));

                }
//            }
        }
//        else if (item.getType().equals(Material.BUCKET)) {
//
//            //Subtract bucket bottle from main hand
//            inventory.getItemInMainHand().subtract(1);
//
//            if (isSaltyBiome(raycastBlock.getBiome())){
//                if (inventory.addItem(saltyBottle).size() > 0) {
//                    player.getWorld().dropItem(player.getLocation(), saltyBottle);
//                    e.setCancelled(true);
//                }
//            }
//        }
    }

    private boolean isWaterBlock(Block block) {
        final BlockData data = block.getBlockData();

        if (block.getType().equals(Material.WATER) ||
                block.getType().equals(Material.KELP) ||
                block.getType().equals(Material.KELP_PLANT) ||
                block.getType() == Material.SEAGRASS ||
                block.getType() == Material.TALL_SEAGRASS
        ) {
            return true;
        }
        //If block is waterloggable and is waterlogged return true;
        return data instanceof Waterlogged && ((Waterlogged) data).isWaterlogged();
    }

    private boolean isSaltyBiome(final Biome biome) {
        return biome.equals(Biome.FROZEN_OCEAN)
                || biome.equals(Biome.DEEP_FROZEN_OCEAN)
                || biome.equals(Biome.WARM_OCEAN)
                || biome.equals(Biome.OCEAN)
                || biome.equals(Biome.DEEP_OCEAN)
                || biome.equals(Biome.DEEP_LUKEWARM_OCEAN)
                || biome.equals(Biome.LUKEWARM_OCEAN)
                || biome.equals(Biome.BEACH)
                || biome.equals(Biome.SNOWY_BEACH)
                || biome.equals(Biome.STONY_SHORE);
    }
}
