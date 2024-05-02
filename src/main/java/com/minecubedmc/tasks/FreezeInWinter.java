package com.minecubedmc.tasks;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.BasicUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FreezeInWinter extends BukkitRunnable {
    private Tweaks plugin;
    private int period;

    public FreezeInWinter(final Tweaks plugin, final int period) {
        this.plugin = plugin;
        this.period = period;
    }

    @Override
    public void run() {
        //Get all players and for each:
        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
        for (Player player : onlinePlayers) {

            //Check if the player is in overworld
            String world = player.getWorld().getName();
            if (!world.equals("world")) {
                player.lockFreezeTicks(false);
                return;
            }

            Block block = player.getLocation().getBlock();

            // Get biome key from block
            String biomeKey = BasicUtils.getBiomeKey(block);

            //Don't freeze in warm biomes
            if (isInWarmBiome(biomeKey)) {
                player.lockFreezeTicks(false);
                return;
            }

            //Check if season is not winter and player is not in a permanently cold biome
            String season = Tweaks.getSeason();
            if (!season.equals("Winter") || !isInColdBiome(biomeKey)) {
                player.lockFreezeTicks(false);
                return;
            }

            //If it's not inside vehicle
            if (player.isInsideVehicle()) {
                player.lockFreezeTicks(false);
                return;
            }

            //and if its inside water, add freeze ticks
            Material type = block.getType();
            if (type.equals(Material.WATER) ||
                    type.equals(Material.SEAGRASS) ||
                    type.equals(Material.TALL_SEAGRASS) ||
                    type.equals(Material.KELP_PLANT) ||
                    type.equals(Material.KELP)
            ) {
                player.lockFreezeTicks(true);
                player.setFreezeTicks(Math.min(player.getMaxFreezeTicks(), player.getFreezeTicks() + period));
            } else {
                player.lockFreezeTicks(false);
            }
        }
    }

    private boolean isInColdBiome(final @NotNull Biome biome) {
        return biome.equals(Biome.FROZEN_OCEAN)
                || biome.equals(Biome.FROZEN_PEAKS)
                || biome.equals(Biome.FROZEN_RIVER)
                || biome.equals(Biome.DEEP_FROZEN_OCEAN)
                || biome.equals(Biome.GROVE)
                || biome.equals(Biome.ICE_SPIKES)
                || biome.equals(Biome.SNOWY_BEACH)
                || biome.equals(Biome.SNOWY_PLAINS)
                || biome.equals(Biome.SNOWY_SLOPES)
                || biome.equals(Biome.SNOWY_TAIGA)
                || biome.equals(Biome.JAGGED_PEAKS);
    }

    private boolean isInColdBiome(final @NotNull String biome) {
        return biome.equals("minecraft:frozen_ocean")
                || biome.equals("minecraft:frozen_peaks")
                || biome.equals("minecraft:frozen_river")
                || biome.equals("minecraft:deep_frozen_ocean")
                || biome.equals("minecraft:grove")
                || biome.equals("minecraft:ice_spikes")
                || biome.equals("minecraft:snowy_beach")
                || biome.equals("minecraft:snowy_plains")
                || biome.equals("minecraft:snowy_slopes")
                || biome.equals("minecraft:snowy_taiga")
                || biome.equals("minecraft:jagged_peaks")
                || biome.equals("minecubed:frozen_depths");
    }

    private boolean isInWarmBiome(final @NotNull Biome biome) {
        return biome.equals(Biome.DESERT)
                || biome.equals(Biome.BADLANDS)
                || biome.equals(Biome.WOODED_BADLANDS)
                || biome.equals(Biome.ERODED_BADLANDS)
                || biome.equals(Biome.SAVANNA)
                || biome.equals(Biome.SAVANNA_PLATEAU)
                || biome.equals(Biome.WINDSWEPT_SAVANNA)
                || biome.equals(Biome.JUNGLE)
                || biome.equals(Biome.BAMBOO_JUNGLE)
                || biome.equals(Biome.SPARSE_JUNGLE)
                || biome.equals(Biome.WARM_OCEAN)
                || biome.equals(Biome.OCEAN)
                || biome.equals(Biome.DEEP_OCEAN)
                || biome.equals(Biome.DEEP_LUKEWARM_OCEAN)
                || biome.equals(Biome.LUKEWARM_OCEAN);
    }

    private boolean isInWarmBiome(final @NotNull String biome) {
        return biome.equals("minecraft:desert")
                || biome.equals("minecraft:badlands")
                || biome.equals("minecraft:wooded_badlands")
                || biome.equals("minecraft:eroded_badlands")
                || biome.equals("minecraft:savanna")
                || biome.equals("minecraft:savanna_plateau")
                || biome.equals("minecraft:windswept_savanna")
                || biome.equals("minecraft:jungle")
                || biome.equals("minecraft:bamboo_jungle")
                || biome.equals("minecraft:sparse_jungle")
                || biome.equals("minecraft:warm_ocean")
                || biome.equals("minecraft:ocean")
                || biome.equals("minecraft:deep_ocean")
                || biome.equals("minecraft:deep_lukewarm_ocean")
                || biome.equals("minecraft:lukewarm_ocean")
                || biome.equals("minecubed:dark_jungle")
                || biome.equals("minecubed:outback")
                || biome.equals("minecubed:xeric_shrubland")
                || biome.equals("minecubed:tropical_river")
                || biome.equals("minecubed:oaasis");
    }
}
