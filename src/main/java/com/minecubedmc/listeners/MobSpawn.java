package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Random;


public class MobSpawn implements Listener {

    Tweaks plugin;

    public MobSpawn(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void entitySpawnEvent(CreatureSpawnEvent e){
        Entity entity = e.getEntity();

//        Location loc = e.getLocation();
//        World world = e.getLocation().getWorld();
//        String biome = world.getBiome(loc).toString();
//        plugin.getLogger().warning(biome);

//        if (e.getEntity() instanceof Zombie && biome.equals("minecubed:sandy_catacombs") && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
//            e.setCancelled(true);
//            world.spawnEntity(loc, EntityType.HUSK, CreatureSpawnEvent.SpawnReason.NATURAL);
//        }
//        else if (e.getEntity() instanceof Skeleton && biome.equals("minecubed:frozen_depths") && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
//            e.setCancelled(true);
//            world.spawnEntity(loc, EntityType.STRAY, CreatureSpawnEvent.SpawnReason.NATURAL);
//        }

        //Limit Frog Amount
        if (entity instanceof Frog && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
            if ( new Random().nextInt(4) + 1 > 1 ) {
                e.setCancelled(true);
            }
        }
    }
}
