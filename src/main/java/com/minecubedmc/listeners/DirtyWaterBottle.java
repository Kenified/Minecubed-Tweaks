package com.minecubedmc.listeners;


import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
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

import java.util.Objects;

public class DirtyWaterBottle implements Listener {

    private final Tweaks plugin;

    public DirtyWaterBottle(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //Check if it is not in offhand
        if (e.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }

        //Check if it is right click action
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK && e.getAction() != Action.RIGHT_CLICK_AIR){
            return;
        }

        //If e item is null or isn't glass bottle return
        ItemStack item = e.getItem();
        if (item == null || !item.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }

        //If raycastBlock is null ignore
        Player player = e.getPlayer();
        Block raycastBlock = player.getTargetBlockExact(5, FluidCollisionMode.ALWAYS);
        if (raycastBlock == null) {
            return;
        }

        //Ignore if the player didn't right-click on water
        if (!isWaterBlock(raycastBlock)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        ItemStack customItem = CustomStack.getInstance("minecubed:water_bottle").getItemStack();
        e.setCancelled(true);

        //Subtract glass bottle from main hand
        inventory.getItemInMainHand().subtract(1);

        if (inventory.addItem(customItem).size() > 0) {
            player.getWorld().dropItem(player.getLocation(), customItem);

        }

    }

    private boolean isWaterBlock(Block block) {
        BlockData data = block.getBlockData();

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


}
