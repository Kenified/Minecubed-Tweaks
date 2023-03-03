package com.minecubedmc.runanbles;

import com.minecubedmc.Tweaks;
import org.bukkit.GameRule;
import org.bukkit.World;

import java.util.*;

public class SyncWorldTimeEvent implements Runnable{

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
