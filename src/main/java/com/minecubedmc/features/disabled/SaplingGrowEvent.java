//package com.minecubedmc.listeners.disabled;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.Material;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.world.StructureGrowEvent;
//
//public class SaplingGrowEvent implements Listener {
//
//    Tweaks plugin;
//
//    public SaplingGrowEvent (Tweaks plugin){    this.plugin = plugin;  }
//
//    @EventHandler
//    public void onSaplingGrow(StructureGrowEvent e){
//
//        if (e.getLocation().getBlock().getType().equals( Material.BIRCH_SAPLING )){
//            e.setCancelled(true);
////            plugin.getServer().getWorld(e.getWorld().getUID()).getBlockAt(e.getLocation()).setb
//            e.getLocation().getBlock().setType(Material.AIR);
//            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "minecraft:placefeature minecraft:mega_spruce " + (int) e.getLocation().getX() + " " + (int) e.getLocation().getY() + " " + (int) e.getLocation().getZ());
//            plugin.getLogger().info("Birch sapling tried to grow!");
//        }
//
//    }
//}
