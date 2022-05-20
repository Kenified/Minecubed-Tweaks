package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
        Ageable ageable = (Ageable) eventBlock.getBlockData();
        String season = plugin.getSapi().getSeason(eventBlock.getWorld()).toString();

        if (eventBlock.getType().equals( Material.CARROTS )){
            //Check if its optimal season
            if (!season.equals("Summer")){
                return;
            }

            //Check if the crop is fully grown
            if (ageable.getAge() != 6){
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > 50 ){
                e.setCancelled(true);
                CustomBlock giantCarrot = CustomBlock.getInstance("minecubed:giant_carrot");
                giantCarrot.place(eventBlock.getLocation());
            }
        }

        if (eventBlock.getType().equals( Material.POTATOES )){
            //Check if its optimal season
            if (!season.equals("Summer")){
                return;
            }

            //Check if the crop is fully grown
            if (ageable.getAge() != 6){
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > 50 ){
                e.setCancelled(true);
                CustomBlock giantPotato = CustomBlock.getInstance("minecubed:giant_potato");
                giantPotato.place(eventBlock.getLocation());
            }
        }

        if (eventBlock.getType().equals( Material.BEETROOTS )){
            //Check if its optimal season
            if (!season.equals("Summer")){
                return;
            }

            //Check if the crop is fully grown
            if (ageable.getAge() != 2){
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > 50 ){
                e.setCancelled(true);
                CustomBlock giantPotato = CustomBlock.getInstance("minecubed:giant_tomato");
                giantPotato.place(eventBlock.getLocation());
            }
        }

        if (eventBlock.getType().equals( Material.WHEAT )){
            //Check if its optimal season
            if (!season.equals("Summer")){
                return;
            }

            //Check if the crop is fully grown
            if (ageable.getAge() != 2){
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > 50 ){
                e.setCancelled(true);
                CustomBlock giantPotato = CustomBlock.getInstance("minecubed:giant_tomato");
                giantPotato.place(eventBlock.getLocation());
            }
        }

        if (eventBlock.getType().equals( Material.COCOA )){
            //Check if its optimal season
            if (!season.equals("Summer")){
                return;
            }

            //Check if the crop is fully grown
            if (ageable.getAge() != 1){
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > 50 ){
                Directional directional = (Directional) eventBlock.getBlockData();
                BlockFace facing = directional.getFacing();
                e.setCancelled(true);
                CustomBlock giantCocoa = CustomBlock.getInstance("minecubed:giant_cocoa");

                switch (facing){
                    case NORTH:
                        giantCocoa = CustomBlock.getInstance("minecubed:giant_cocoa_north");
                        break;
                    case EAST:
                        giantCocoa = CustomBlock.getInstance("minecubed:giant_cocoa_east");
                        break;
                    case SOUTH:
                        giantCocoa = CustomBlock.getInstance("minecubed:giant_cocoa_south");
                        break;
                    case WEST:
                        giantCocoa = CustomBlock.getInstance("minecubed:giant_cocoa_west");
                        break;
                }
                giantCocoa.place(eventBlock.getLocation());
            }
        }
    }
}
