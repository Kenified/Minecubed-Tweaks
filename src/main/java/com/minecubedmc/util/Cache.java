package com.minecubedmc.util;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    
    private static final ConcurrentHashMap<String, ItemStack> ItemCache = new ConcurrentHashMap<>();
    private static final ItemStack DEFAULT_ITEM = new ItemStack(Material.AIR);
    private static final ConcurrentHashMap<Integer, ItemStack> HDBCache = new ConcurrentHashMap<>();
    
/*
    private static final ConcurrentHashMap<String, CustomBlock> BlockCache = new ConcurrentHashMap<>();
    private static final CustomBlock DEFAULT_BLOCK = null;

    public static CustomBlock getCustomBlock(final String ID) {
        CustomBlock customBlock =  BlockCache.getOrDefault(ID, DEFAULT_BLOCK);

        // Fetch the item and add it to cache if not found
        if (customBlock == DEFAULT_BLOCK){
            if (Tweaks.isIALoaded()) {
                customBlock = CustomBlock.getInstance(ID);
                BlockCache.put(ID, customBlock);
            }
        }

        return customBlock;
    }
*/
    
    public static ItemStack getCustomItem(@NotNull String ID) {
        // Special cases
        switch (ID) {
            case "minecubed:tripwire" -> {
                return new ItemStack(Material.TRIPWIRE);
            }
            case "minecubed:giant_cocoa_north", "minecubed:giant_cocoa_west", "minecubed:giant_cocoa_east", "minecubed:giant_cocoa_south" ->
                ID = "minecubed:giant_cocoa";
            // Block that do not have an item
            case "minecubed:grown_eggplant", "minecubed:grown_corn" -> {
                return new ItemStack(Material.AIR);
            }
        }
    
        ItemStack customItem = ItemCache.getOrDefault(ID, DEFAULT_ITEM);
        
        // Fetch the item and add it to cache if not found
        if (customItem.equals(DEFAULT_ITEM)) {
            // Check if IA is loaded
            if (Tweaks.isIALoaded()) {
                // Special case for water_bottle
                if (ID.equals("minecubed:water_bottle")) {
                    customItem = ItemStack.deserializeBytes(
                        CustomStack.getInstance("minecubed:water_bottle")
                            .getItemStack()
                            .serializeAsBytes()
                    );
                }
                else customItem = CustomStack.getInstance(ID).getItemStack();
            } else return null;
            ItemCache.put(ID, customItem);
        }
    
        return customItem;
    }
    
    public static ItemStack getHeadItem(final int ID) {
        ItemStack headItem = HDBCache.get(ID);
    
        // Fetch the item and add it to cache if not found
        if (headItem == null){
            headItem = Tweaks.getHeadDatabaseAPI().getItemHead(String.valueOf(ID));
            HDBCache.put(ID, headItem);
        }
        
        return headItem;
    }
    
    public static void resetItemCache() {
        if (!ItemCache.isEmpty()){
            ItemCache.clear();
            Tweaks.getPlugin(Tweaks.class).getLogger().info("Item cache cleared");
        }
    }

    public static void resetHeadCache(){
        if (!HDBCache.isEmpty()){
            HDBCache.clear();
            Tweaks.getPlugin(Tweaks.class).getLogger().info("Head cache cleared");
        }
    }
}
