package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerItemConsumeListener implements Listener {

    Tweaks plugin;

    public PlayerItemConsumeListener(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e){
        ItemStack eventFood = e.getItem();
        Player eventPlayer = e.getPlayer();

        //Check if it's not a custom item
        if(CustomStack.byItemStack(eventFood) != null){
            return;
        }

        switch (eventFood.getType()){
            case CARROT:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood);
        }
    }

    private void handleFood(Player player, ItemStack food){
        PlayerInventory inventory = player.getInventory();
        float saturation = player.getSaturation();
        int hunger = player.getFoodLevel();

        player.setSaturation(saturation + 2 );
        player.setFoodLevel(hunger + 2);

        if (inventory.getItemInMainHand().equals(food)){
            inventory.getItemInMainHand().subtract(1);
        }
        else if (inventory.getItemInOffHand().equals(food)){
            inventory.getItemInOffHand().subtract(1);
        }
    }
}
