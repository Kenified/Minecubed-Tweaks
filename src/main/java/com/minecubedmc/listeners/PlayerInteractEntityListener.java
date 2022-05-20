package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerInteractEntityListener implements Listener {

    private final Tweaks plugin;

    public PlayerInteractEntityListener(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityInteractEvent(PlayerInteractEntityEvent event){
//        plugin.getLogger().info("Interact Event");
        Entity entity = event.getRightClicked();
        Player player = event.getPlayer();
        EntityType entityType = entity.getType();
        String eventWorld = event.getRightClicked().getWorld().getName();

        if (entityType.equals(EntityType.ARMOR_STAND) || entityType.equals(EntityType.ITEM_FRAME)){

            if (eventWorld.equals("world_spawn")){

                if (!player.hasPermission("group.admin")){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot interact with that here."));
                    event.setCancelled(true);

                }
            }
        }
    }

}
