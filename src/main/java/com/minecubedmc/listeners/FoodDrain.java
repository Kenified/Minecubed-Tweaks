package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodDrain implements Listener {

    public Tweaks plugin;

    public FoodDrain(Tweaks plugin) { this.plugin = plugin; }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFoodLevelChange(FoodLevelChangeEvent e){
        HumanEntity player = e.getEntity();

        //Cap Saturation and hunger for custom foods to follow vanilla logic
        if (player.getFoodLevel() > 20){
            player.setFoodLevel(20);
        }

        if (player.getSaturation() > player.getFoodLevel()){
            player.setSaturation(player.getFoodLevel());
        }

//        plugin.getLogger().warning("Player: " + player.getName() + "| Food: " + player.getFoodLevel() + " | Saturation: " + player.getSaturation());
    }
}
