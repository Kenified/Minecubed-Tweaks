package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

public class HangingBreakByEntityListener implements Listener {

    private final Tweaks plugin;

    public HangingBreakByEntityListener(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamageEvent(HangingBreakByEntityEvent event){
//        plugin.getLogger().info("Hanging Break By Event");
        Entity entity = event.getEntity();
        Entity damager = event.getRemover();
        EntityType entityType = entity.getType();
        String eventWorld = event.getEntity().getLocation().getWorld().getName();

        if (entityType.equals(EntityType.ARMOR_STAND) || entityType.equals(EntityType.ITEM_FRAME)){

            if (eventWorld.equals("world_spawn")){

                if (damager.getType().equals(EntityType.PLAYER)){

                    if (!damager.hasPermission("group.admin")){
                        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot break that here."));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

}
