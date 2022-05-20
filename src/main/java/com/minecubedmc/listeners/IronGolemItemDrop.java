package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class IronGolemItemDrop implements Listener {

    private final Tweaks plugin;

    public IronGolemItemDrop(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        LivingEntity entity = e.getEntity();

        if (entity instanceof IronGolem) {
            ItemStack ironScrap = CustomStack.getInstance("minecubed:iron_scrap").getItemStack();
            entity.getLocation().getWorld().dropItem(entity.getLocation(), ironScrap);
        }
    }

}
