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

public class FillCauldronWithDirtyWater implements Listener {

    private final Tweaks plugin;

    public FillCauldronWithDirtyWater(Tweaks plugin) {
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
        ItemStack dirty_water = ItemStack.deserializeBytes(CustomStack.getInstance("minecubed:water_bottle").getItemStack().serializeAsBytes()); //For some reason??

        if (item == null || !item.isSimilar(dirty_water)) {
            return;
        }

        //If block is null ignore it
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();

        if (block.getType().equals(Material.WATER_CAULDRON)){
            Levelled cauldron = (Levelled) e.getClickedBlock().getBlockData();

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
                e.getClickedBlock().setBlockData(cauldron);
            }
        }
        else if (block.getType().equals(Material.CAULDRON)){
            e.getClickedBlock().setType(Material.WATER_CAULDRON);
            Levelled cauldron = (Levelled) e.getClickedBlock().getBlockData();

            //Return Glass Bottle
            inventory.getItemInMainHand().subtract(1);
            if (inventory.addItem(new ItemStack(Material.GLASS_BOTTLE)).size() > 0) {
                player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.GLASS_BOTTLE));
            }

            cauldron.setLevel(1);
            e.getClickedBlock().setBlockData(cauldron);
        }

    }

}
