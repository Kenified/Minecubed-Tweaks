package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Illager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;
import java.util.List;

public class NerfCoinDrops implements Listener {

    Tweaks plugin;

    public NerfCoinDrops(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPillagerDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();

        // If natural spawn, ignore
        if( entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
            return;
        }

        // If not an illager, ignore
        if (!(entity instanceof Illager)){
            return;
        }

        // Remove emeralds from non-natural spawn drops
        event.getDrops().removeIf(drop -> drop.getType().equals(Material.EMERALD));
    }
}
