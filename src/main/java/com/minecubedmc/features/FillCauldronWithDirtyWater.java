package com.minecubedmc.features;

import com.minecubedmc.items.CustomItems;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FillCauldronWithDirtyWater implements Listener {
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
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

        if (item ==  null || !item.isSimilar(CustomItems.getCustomItem("minecubed:water_bottle"))) {
            return;
        }

        //If block is null ignore it
        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }
    
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();

        if (block.getType().equals(Material.WATER_CAULDRON)){
            Levelled cauldron = (Levelled) event.getClickedBlock().getBlockData();

            //If its full ignore
            if (cauldron.getLevel() == 3) {
                return;
            }
            else{
                //Return Glass Bottle
                inventory.getItemInMainHand().subtract(1);
                if (inventory.addItem(new ItemStack(Material.GLASS_BOTTLE)).size() > 0) {
                    player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GLASS_BOTTLE));
                }

                cauldron.setLevel(cauldron.getLevel() + 1);
                event.getClickedBlock().setBlockData(cauldron);
            }
        }
        else if (block.getType().equals(Material.CAULDRON)){
            event.getClickedBlock().setType(Material.WATER_CAULDRON);
            final Levelled cauldron = (Levelled) event.getClickedBlock().getBlockData();

            //Return Glass Bottle
            inventory.getItemInMainHand().subtract(1);
            if (inventory.addItem(new ItemStack(Material.GLASS_BOTTLE)).size() > 0) {
                player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GLASS_BOTTLE));
            }

            cauldron.setLevel(1);
            event.getClickedBlock().setBlockData(cauldron);
        }

    }

}
