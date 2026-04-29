package com.minecubedmc.features;

import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class StrongerLlamas implements Listener {
    
    // Increase the damage of llama spit projectile
    @EventHandler
    public void onLlamaSpit(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Llama) {
            event.setDamage(event.getDamage() * 6); // Increase damage by 6 times
        }
    }
}
