//package com.minecubedmc.listeners.disabled;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.Material;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.Listener;
//import org.bukkit.event.inventory.CraftItemEvent;
//import org.bukkit.event.inventory.InventoryType;
//import org.bukkit.inventory.Inventory;
//import org.bukkit.inventory.ItemStack;
//
//public class GrindstoneNoXP implements Listener {
//
//    Tweaks plugin;
//
//    public GrindstoneNoXP(Tweaks plugin){
//        this.plugin = plugin;
//    }
//
//    @EventHandler
//    public void onGrind(CraftItemEvent e){
//        Inventory inventory = e.getInventory();
//        if (inventory.getType().equals(InventoryType.GRINDSTONE)){
//
//            if (inventory.getItem(0).getType().equals(Material.ENCHANTED_BOOK) ||
//                inventory.getItem(1).getType().equals(Material.ENCHANTED_BOOK)
//            ){
//                e.setCancelled(true);
//                inventory.setItem(0, new ItemStack(Material.AIR));
//                inventory.setItem(2, new ItemStack(Material.BOOK));
//           }
//
//        }
//    }
//}
