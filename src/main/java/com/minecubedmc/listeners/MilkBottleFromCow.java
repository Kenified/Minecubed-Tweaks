package com.minecubedmc.listeners;


import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MilkBottleFromCow implements Listener {

    private final Tweaks plugin;

    public MilkBottleFromCow(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent e) {
        //Ignore if not triggered by main hand
        if (e.getHand() != EquipmentSlot.HAND){
            return;
        }

        //If e item is null or isn't glass bottle return
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();
        ItemStack item = inventory.getItemInMainHand();
        if (item == null || !item.getType().equals(Material.GLASS_BOTTLE)) {
            return;
        }

        ItemStack customItem;
        Entity interactedEntity = e.getRightClicked();
        if (interactedEntity instanceof Cow || interactedEntity instanceof Goat){
            customItem = CustomStack.getInstance("minecubed:milk_bottle").getItemStack();
        }
        else if (interactedEntity instanceof Zombie){
            customItem = CustomStack.getInstance("minecubed:zombie_milk").getItemStack();
        }
        else return;

        e.setCancelled(true);
        item.subtract(1);

        if (inventory.addItem(customItem).size() > 0) {
            player.getWorld().dropItem(player.getLocation(), customItem);
        }
    }
}
