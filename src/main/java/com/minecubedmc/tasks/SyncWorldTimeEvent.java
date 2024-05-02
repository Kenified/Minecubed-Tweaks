package com.minecubedmc.tasks;

import com.minecubedmc.Tweaks;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class SyncWorldTimeEvent extends BukkitRunnable {

    Tweaks plugin;
    World overworld;
    String[] syncedWorldName;
    Set<World> syncedWorlds = new HashSet<>();

    public SyncWorldTimeEvent(Tweaks plugin, String[] syncedWorldName){
        this.plugin = plugin;
        this.syncedWorldName = syncedWorldName;
        overworld = plugin.getServer().getWorld("world");
    
        for (String worldName : syncedWorldName) {
            syncedWorlds.add(plugin.getServer().getWorld(worldName));
        }
        
        for (World world : syncedWorlds) {
            if (Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_DAYLIGHT_CYCLE))) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            }
            if (Boolean.TRUE.equals(world.getGameRuleValue(GameRule.DO_WEATHER_CYCLE))){
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            }
        }

        plugin.getLogger().info("Started syncing time for worlds: " + Arrays.toString(syncedWorldName));
        
    }

    @Override
    public void run() {
        syncedWorlds.forEach(world -> {
            world.setTime(overworld.getTime());
            world.setStorm(overworld.hasStorm());
            world.setThundering(overworld.isThundering());
        });
    }
}
