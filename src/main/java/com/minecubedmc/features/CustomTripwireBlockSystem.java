package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.items.CustomTripwireBlock;
import com.minecubedmc.util.Cache;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public class CustomTripwireBlockSystem implements Listener {
    Tweaks plugin;
    
    public CustomTripwireBlockSystem(Tweaks plugin){
        this.plugin = plugin;
    }
    
    private static final ConcurrentHashMap<String, CustomTripwireBlock> CustomTripwireBlocks = new ConcurrentHashMap<>();
    
    public static void seedBlocks(){
        CustomTripwireBlocks.put("minecubed:tripwire",               new CustomTripwireBlock(0));
        CustomTripwireBlocks.put("minecubed:grown_eggplant",         new CustomTripwireBlock(1));
        CustomTripwireBlocks.put("minecubed:grown_corn",             new CustomTripwireBlock(2));
        CustomTripwireBlocks.put("minecubed:giant_pumpkin",          new CustomTripwireBlock(3));
        CustomTripwireBlocks.put("minecubed:giant_strawberry",       new CustomTripwireBlock(4));
        CustomTripwireBlocks.put("minecubed:giant_onion",            new CustomTripwireBlock(5));
        CustomTripwireBlocks.put("minecubed:giant_lettuce",          new CustomTripwireBlock(6));
        CustomTripwireBlocks.put("minecubed:giant_cocoa",            new CustomTripwireBlock(7));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_north",      new CustomTripwireBlock(8));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_east",       new CustomTripwireBlock(9));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_south",      new CustomTripwireBlock(10));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_west",       new CustomTripwireBlock(11));
        CustomTripwireBlocks.put("minecubed:wild_eggplants",         new CustomTripwireBlock(12));
        CustomTripwireBlocks.put("minecubed:wild_corn",              new CustomTripwireBlock(13));
        CustomTripwireBlocks.put("minecubed:wild_tomatoes",          new CustomTripwireBlock(14));
        CustomTripwireBlocks.put("minecubed:wild_potatoes",          new CustomTripwireBlock(15));
        CustomTripwireBlocks.put("minecubed:wild_onions",            new CustomTripwireBlock(16));
        CustomTripwireBlocks.put("minecubed:wild_lettuce",           new CustomTripwireBlock(17));
        CustomTripwireBlocks.put("minecubed:wild_carrots",           new CustomTripwireBlock(18));
        CustomTripwireBlocks.put("minecubed:jacaranda_droopy",       new CustomTripwireBlock(19));
        CustomTripwireBlocks.put("minecubed:toadstool",              new CustomTripwireBlock(20));
        CustomTripwireBlocks.put("minecubed:flower_cover_aqua",      new CustomTripwireBlock(21));
        CustomTripwireBlocks.put("minecubed:flower_cover_blue",      new CustomTripwireBlock(22));
        CustomTripwireBlocks.put("minecubed:flower_cover_indigo",    new CustomTripwireBlock(23));
        CustomTripwireBlocks.put("minecubed:flower_cover_pink",      new CustomTripwireBlock(24));
        CustomTripwireBlocks.put("minecubed:flower_cover_red",       new CustomTripwireBlock(25));
        CustomTripwireBlocks.put("minecubed:flower_cover_yellow",    new CustomTripwireBlock(26));
        
        // TODO: find more elegant way when not lazy
        Tweaks.getPlugin(Tweaks.class).getLogger().info("Seeded tripwire blocks");
    }
    
    public static CustomTripwireBlock getCustomBlock(final String ID){
        return CustomTripwireBlocks.get(ID);
    }
    
    public static String[] getCustomBlockList(){
        return CustomTripwireBlocks.keySet().toArray(new String[0]);
    }
    
    @EventHandler
    public void blockBreakEvent(final BlockBreakEvent event){
        Block eventBlock = event.getBlock();
        Block relativeBlock = eventBlock.getRelative(BlockFace.UP);
        
        final Block finalBlock;
        
        if (eventBlock.getType().equals(Material.TRIPWIRE)){
            finalBlock = eventBlock;
        } else if (relativeBlock.getType().equals(Material.TRIPWIRE)){
            finalBlock = relativeBlock;
        } else {
            return;
        }
    
        // Iterate through the custom block list and verify if the broken block matches any of them
        Arrays.stream(getCustomBlockList()).sequential().forEach(customBlock -> {
    
            // Don't drop custom item if player is in creative unless player breaks relative block
            if (relativeBlock != finalBlock){
                if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                    return;
                }
            }
            
            // Check if block matches custom block
            if (finalBlock.getBlockData().equals(getCustomBlock(customBlock).getBlockData())){
                // Don't drop vanilla item
                event.setDropItems(false);
                // Drop custom item
                finalBlock.getWorld().dropItemNaturally(finalBlock.getLocation(), Cache.getCustomItem(customBlock));
            }
            
        });
    }
    
    @EventHandler
    public void blockPlaceEvent(final BlockPlaceEvent event){
        Block eventBlock = event.getBlock();
        final ItemStack eventItem = event.getItemInHand();
        
        // Don't allow placing tripwire
        if (eventBlock.getType().equals(Material.TRIPWIRE)){
            if (eventBlock.getBlockData().equals(getCustomBlock("minecubed:tripwire").getBlockData())){
                event.setCancelled(true);
                return;
            }
        }
        
        // Iterate through the list of custom blocks and determine if they have been placed
        Arrays.stream(getCustomBlockList()).sequential().forEach(customBlockID -> {
            ItemStack customItem = null;
            
            
            // Check if item exists
            try {
                customItem = Cache.getCustomItem(customBlockID);
            } catch (NullPointerException e) {
                plugin.getLogger().severe("Could not find custom item: " + customBlockID);
            }
            
            // If item is null, return
            if (customItem == null) {
                return;
            }
            
            // If item is not custom block, return
            if (!eventItem.isSimilar(customItem)) {
                return;
            }
    
            // Place custom block
            getCustomBlock(customBlockID).place(eventBlock.getLocation());
        
            // Remove item from player if not in creative
            if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                eventItem.subtract(1);
            }
        });
    }
    
    
    
}
