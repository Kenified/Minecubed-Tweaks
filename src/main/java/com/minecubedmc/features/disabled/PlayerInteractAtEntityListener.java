//package com.minecubedmc.listeners.old;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.Player;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.player.PlayerInteractAtEntityEvent;
//
//public class PlayerInteractAtEntityListener implements Listener {
//
//    private final Tweaks plugin;
//
//    public PlayerInteractAtEntityListener(Tweaks plugin){
//        this.plugin = plugin;
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onEntityInteractEvent(PlayerInteractAtEntityEvent event){
////        plugin.getLogger().info("Interact At Event");
//        Entity entity = event.getRightClicked();
//        Player player = event.getPlayer();
//        EntityType entityType = entity.getType();
//        String eventWorld = event.getRightClicked().getWorld().getName();
//
//        if (entityType.equals(EntityType.ARMOR_STAND) || entityType.equals(EntityType.ITEM_FRAME)){
//
//            if (eventWorld.equals("world_spawn")){
//
//                if (!player.hasPermission("group.admin")){
//                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot interact with that here."));
//                    event.setCancelled(true);
//
//                }
//            }
//        }
//    }
//
//}
