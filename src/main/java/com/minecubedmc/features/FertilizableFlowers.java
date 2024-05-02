package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.items.CustomTripwireBlock;
import com.minecubedmc.util.Cache;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;
import java.util.Random;

public class FertilizableFlowers implements Listener {
    
    Tweaks plugin;
    
    public FertilizableFlowers(Tweaks plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerRightClick(PlayerInteractEvent event){
        if (event.isCancelled()){
            return;
        }

        // Check if it is not in offhand
        if (event.getHand() == EquipmentSlot.OFF_HAND){
            return;
        }
    
        //Check if it is right click action
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        // If clicked block is null ignore
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        // Ignore if held item is not fertilizer
        PlayerInventory playerInventory = event.getPlayer().getInventory();
        if (playerInventory.getItemInMainHand().getType() != Material.BONE_MEAL) {
            return;
        }

        // Check if the player right-clicked on tripwire custom block
        if (clickedBlock.getType().equals(Material.TRIPWIRE)) {
            Arrays.stream(CustomTripwireBlockSystem.getCustomBlockList()).sequential().forEach(customBlockID -> {
                // Ignore if block is not custom
                if (!clickedBlock.getBlockData().equals(CustomTripwireBlockSystem.getCustomBlock(customBlockID).getBlockData())){
                    return;
                }

                // Ignore if custom block is not a flower
                customBlockID = customBlockID.toLowerCase();
                if(!customBlockID.contains("flower_cover") &&
                        !customBlockID.contains("torchflower") &&
                        !customBlockID.contains("searocket") &&
                        !customBlockID.contains("cotton_grass") &&
                        !customBlockID.contains("goldenrod") &&
                        !customBlockID.contains("cartwheel_flower")

                ){
                    return;
                }

                handleFertilizing(clickedBlock, event.getPlayer(), playerInventory, customBlockID);


            });
        }
        else if (clickedBlock.getType().equals(Material.DANDELION) ||
                clickedBlock.getType().equals(Material.POPPY) ||
                clickedBlock.getType().equals(Material.ALLIUM) ||
                clickedBlock.getType().equals(Material.AZURE_BLUET) ||
                clickedBlock.getType().equals(Material.RED_TULIP) ||
                clickedBlock.getType().equals(Material.ORANGE_TULIP) ||
                clickedBlock.getType().equals(Material.WHITE_TULIP) ||
                clickedBlock.getType().equals(Material.PINK_TULIP) ||
                clickedBlock.getType().equals(Material.OXEYE_DAISY) ||
                clickedBlock.getType().equals(Material.CORNFLOWER) ||
                clickedBlock.getType().equals(Material.LILY_OF_THE_VALLEY)

        ){
            handleFertilizing(clickedBlock, event.getPlayer(), playerInventory, new ItemStack(clickedBlock.getType()));
        }

    }

    private void handleFertilizing(Block clickedBlock, Player player, PlayerInventory playerInventory, String customBlockID){
        handleFertilizing(clickedBlock, player, playerInventory, Cache.getCustomItem(customBlockID));
    }

    private void handleFertilizing(Block clickedBlock, Player player, PlayerInventory playerInventory, ItemStack customBlock){
        Random random = new Random();
        World eventWorld = clickedBlock.getWorld();
        Location blockLocation = clickedBlock.getLocation();

        eventWorld.playSound(blockLocation, Sound.ITEM_BONE_MEAL_USE, SoundCategory.PLAYERS, 1, 1);

        // Drop custom item 35% of the time
        if(random.nextInt(100) < 35) {
            eventWorld.dropItemNaturally(blockLocation, customBlock);
        }

        // Spawn 10 particles emulating bone meal effect
        for (int i = 0; i < 10; i++) {
            Location particleLocation = blockLocation.clone();
            particleLocation.set(
                    blockLocation.getX() + random.nextFloat() * 0.5,
                    blockLocation.getY() + random.nextFloat() * 0.5,
                    blockLocation.getZ() + random.nextFloat() * 0.5
            );
            eventWorld.playEffect(particleLocation, Effect.VILLAGER_PLANT_GROW, 1, 24);
        }

        // Ignore if player is in creative
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        // Remove one bone meal
        playerInventory.getItemInMainHand().subtract();
    }
}
