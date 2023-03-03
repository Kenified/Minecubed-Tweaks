package com.minecubedmc.features;

import com.archyx.aureliumskills.api.AureliumAPI;
import com.archyx.aureliumskills.skills.Skills;
import com.archyx.aureliumskills.skills.farming.FarmingSource;
import com.archyx.aureliumskills.source.SourceManager;
import com.minecubedmc.Tweaks;
import com.minecubedmc.items.CustomItems;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;


public class CustomCrops implements Listener {
    
    @EventHandler
    public void onCropGrow(final BlockGrowEvent event) {
        final Block crop = event.getBlock();
        final Material cropType = crop.getType();
    
        final Collection<Player> players = event.getBlock().getLocation().getNearbyPlayers((crop.getWorld().getSimulationDistance() * 16) + 8 );
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.setCancelled(true);
            return;
        }

        if (cropType.equals(Material.CARROTS) ||
                cropType.equals(Material.WHEAT) ||
                cropType.equals(Material.POTATOES)
        ) {
            int cropAge = ((Ageable) crop.getBlockData()).getAge();
            if (cropAge ==  3) {
                event.setCancelled(true);
            }
        }else if (cropType.equals(Material.MELON_STEM)) {
            int cropAge = ((Ageable) crop.getBlockData()).getAge();
            if (cropAge ==  2){
                event.setCancelled(true);
                CustomItems.getCustomBlock("minecubed:grown_eggplant").place(crop.getLocation());
            }
        }else if (cropType.equals(Material.PUMPKIN_STEM)) {
            int cropAge = ((Ageable) crop.getBlockData()).getAge();
            if (cropAge ==  2){
                event.setCancelled(true);
                CustomItems.getCustomBlock("minecubed:grown_corn").place(crop.getLocation());
            }
        }
    }

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event){
//        Player player = event.getPlayer();
//        Block block = event.getBlockPlaced();
        final ItemStack item = event.getItemInHand();
    
        handleCrop(item, CustomItems.getCustomItem("minecubed:lettuce_seeds"), Material.CARROTS,0 , event);
        handleCrop(item, CustomItems.getCustomItem("minecubed:rice_seeds"), Material.WHEAT,0, event);
        handleCrop(item, CustomItems.getCustomItem("minecubed:onion_seeds"), Material.POTATOES,0, event);
        handleCrop(item, CustomItems.getCustomItem("minecubed:eggplant_seeds"), Material.MELON_STEM,0, event);
        handleCrop(item, CustomItems.getCustomItem("minecubed:corn_seeds"), Material.PUMPKIN_STEM,0, event);
        handleCrop(item, new ItemStack(Material.CARROT), Material.CARROTS,4 , event);
        handleCrop(item, new ItemStack(Material.WHEAT_SEEDS), Material.WHEAT,4, event);
        handleCrop(item, new ItemStack(Material.POTATO), Material.POTATOES,4, event);
        handleCrop(item, new ItemStack(Material.MELON_SEEDS), Material.MELON_STEM,4, event);
        handleCrop(item, new ItemStack(Material.PUMPKIN_SEEDS), Material.PUMPKIN_STEM,4, event);

    }
    
    private void handleCrop(final ItemStack item, final ItemStack placedSeed, final Material crop, int stage, final BlockPlaceEvent event){
        if (placedSeed.isSimilar(item)){
            //Set Age blockdata
            BlockData blockData = crop.createBlockData();
            Ageable ageable = (Ageable) blockData;
            ageable.setAge(stage);
    
    
            //Set the new block and newblockdata
            event.getBlockPlaced().setType(crop);
            event.getBlockPlaced().setBlockData(blockData);
    
            //Subtract one seed from hand
            item.subtract(1);
        }
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event){
        if (event.isCancelled()) return;

        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }
    
        final Material type = event.getBlock().getType();
        final Location location = event.getBlock().getLocation();
        int age;

        if (type.equals(Material.CARROTS)){
            age = ((Ageable) event.getBlock().getBlockData()).getAge();
            handleCropBreak(age, location, CustomItems.getCustomItem("minecubed:lettuce"), CustomItems.getCustomItem("minecubed:lettuce_seeds"), event);
        } else if (type.equals(Material.WHEAT)) {
            age = ((Ageable) event.getBlock().getBlockData()).getAge();
            handleCropBreak(age, location, CustomItems.getCustomItem("minecubed:rice"), CustomItems.getCustomItem("minecubed:rice_seeds"), event);
        } else if (type.equals(Material.POTATOES)) {
            age = ((Ageable) event.getBlock().getBlockData()).getAge();
            handleCropBreak(age, location, CustomItems.getCustomItem("minecubed:onion"), CustomItems.getCustomItem("minecubed:onion_seeds"), event);
        } else if (type.equals(Material.MELON_STEM)) {
            age = ((Ageable) event.getBlock().getBlockData()).getAge();
            handleCropBreak(age, location, CustomItems.getCustomItem("minecubed:eggplant"), CustomItems.getCustomItem("minecubed:eggplant_seeds"), event);
        } else if (type.equals(Material.PUMPKIN_STEM)) {
            age = ((Ageable) event.getBlock().getBlockData()).getAge();
            handleCropBreak(age, location, CustomItems.getCustomItem("minecubed:corn"), CustomItems.getCustomItem("minecubed:corn_seeds"), event);
        }
    }

    private void handleCropBreak(final int age, final Location location, final ItemStack crop, final ItemStack seeds, final BlockBreakEvent e){
        if (age < 3){
            seeds.setAmount(1);
            location.getWorld().dropItemNaturally(location, seeds);
        } else if (age ==  3) {
            seeds.setAmount(new Random().nextInt(2) + 1);
            location.getWorld().dropItemNaturally(location, seeds);
            location.getWorld().dropItemNaturally(location, crop);

            Block block = e.getBlock();
            Material type = block.getType();
            
            //Add Aurellium skills support
            double xpAmount = 0;
            SourceManager sourceManager = AureliumAPI.getPlugin().getSourceManager();
            if (type.equals(Material.CARROTS) || type.equals(Material.WHEAT) ){ //Lettuce && Rice
                xpAmount = sourceManager.getXp(FarmingSource.CARROT);
            } else if (type.equals(Material.POTATOES)) { // Onion
                xpAmount = sourceManager.getXp(FarmingSource.POTATO);
            } else if ( CustomItems.getCustomBlock("minecubed:grown_eggplant").getBaseBlockData() == block.getBlockData() || CustomItems.getCustomBlock("minecubed:grown_corn").getBaseBlockData() == block.getBlockData() ) { //Eggplant && Corn
                xpAmount = sourceManager.getXp(FarmingSource.BEETROOT);
            }
            AureliumAPI.addXp(e.getPlayer(), Skills.FARMING, xpAmount);
        }
        else {
            return;
        }
        e.setDropItems(false);
    }

    @EventHandler
    public void onBonemeal(final BlockFertilizeEvent event){
        final Block crop = event.getBlock();
        final Material cropType = crop.getType();
        int cropAge;
        int newCropAge;

        if (cropType.equals(Material.CARROTS) ||
                cropType.equals(Material.WHEAT) ||
                cropType.equals(Material.POTATOES)
        ){
            cropAge = ((Ageable) crop.getBlockData()).getAge();
            newCropAge = ((Ageable) event.getBlocks().get(0).getBlockData()).getAge();

            if (cropAge < 3 && newCropAge > 3) {

                event.setCancelled(true);
                //Set Age blockdata
                BlockData blockData = cropType.createBlockData();
                Ageable ageable = (Ageable) blockData;
                ageable.setAge(3);

                //Set the new block and newblockdata
                crop.setType(cropType);
                crop.setBlockData(blockData);
            }
            else if (cropAge == 3){
                event.setCancelled(true);
            }
        } else if (cropType.equals(Material.MELON_STEM)) {
            cropAge = ((Ageable) crop.getBlockData()).getAge();
            newCropAge = ((Ageable) event.getBlocks().get(0).getBlockData()).getAge();

            if ( (cropAge < 2 && newCropAge > 2) || cropAge ==  3 ) {
                // Set the new block
                event.setCancelled(true);
                CustomItems.getCustomBlock("minecubed:grown_eggplant").place(crop.getLocation());
            }
        } else if (cropType.equals(Material.PUMPKIN_STEM)) {
            cropAge = ((Ageable) crop.getBlockData()).getAge();
            newCropAge = ((Ageable) event.getBlocks().get(0).getBlockData()).getAge();

            if ( (cropAge < 2 && newCropAge > 2) || cropAge ==  3) {
                // Set the new block
                event.setCancelled(true);
                CustomItems.getCustomBlock("minecubed:grown_corn").place(crop.getLocation());
            }
        }
    }

    @EventHandler
    public void onBeeFertilize(final EntityChangeBlockEvent event){
        if (! (event.getEntity() instanceof Bee)){
            return;
        }

        Collection<Player> players = event.getBlock().getLocation().getNearbyPlayers((event.getBlock().getWorld().getSimulationDistance() * 16) + 8 );
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.setCancelled(true);
            return;
        }
    
        final Block crop = event.getBlock();
        final Material cropType = crop.getType();

        if (cropType.equals(Material.CARROTS) ||
                cropType.equals(Material.WHEAT) ||
                cropType.equals(Material.POTATOES)
        ){
            int cropAge = ((Ageable) crop.getBlockData()).getAge();
            if (cropAge == 3){
                event.setCancelled(true);
            }
        }
    }
}