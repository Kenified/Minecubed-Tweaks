package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.entity.Entity;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Squid;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class SquidFallDamage implements Listener {

    Tweaks plugin;

    public SquidFallDamage(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSquidDamage(EntityDamageEvent e){
        Entity entity = e.getEntity();

        if (entity instanceof Squid || entity instanceof GlowSquid){
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)){
                e.setCancelled(true);
            }
        }
    }
}
