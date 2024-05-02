package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.inventory.ItemStack;

public class SelfPlantingSaplings implements Listener {

    Tweaks plugin;

    public SelfPlantingSaplings(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSaplingDespawn(ItemDespawnEvent event){
        Item eventSapling = event.getEntity();
        ItemStack eventItemStack = eventSapling.getItemStack();
        Block eventBlockAt = eventSapling.getWorld().getBlockAt(eventSapling.getLocation());

        // Check if the block is replaceable
        if (!Tag.REPLACEABLE.isTagged(eventBlockAt.getType())) {
            return;
        }

        // Check if the block below is dirt
        if (!Tag.DIRT.isTagged(eventBlockAt.getRelative(BlockFace.DOWN).getType())){
            return;
        }

        if (eventItemStack.isSimilar(Cache.getCustomItem("minecubed:red_maple_seed"))) {
            handlePlacement(eventBlockAt, Material.ACACIA_SAPLING);
        }
        else if (eventItemStack.isSimilar(Cache.getCustomItem("minecubed:orange_maple_seed"))) {
            handlePlacement(eventBlockAt, Material.JUNGLE_SAPLING);
        }
        else if (eventItemStack.isSimilar(Cache.getCustomItem("minecubed:yellow_maple_seed"))) {
            handlePlacement(eventBlockAt, Material.OAK_SAPLING);
        }
        else if (eventItemStack.isSimilar(Cache.getCustomItem("minecubed:jacaranda_seed"))) {
            handlePlacement(eventBlockAt, Material.BIRCH_SAPLING);
        }
        else if (eventItemStack.isSimilar(Cache.getCustomItem("minecubed:fir_cone"))){
            handlePlacement(eventBlockAt, Material.SPRUCE_SAPLING);
        }
        else if (eventItemStack.getType().toString().toLowerCase().contains("sapling")){
            eventBlockAt.setType(eventItemStack.getType());
        }

    }

    public void handlePlacement(Block eventblock, Material saplingType) {
        // Set the block to the sapling type
        eventblock.setType(saplingType);
        // Get the sapling data
        Sapling saplingData = (Sapling) eventblock.getBlockData();
        // Set the sapling stage to 1
        saplingData.setStage(1);
        // Update the block data with the sapling data
        eventblock.setBlockData(saplingData);
    }
}
