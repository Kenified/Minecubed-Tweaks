package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import com.minecubedmc.items.CustomTripwireBlock;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.Directional;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;

import java.util.Collection;
import java.util.Random;

public class GiantCrops implements Listener {

    Tweaks plugin;
    
    public GiantCrops(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCropGrow(final BlockGrowEvent event) {
        final Block eventBlock = event.getBlock();
        final Material material = eventBlock.getType();
        final Material newStateMaterial = event.getNewState().getBlockData().getMaterial();

        //Get nearby players and if all of them are afk cancel the growth
        final Collection<Player> players = event.getBlock().getLocation().getNearbyPlayers((eventBlock.getWorld().getSimulationDistance() * 16) + 8 );
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.setCancelled(true);
            return;
        }

        //Block melon and pumpkin blocks from growing from stems
        if (newStateMaterial.equals(Material.PUMPKIN) || newStateMaterial.equals(Material.MELON)) {
            event.setCancelled(true);
            return;
        }

        if (material.equals(Material.CARROTS) ||
                material.equals(Material.SWEET_BERRY_BUSH) ||
                material.equals(Material.WHEAT) ||
                material.equals(Material.BEETROOTS) ||
                material.equals(Material.MELON_STEM) ||
                material.equals(Material.POTATOES) ||
                material.equals(Material.PUMPKIN_STEM) ||
                material.equals(Material.COCOA) ||
                material.equals(Material.NETHER_WART)
        ) {
            handleCrop(eventBlock, material, event);
        }
    }

    private void handleCrop(Block crop, Material type, BlockGrowEvent event) {
        final World world = crop.getWorld();
        final FileConfiguration config = plugin.getConfig();
        final String cropConfigLocation = "crops.".concat(type.toString());

        //Check if the crop is in fertile world, if not cancel the growth
        final String fertileWorld = config.getString(cropConfigLocation.concat(".fertile-world"));
        if (!fertileWorld.equals(world.getName())) {
            event.setCancelled(true);
            return;
        }

        //Check if current season matches the fertile season, or if it's allowed to grow in any season
        final String currentSeason = Tweaks.getSeason();
        final String fertileSeason = config.getString(cropConfigLocation.concat(".fertile-season"));
        
        if (fertileSeason.equals("Any") || fertileSeason.equals(currentSeason)) {
            Ageable ageable = (Ageable) crop.getBlockData();
//            CustomBlock giantCrop = null;
            CustomTripwireBlock giantCrop = null;
            
            int newAge = ((Ageable) event.getNewState().getBlockData()).getAge();
            if ( type.equals(Material.CARROTS) && ageable.getAge() == 2 && newAge == 3) {
                if (new Random().nextInt(100) > (100 - 5)) {
                    giantCrop = Cache.getCustomBlock("minecubed:giant_lettuce");
                }
                else return;
            }
            else if ( type.equals(Material.POTATOES) && ageable.getAge() == 2 && newAge == 3) {
                if (new Random().nextInt(100) > (100 - 5)) {
                    giantCrop = Cache.getCustomBlock("minecubed:giant_onion");
                }
                else return;
            }
            else {
                //Check if the crop is fully grown
                if (ageable.getAge() != ageable.getMaximumAge() - 1) {
                    return;
                }

                //Chance for crop to be giant
                int chance = config.getInt(cropConfigLocation.concat(".giant-crop-chance"));
                if (new Random().nextInt(100) > (100 - chance)) {

                    //Check if crop is directional
                    String customBlockID = config.getString(cropConfigLocation.concat(".giant-crop-id"));
                    boolean isDirectional = config.getBoolean(cropConfigLocation.concat(".directional"));
                    if (isDirectional) {
                        Directional directional = (Directional) crop.getBlockData();
                        String facing = directional.getFacing().toString().toLowerCase();
                        giantCrop = Cache.getCustomBlock(customBlockID.concat("_").concat(facing));
                    } else {
                        giantCrop = Cache.getCustomBlock(customBlockID);
                    }
                }
//            //WIP
//            else if (type.equals(Material.MELON_STEM)){
//                giantCrop = CustomItems.getCustomBlock("minecubed:grown_melon");
//            }
//            else if (type.equals(Material.PUMPKIN_STEM)){
//                giantCrop = CustomItems.getCustomBlock("minecubed:grown_pumpkin");
//            }
                else return;
            }

            event.setCancelled(true);
            giantCrop.place(crop.getLocation());

        }
    }

}

