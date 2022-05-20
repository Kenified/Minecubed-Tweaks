package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
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

        if (eventBlock.getType().equals( Material.CARROT )){
            //Check if its optimal season
            if (ageable.getAge() != 7){
                return;
            }

            //Check if the crop is fully grown
            if (!season.equals("SUMMER")){
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > 50 ){
                e.setCancelled(true);
                CustomBlock giantCarrot = CustomBlock.getInstance("minecubed:giant_carrot");
                giantCarrot.place(eventBlock.getLocation());
                plugin.getLogger().info("Giant carrot grew!");
            }

            plugin.getLogger().info("Regular carrot grew!");

        }

    }
}
