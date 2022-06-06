package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsumeListener implements Listener {

    Tweaks plugin;

    public PlayerItemConsumeListener(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e){
        ItemStack eventFood = e.getItem();
        Player eventPlayer = e.getPlayer();

        //Cap Saturation for custom foods to follow vanilla logic
        if (eventPlayer.getSaturation() > eventPlayer.getFoodLevel()){
            eventPlayer.setSaturation(eventPlayer.getFoodLevel());
        }

        //Check if it's not a custom item
        if(CustomStack.byItemStack(eventFood) != null){
            return;
        }

        switch (eventFood.getType()){
            case POTATO:
            case CHICKEN:
            case BEEF:
            case PORKCHOP:
            case RABBIT:
            case MUTTON:
                e.setCancelled(true);
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 200, 0, true ));
                handleFood(eventPlayer, eventFood, 1, 0.6f, null);
                break;
            case BAKED_POTATO:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 3, 1.9f, null);
                break;
            case CARROT:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 1, 1.9f, null);
                break;
            case BEETROOT:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 2, 2.4f, null);
                break;
            case MELON:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 1, 0.6f, null);
                break;
            case GLOW_BERRIES:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 1, 1f, null);
                break;
            case COOKIE:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 2, 1f, null);
                break;
            case COOKED_CHICKEN:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 4, 2.6f, null);
                break;
            case COOKED_BEEF:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 4, 3.5f, null);
                break;
            case COOKED_PORKCHOP:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 4, 4f, null);
                break;
            case COOKED_RABBIT:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 4, 7.2f, null);
                break;
            case COOKED_MUTTON:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 4, 6f, null);
                break;
            case BREAD:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 3, 2.6f, null);
                break;
            case GOLDEN_CARROT:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 4, 7.1f, null);
                break;
            case GOLDEN_APPLE:
                e.setCancelled(true);
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1, true ));
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0, true ));
                handleFood(eventPlayer, eventFood, 5, 2.6f, null);
                break;
            case ENCHANTED_GOLDEN_APPLE:
                e.setCancelled(true);
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 400, 1, true ));
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 2400, 3, true ));
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 6000, 0, true ));
                eventPlayer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 6000, 0, true ));
                handleFood(eventPlayer, eventFood, 12, 12.1f, null);
                break;
            case HONEY_BOTTLE:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 3, 3f, new ItemStack(Material.GLASS_BOTTLE));
                break;
            case MILK_BUCKET:
                e.setCancelled(true);
                handleFood(eventPlayer, eventFood, 0, 0f, new ItemStack(Material.BUCKET));
                break;
        }
    }

    private void handleFood(Player player, ItemStack food, int foodHunger, float foodSaturation, ItemStack returnItem){
        PlayerInventory inventory = player.getInventory();
        float playerSaturation = player.getSaturation();
        int playerFoodLevel = player.getFoodLevel();

        playerFoodLevel += foodHunger;
        playerSaturation += foodSaturation;

        //Cap Food level at 20
        if (playerFoodLevel > 20 ) {
            playerFoodLevel = 20;
        }
        else{   //Only give saturation if food is below 20
            playerSaturation += foodSaturation;

            //Saturation level cannot be higher than current food level
            if (playerSaturation > playerFoodLevel){
                playerSaturation = playerFoodLevel;
            }

            player.setSaturation(playerSaturation);
        }


        player.setFoodLevel(playerFoodLevel);

        if (inventory.getItemInMainHand().equals(food)){
            inventory.getItemInMainHand().subtract(1);
        }
        else if (inventory.getItemInOffHand().equals(food)){
            inventory.getItemInOffHand().subtract(1);
        }

        if(returnItem != null){
            inventory.addItem(returnItem);
        }
    }
}
