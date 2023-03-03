package com.minecubedmc.runanbles;

import com.minecubedmc.Tweaks;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;

public class FreezeInWinter implements Runnable {
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
                return;
            }

            Block block = player.getLocation().getBlock();
            Biome biome = block.getBiome();

            //Don't freeze in warm biomes
            if (isInWarmBiome(biome)) {
                return;
            }

            //Check if season is winter or player is in a cold biome
            String season = Tweaks.getSeason();
            if (!season.equals("Winter") && !isInColdBiome(biome)) {
                return;
            }

            //If it's not inside vehicle
            if (player.isInsideVehicle()) {
                return;
            }

            //and if its inside water, add freeze ticks
            Material type = block.getType();
            if (type.equals(Material.WATER)) {
                player.lockFreezeTicks(true);
                player.setFreezeTicks(Math.min(player.getMaxFreezeTicks(), player.getFreezeTicks() + period));
            } else {
                player.lockFreezeTicks(false);
            }
        }
    }

    private boolean isInColdBiome(final Biome biome) {
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
                || biome.equals(Biome.JAGGED_PEAKS)
                || biome.equals(Biome.CUSTOM);
    }

    private boolean isInWarmBiome(final Biome biome) {
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
}
