//package com.minecubedmc.listeners.old;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.ChatColor;
//import org.bukkit.entity.Entity;
//import org.bukkit.entity.EntityType;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.entity.EntityDamageByEntityEvent;
//
//public class EntityDamageByEntityListener implements Listener {
//
//    private final Tweaks plugin;
//
//    public EntityDamageByEntityListener(Tweaks plugin){
//        this.plugin = plugin;
//    }
//
//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onDamageEvent(EntityDamageByEntityEvent event){
////        plugin.getLogger().info("Damage Event");
//        Entity entity = event.getEntity();
//        Entity damager = event.getDamager();
//        EntityType entityType = entity.getType();
//        String eventWorld = event.getEntity().getLocation().getWorld().getName();
//
//        if (entityType.equals(EntityType.ARMOR_STAND) || entityType.equals(EntityType.ITEM_FRAME)){
//
//            if (eventWorld.equals("world_spawn")){
//
//                if (damager.getType().equals(EntityType.PLAYER)){
//
//                    if (!damager.hasPermission("group.admin")){
//                        damager.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou cannot break that here."));
//                        event.setCancelled(true);
//                    }
//                }
//            }
//        }
//    }
//
//}
