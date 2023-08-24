package com.minecubedmc.features;

import com.minecubedmc.util.Cache;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Lightable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class WaterCauldronPurify implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        //Check if it is not in offhand
        if (event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        //Check if it is right click action
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        //If event item is null or isn't glass bottle return
        final ItemStack item = event.getItem();
        if (item == null || !item.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }

        //If block is null or not cauldron ignore it
        final Block block = event.getClickedBlock();
        if (block != null && !block.getType().equals(Material.WATER_CAULDRON)) {
            return;
        }

        //If cauldron is empty return
        final Levelled cauldron = (Levelled) event.getClickedBlock().getBlockData();
        if (cauldron.getLevel() == 0) {
            return;
        }
    
        final Player player = event.getPlayer();
        final Block heatSource = block.getRelative(BlockFace.DOWN);
        final PlayerInventory inventory = player.getInventory();
        event.setCancelled(true);

        //Subtract glass bottle from main hand
        inventory.getItemInMainHand().subtract(1);

        //Check if heat source is a lightable block
        if(heatSource.getType().equals(Material.CAMPFIRE)){
            Lightable heatSourceData = (Lightable) heatSource.getBlockData();

            //If heat source is lit give custom item else give dirty water bottle
            if (heatSourceData.isLit()) {
                //If inventory is full drop the custom item
                if (inventory.addItem(Cache.getCustomItem("minecubed:purified_water")).size() > 0) {
                    player.getWorld().dropItem(player.getLocation(), Cache.getCustomItem("minecubed:purified_water"));
                }
            }
            else{
                if (inventory.addItem(Cache.getCustomItem("minecubed:water_bottle")).size() > 0) {
                    //If inventory is full drop the custom item
                    player.getWorld().dropItem(player.getLocation(), Cache.getCustomItem("minecubed:water_bottle"));
                }
            }
        }
        //If its not campfire block then give dirty water
        else {
            if (inventory.addItem(Cache.getCustomItem("minecubed:water_bottle")).size() > 0) {
                //If inventory is full drop the custom item
                player.getWorld().dropItem(player.getLocation(), Cache.getCustomItem("minecubed:water_bottle"));
            }
        }

        if (cauldron.getLevel() > 1){
            cauldron.setLevel(cauldron.getLevel() - 1);
            event.getClickedBlock().setBlockData(cauldron);
        }
        else {
            event.getClickedBlock().setType(Material.CAULDRON);
        }
    }

}
