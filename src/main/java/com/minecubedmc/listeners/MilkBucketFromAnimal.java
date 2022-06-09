package com.minecubedmc.listeners;


import com.minecubedmc.Tweaks;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MilkBucketFromAnimal implements Listener {

    private final Tweaks plugin;

    public MilkBucketFromAnimal(Tweaks plugin) {
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
        if (item == null || !item.getType().equals(Material.BUCKET)) {
            return;
        }


        if (!e.getRightClicked().getType().equals(EntityType.GOAT)){
            return;
        }
        e.setCancelled(true);
        item.subtract(1);

        ItemStack customItem = new ItemStack(Material.MILK_BUCKET);
        if (inventory.addItem(customItem).size() > 0) {
            player.getWorld().dropItem(player.getLocation(), customItem);
        }
    }
}
