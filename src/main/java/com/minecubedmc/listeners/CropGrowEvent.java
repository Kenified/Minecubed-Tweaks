package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Random;

public class CropGrowEvent implements Listener {

    Tweaks plugin;

    public CropGrowEvent (Tweaks plugin){    this.plugin = plugin;  }

    @EventHandler
    public void onCropGrow(BlockGrowEvent e){
        Block eventBlock = e.getBlock();
        Material material = eventBlock.getType();
        Material newStateMaterial = e.getNewState().getBlockData().getMaterial();


        //Block melon and pumpkin blocks from growing from stems
        if (newStateMaterial.equals(Material.PUMPKIN) || newStateMaterial.equals(Material.MELON) ){
            e.setCancelled(true);
            return;
        }

        switch (material){
            case CARROTS:
                handleCrop(eventBlock, "Spring", "minecubed:giant_carrot", 50, e);
                break;
//            case SWEET_BERRY_BUSH:
//                handleCrop(eventBlock, "Spring", "minecubed:giant_strawberry", 50, e);
//                break;
//            case WHEAT:
//                handleCrop(eventBlock, "Summer", "minecubed:giant_wheat", 50, e);
//                break;
            case BEETROOTS:
                handleCrop(eventBlock, "Summer", "minecubed:giant_tomato", 50, e);
                break;
            case MELON_STEM:
                handleCrop(eventBlock, "Summer", "minecubed:giant_melon", 50, e);
                break;
            case POTATOES:
                handleCrop(eventBlock, "Fall", "minecubed:giant_potato", 50, e);
                break;
            case PUMPKIN_STEM:
                handleCrop(eventBlock, "Fall", "minecubed:giant_pumpkin", 50, e);
                break;
            case COCOA:
                handleDirectionalCrop(eventBlock, "Fall", "minecubed:giant_cocoa", 50, e);
                break;
//            case NETHER_WART:
//                handleCrop(eventBlock, "Any", "minecubed:giant_wart", 50, e);
//                break;
        }
    }

    private void handleCrop(Block crop, String growthSeason, String customBlockID, int giantChance, BlockGrowEvent event){
        Ageable ageable = (Ageable) crop.getBlockData();
        String currentSeason = plugin.getSapi().getSeason(crop.getWorld()).toString();

        //Check if current season matches the optimal season, if growth season is "Any", skip check (for nether and end)
        if (growthSeason.equals("Any")){ /* Empty skip season check */ }
        else if (!growthSeason.equals(currentSeason)){
            return;
        }

        //Check if the crop is fully grown
        if (ageable.getAge() != ageable.getMaximumAge() - 1 ){
            return;
        }

        //Chance for crop to be giant
        if (new Random().nextInt(100) > (100 - giantChance) ){
            CustomBlock giantCarrot = CustomBlock.getInstance(customBlockID);

            event.setCancelled(true);
            giantCarrot.place(crop.getLocation());
        }
    }

    private void handleDirectionalCrop(Block crop, String growthSeason, String customBlockID, int giantChance, BlockGrowEvent event){
        Ageable ageable = (Ageable) crop.getBlockData();
        String currentSeason = plugin.getSapi().getSeason(crop.getWorld()).toString();

        //Check if current season matches the optimal season, if growth season is "Any", skip check (for nether and end)
        if (growthSeason.equals("Any")){ /* Empty skip season check */ }
        else if (!growthSeason.equals(currentSeason)){
            return;
        }

        //Check if the crop is fully grown
        if (ageable.getAge() != ageable.getMaximumAge() - 1 ){
            return;
        }

        //Chance for crop to be giant
        if (new Random().nextInt(100) > (100 - giantChance) ){
            Directional directional = (Directional) crop.getBlockData();
            String facing = directional.getFacing().toString().toLowerCase();
            CustomBlock giantCocoa = CustomBlock.getInstance(customBlockID.concat("_").concat(facing));

            event.setCancelled(true);
            giantCocoa.place(crop.getLocation());

        }
    }
}

