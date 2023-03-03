package com.minecubedmc.features;

import com.minecubedmc.items.CustomItems;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MilkBottleFromCow implements Listener {
    
    @EventHandler
    public void onPlayerInteract(final PlayerInteractEntityEvent event) {
        //Ignore if not triggered by main hand
        if (event.getHand() != EquipmentSlot.HAND){
            return;
        }

        //If event item is null or isn't glass bottle return
        final Player player = event.getPlayer();
        final PlayerInventory inventory = player.getInventory();
        final ItemStack item = inventory.getItemInMainHand();
        if (item == null || !item.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }
    
        ItemStack customItem;
        final Entity interactedEntity = event.getRightClicked();
        if (interactedEntity instanceof Cow || interactedEntity instanceof Goat || interactedEntity instanceof Sheep){
            customItem = CustomItems.getCustomItem("minecubed:milk_bottle");
        }
        else if (interactedEntity instanceof Zombie){
            customItem = CustomItems.getCustomItem("minecubed:zombie_milk");
        }
        else return;

        event.setCancelled(true);
        item.subtract(1);

        if (inventory.addItem(customItem).size() > 0) {
            player.getWorld().dropItem(player.getLocation(), customItem);
        }
    }
}
