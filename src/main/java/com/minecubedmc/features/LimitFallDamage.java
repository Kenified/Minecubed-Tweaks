package com.minecubedmc.features;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class LimitFallDamage implements Listener {

    @EventHandler
    public void onSquidDamage(final EntityDamageEvent event) {

        //Ignore non fall damage
        if (!event.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            return;
        }

        //Get damaged entity
        final Entity entity = event.getEntity();

        if (entity instanceof Player player) {

            //If player wears no boots return
            final ItemStack boots = player.getInventory().getBoots();
            if (boots == null){
                return;
            }

            //Ignore fall damage event completely if its below certain fall distance since damage is usually 0 anyhow
            float fallDistance = player.getFallDistance();
            if (fallDistance > 3) {
                float featherFallingLevel = boots.getEnchantments().getOrDefault(Enchantment.PROTECTION_FALL, 0);
                if (fallDistance < featherFallingLevel * 2.5) {
                    event.setCancelled(true);
                    return;
                }
            }

            //Ignore player fall damage if its riding immune vehicle
            final Entity vehicle = player.getVehicle();
            if (isImmune(vehicle)) {
                event.setCancelled(true);
            }

            return;
        }

        //Make certain entities immune to fall damage to increase viability
        if (isImmune(entity)) {
            event.setCancelled(true);
        }
    }

    private boolean isImmune(Entity entity) {
        return entity instanceof Squid ||
                entity instanceof Horse ||
                entity instanceof Donkey ||
                entity instanceof Mule ||
                entity instanceof ZombieHorse ||
                entity instanceof SkeletonHorse ||
                entity instanceof Boat ||
                entity instanceof Minecart;
    }
}
