package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Material;
import org.bukkit.World;
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
                handleCrop(eventBlock, "Spring", "world", "minecubed:giant_carrot", 50, false, e);
                break;
            case SWEET_BERRY_BUSH:
                handleCrop(eventBlock, "Spring", "world", "minecubed:giant_strawberry", 50, false, e);
                break;
            case WHEAT:
                handleCrop(eventBlock, "Summer", "world", "minecubed:giant_wheat", 50, false, e);
                break;
            case BEETROOTS:
                handleCrop(eventBlock, "Summer", "world", "minecubed:giant_tomato", 50, false, e);
                break;
            case MELON_STEM:
                handleCrop(eventBlock, "Summer", "world", "minecubed:giant_melon", 50, false, e);
                break;
            case POTATOES:
                handleCrop(eventBlock, "Fall", "world", "minecubed:giant_potato", 50, false, e);
                break;
            case PUMPKIN_STEM:
                handleCrop(eventBlock, "Fall", "world", "minecubed:giant_pumpkin", 50, false, e);
                break;
            case COCOA:
                handleCrop(eventBlock, "Fall", "world", "minecubed:giant_cocoa", 50, true, e);
                break;
            case NETHER_WART:
                handleCrop(eventBlock, "Any", "world_nether", "minecubed:giant_wart", 50, false, e);
                break;
        }
    }

    private void handleCrop(Block crop, String fertileSeason, String fertileWorld, String customBlockID, int giantChance, Boolean isDirectional,BlockGrowEvent event) {
        World world = crop.getWorld();
        Ageable ageable = (Ageable) crop.getBlockData();
        String currentSeason = plugin.getSapi().getSeason(world).toString();
        CustomBlock giantCrop;

        //Check if the crop is in fertile world
        if (!fertileWorld.equals(world.toString())){
            return;
        }

        //Check if current season matches the fertile season, or if it's allowed to grow in any season
        if (fertileSeason.equals("Any") || fertileSeason.equals(currentSeason)) {

            //Check if the crop is fully grown
            if (ageable.getAge() != ageable.getMaximumAge() - 1) {
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > (100 - giantChance)) {
                CustomBlock giantCarrot = CustomBlock.getInstance(customBlockID);
                if(isDirectional){
                    Directional directional = (Directional) crop.getBlockData();
                    String facing = directional.getFacing().toString().toLowerCase();
                    giantCrop = CustomBlock.getInstance(customBlockID.concat("_").concat(facing));
                }
                else{
                    giantCrop = CustomBlock.getInstance(customBlockID);
                }
                event.setCancelled(true);
                giantCrop.place(crop.getLocation());
            }
        }
    }

}

