package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;


public class MobSpawn implements Listener {

    Tweaks plugin;

    public MobSpawn(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void entitySpawnEvent(CreatureSpawnEvent e){
        Location loc = e.getLocation();
        World world = e.getLocation().getWorld();
        String biome = world.getBiome(loc).toString();
        plugin.getLogger().warning(biome);
        if (e.getEntity() instanceof Zombie && biome.equals("minecubed:sandy_catacombs") && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
            e.setCancelled(true);
            world.spawnEntity(loc, EntityType.HUSK, CreatureSpawnEvent.SpawnReason.NATURAL);
        }
        else if (e.getEntity() instanceof Skeleton && biome.equals("minecubed:frozen_depths") && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
            e.setCancelled(true);
            world.spawnEntity(loc, EntityType.STRAY, CreatureSpawnEvent.SpawnReason.NATURAL);
        }

    }
}
