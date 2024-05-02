package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import com.minecubedmc.util.SerializableLocation;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;

public class CustomLeavesDrops implements Listener {

    Tweaks plugin;

    public CustomLeavesDrops(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeafDecay(final LeavesDecayEvent event){
        if (event.isCancelled()){
            return;
        }

        final Block block = event.getBlock();
        SerializableLocation serializableLocation = new SerializableLocation(block.getLocation());
        if (CustomLeavesPersistent.persistentLeavesSet.contains(serializableLocation)) {
            event.setCancelled(true);
            return;
        }

        if (!block.getType().toString().toLowerCase().contains("leaves")){
          return;
        }

        // Cancel the event because of item drops
        event.setCancelled(true);

        handleLeafBreakOrDecay(block);

        block.setType(Material.AIR);
   }

    @EventHandler
    public void onLeafBreak (final BlockBreakEvent event) {
       if (event.isCancelled()){
           return;
       }

        final Block block = event.getBlock();

        if (!block.getType().toString().toLowerCase().contains("leaves")){
           return;
        }

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack item = inventory.getItemInMainHand();
        // If the player is holding shears
        if (item.getType().equals(Material.SHEARS)){
            // If leaves are persistent (custom), don't drop anything
            if (((Leaves) block.getBlockData()).isPersistent()){
                event.setDropItems(false);
                return;
            }
            // Otherwise drop the leaves
            event.setDropItems(false);
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(block.getType()));
        }
        // If player is not holding shears, handle leaf as break
        else{
            event.setDropItems(false);
            handleLeafBreakOrDecay(block);
        }
   }

    private void handleLeafBreakOrDecay(Block block){
        final Material leaf = block.getType();
        final boolean isPersistent = ((Leaves) block.getBlockData()).isPersistent();

        final Location location = block.getLocation();
        World world = location.getWorld();

        Random random = new Random();

        switch (leaf) {
           case OAK_LEAVES -> {
               // Oak
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.OAK_SAPLING));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, new ItemStack(Material.APPLE));
                   }
               }
               // EMPTY
               else {

               }
           }
           case SPRUCE_LEAVES -> {
               // Spruce
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.SPRUCE_SAPLING));
                   }

               }
               // Fir
               else {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:fir_cone"));
                   }
               }
           }
           case BIRCH_LEAVES -> {
               // Aspen
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.BIRCH_SAPLING));
                   }
               }
               // EMPTY
               else {

               }
           }
           case JUNGLE_LEAVES -> {
               // Jungle
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.025f){
                       world.dropItemNaturally(location, new ItemStack(Material.JUNGLE_SAPLING));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:mango"));
                   }
               }
               // Orange Maple
               else {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:orange_maple_seed"));
                   }
               }
           }
           case ACACIA_LEAVES -> {
               // Mahogany
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.ACACIA_SAPLING));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:banana"));
                   }
               }
               // Red Maple
               else {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:red_maple_seed"));
                   }
               }
           }
           case DARK_OAK_LEAVES -> {
               // Dark Oak
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.DARK_OAK_SAPLING));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, new ItemStack(Material.APPLE));
                   }
               }
               // EMPTY
               else {

               }
           }
           case MANGROVE_LEAVES -> {
               // Mangrove
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:pitaya"));
                   }
               }
               // Yellow Maple
               else {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:yellow_maple_seed"));
                   }
               }
           }
           case CHERRY_LEAVES -> {
               // Cherry
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.CHERRY_SAPLING));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:cherry"));
                   }
               }
               // EMPTY
               else {

               }
           }
           case AZALEA_LEAVES -> {
               // Azalea
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.AZALEA));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:starfruit"));
                   }
               }
               // EMPTY
               else {

               }
           }
           case FLOWERING_AZALEA_LEAVES -> {
               // Flowering Azalea
               if (!isPersistent) {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, new ItemStack(Material.FLOWERING_AZALEA));
                   }

                   if (random.nextFloat() <= 0.10f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:starfruit"));
                   }
               }
               // Jacaranda
               else {
                   if (random.nextFloat() <= 0.02f){
                       ItemStack stickItem = new ItemStack(Material.STICK);
                       stickItem.setAmount( random.nextInt(2) + 1 );
                       world.dropItemNaturally(location, stickItem);
                   }

                   if (random.nextFloat() <= 0.05f){
                       world.dropItemNaturally(location, Cache.getCustomItem("minecubed:jacaranda_seed"));
                   }
               }
           }
           default -> { }
        }

//        // Replace the block with air
//        block.setType(Material.AIR);
   }
}

