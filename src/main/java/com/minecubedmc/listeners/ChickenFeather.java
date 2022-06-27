package com.minecubedmc.listeners;


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


public class ChickenFeather implements Listener {

    private final Tweaks plugin;

    public ChickenFeather(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEggDrop(EntityDropItemEvent e) {
        //Check if entity is chicken
        Entity entity = e.getEntity();
        if (!(entity instanceof Chicken)) {
            return;
        }

        //Check if dropped item is egg
        ItemStack eDrop = e.getItemDrop().getItemStack();
        if(!eDrop.getType().equals(Material.EGG)) {
            return;
        }

        World world = entity.getWorld();
        Location location = entity.getLocation();
        ItemStack drop = new ItemStack(Material.FEATHER);

        double health = ((Chicken) entity).getHealth();
        double maxHealth = ((Chicken) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

        if (health < maxHealth){
            ((Chicken) entity).setHealth(health + 1);
        }
        world.dropItemNaturally(location, drop);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        if (e.getHand() != EquipmentSlot.HAND){
            return;
        }

        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();

        if (entity instanceof Chicken && player.isSneaking()){
            World world = entity.getWorld();
            Location location = entity.getLocation();
            ItemStack drop = new ItemStack(Material.FEATHER);

            double health = ((Chicken) entity).getHealth();

            if (health > 1){
                ((Chicken) entity).damage(0);
                ((Chicken) entity).setHealth(health - 1);
                world.dropItemNaturally(location, drop);
            }
        }
    }
}
