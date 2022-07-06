package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomBlock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Random;

public class CropGrowEvent implements Listener {

    Tweaks plugin;

    public CropGrowEvent(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCropGrow(BlockGrowEvent e) {
        Block eventBlock = e.getBlock();
        Material material = eventBlock.getType();
        Material newStateMaterial = e.getNewState().getBlockData().getMaterial();

        //Block melon and pumpkin blocks from growing from stems
        if (newStateMaterial.equals(Material.PUMPKIN) || newStateMaterial.equals(Material.MELON)) {
            e.setCancelled(true);
            return;
        }

        if (material.equals(Material.CARROTS) ||
//                material.equals(Material.SWEET_BERRY_BUSH) ||
                material.equals(Material.WHEAT) ||
                material.equals(Material.BEETROOTS) ||
                material.equals(Material.MELON_STEM) ||
                material.equals(Material.POTATOES) ||
                material.equals(Material.PUMPKIN_STEM) ||
                material.equals(Material.COCOA) ||
                material.equals(Material.NETHER_WART)
        ) {
            handleCrop(eventBlock, material, e);
        }
    }

    private void handleCrop(Block crop, Material type, BlockGrowEvent event) {
        World world = crop.getWorld();
        Ageable ageable = (Ageable) crop.getBlockData();
        String currentSeason = plugin.getSapi().getSeason(world).toString();
        FileConfiguration config = plugin.getConfig();
        String cropConfigLocation = "crops.".concat(type.toString());
        String fertileWorld = config.getString(cropConfigLocation.concat(".fertile-world"));
        String fertileSeason = config.getString(cropConfigLocation.concat(".fertile-season"));
        int chance = config.getInt(cropConfigLocation.concat(".giant-crop-chance"));
        String customBlockID = config.getString(cropConfigLocation.concat(".giant-crop-id"));
        boolean isDirectional = config.getBoolean(cropConfigLocation.concat(".directional"));
        CustomBlock giantCrop;

        //Check if the crop is in fertile world, if not cancel the growth
        if (!fertileWorld.equals(world.getName())) {
            event.setCancelled(true);
            return;
        }

        //Check if current season matches the fertile season, or if it's allowed to grow in any season
        if (fertileSeason.equals("Any") || fertileSeason.equals(currentSeason)) {

            //Check if the crop is fully grown
            if (ageable.getAge() != ageable.getMaximumAge() - 1) {
                return;
            }

            //Chance for crop to be giant
            if (new Random().nextInt(100) > (100 - chance)) {

                //Check if crop is directional
                if (isDirectional) {
                    Directional directional = (Directional) crop.getBlockData();
                    String facing = directional.getFacing().toString().toLowerCase();
                    giantCrop = CustomBlock.getInstance(customBlockID.concat("_").concat(facing));
                } else {
                    giantCrop = CustomBlock.getInstance(customBlockID);
                }
                event.setCancelled(true);
                giantCrop.place(crop.getLocation());
            }
        }
    }

}

