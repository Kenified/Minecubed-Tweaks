package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
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

public class PurifiedWaterCauldron implements Listener {

    private final Tweaks plugin;

    public PurifiedWaterCauldron(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //Check if it is not in offhand
        if (e.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        //Check if it is right click action
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK){
            return;
        }

        //If e item is null or isn't glass bottle return
        ItemStack item = e.getItem();
        if (item == null || !item.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }

        //If block is null or not cauldron ignore it
        Block block = e.getClickedBlock();
        if (block != null && !block.getType().equals(Material.WATER_CAULDRON)) {
            return;
        }

        //If cauldron is empty return
        Levelled cauldron = (Levelled) e.getClickedBlock().getBlockData();
        if (cauldron.getLevel() == 0) {
            return;
        }

        Player player = e.getPlayer();
        Block heatSource = block.getRelative(BlockFace.DOWN);
        PlayerInventory inventory = player.getInventory();
        ItemStack dirty_water = CustomStack.getInstance("minecubed:water_bottle").getItemStack();
        ItemStack purified_water = CustomStack.getInstance("minecubed:purified_water").getItemStack();
        e.setCancelled(true);

        //Subtract glass bottle from main hand
        inventory.getItemInMainHand().subtract(1);

        //Check if heat source is a lightable block
        if(heatSource.getType().equals(Material.CAMPFIRE)){
            Lightable heatSourceData = (Lightable) heatSource.getBlockData();

            //If heat source is lit give custom item else give dirty water bottle
            if (heatSourceData.isLit()) {
                //If inventory is full drop the custom item
                if (inventory.addItem(purified_water).size() > 0) {
                    player.getWorld().dropItem(player.getLocation(), purified_water);
                }
            }
            else{
                if (inventory.addItem(dirty_water).size() > 0) {
                    //If inventory is full drop the custom item
                    player.getWorld().dropItem(player.getLocation(), dirty_water);
                }
            }
        }
        //If its not campfire block then give dirty water
        else {
            if (inventory.addItem(dirty_water).size() > 0) {
                //If inventory is full drop the custom item
                player.getWorld().dropItem(player.getLocation(), dirty_water);
            }
        }

        if (cauldron.getLevel() > 1){
            cauldron.setLevel(cauldron.getLevel() - 1);
            e.getClickedBlock().setBlockData(cauldron);
        }
        else {
            e.getClickedBlock().setType(Material.CAULDRON);
        }
    }

}
