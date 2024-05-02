package com.minecubedmc.features;


import com.minecubedmc.Tweaks;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;


public class ChickenExtraFeathers implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEggDrop(final EntityDropItemEvent event) {
        //Check if entity is chicken
        final Entity entity = event.getEntity();
        if (!(entity instanceof Chicken)) {
            return;
        }
    
        final Collection<Player> players = event.getEntity().getLocation().getNearbyPlayers((event.getEntity().getWorld().getSimulationDistance() * 16) + 8 );
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.setCancelled(true);
            return;
        }

        //Check if dropped item is egg
        final ItemStack eDrop = event.getItemDrop().getItemStack();
        if(!eDrop.getType().equals(Material.EGG)) {
            return;
        }
    
        final double health = ((Chicken) entity).getHealth();
        final double maxHealth = ((Chicken) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

        //Fix error 'Health must be between 0 and 4.0, but was 4.924400329589844'
        if (health > 3 && health < maxHealth){
            ((Chicken) entity).setHealth(maxHealth);
        }
        else if (health < maxHealth){
            ((Chicken) entity).setHealth(Math.min(health + 1, maxHealth));
        }

        // 50% chance to drop feather
        if (new Random().nextFloat() < 0.5f) {
            final World world = entity.getWorld();
            final Location location = entity.getLocation();
            final ItemStack drop = new ItemStack(Material.FEATHER);
            world.dropItemNaturally(location, drop);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEntityEvent e) {
        if (e.getHand() != EquipmentSlot.HAND){
            return;
        }
    
        final Player player = e.getPlayer();
        final Entity entity = e.getRightClicked();

        if (entity instanceof Chicken && player.isSneaking()){
            final World world = entity.getWorld();
            final Location location = entity.getLocation();
            final ItemStack drop = new ItemStack(Material.FEATHER);
    
            final double health = ((Chicken) entity).getHealth();

            if (health > 1){
                ((Chicken) entity).damage(0);
                ((Chicken) entity).setHealth(health - 1);
                world.dropItemNaturally(location, drop);
            }
        }
    }
}
