package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TougherPets implements Listener {

    Tweaks plugin;

    public TougherPets(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPetDamage(EntityDamageByEntityEvent event){
        if (event.isCancelled()){
            return;
        }

        Entity pet = event.getEntity();
        Entity damager = event.getDamager();

        if (!(pet instanceof Tameable) && !(pet instanceof Allay)){
            return;
        }

        if (damager instanceof Player){
            return;
        }

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause == EntityDamageEvent.DamageCause.FALL ||
            cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||
            cause == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||
            cause == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ||
            cause == EntityDamageEvent.DamageCause.PROJECTILE

        ){
            event.setCancelled(true);
        }

    }

}
