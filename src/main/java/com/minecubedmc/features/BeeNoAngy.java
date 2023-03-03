package com.minecubedmc.features;

import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

public class BeeNoAngy implements Listener {

    @EventHandler
    public void beeAngy(final EntityTargetEvent event){
        final Entity entity = event.getEntity();

        if (entity instanceof Bee){
            event.setCancelled(true);
        }
    }

}
