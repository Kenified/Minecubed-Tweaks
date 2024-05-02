package com.minecubedmc.features.disabled;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.BasicUtils;
import io.lumine.mythic.api.skills.conditions.ILocationComparisonCondition;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AgeItems implements Listener {
    
    Tweaks plugin;
    public AgeItems(Tweaks plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onInventoryOpen(final @NotNull InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        
        if (!inventory.getType().equals(InventoryType.BARREL)) {
            return;
        }
        
        BasicUtils.TickInventories.addTickingInventory(generateInventoryUUID(inventory), inventory);
        plugin.getLogger().warning("Added inventory" + generateInventoryUUID(inventory));
        plugin.getLogger().warning(inventory.getLocation().toString());
    }
    
    @EventHandler
    public void onInventoryClose(final @NotNull InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        BasicUtils.TickInventories.removeTickingInventory(generateInventoryUUID(inventory));
        plugin.getLogger().warning("Removed inventory" + generateInventoryUUID(inventory));
        plugin.getLogger().warning(inventory.getLocation().toString());
    }
    
    @EventHandler
    public void onItemMove(final @NotNull InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        
        if (itemStack == null) {
            return;
        }
        
        if (!BasicUtils.TickInventories.isAgingItem(itemStack)) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            
            if (itemMeta == null) {
                return;
            }
            
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
            dataContainer.set(BasicUtils.TickInventories.START_TIME_KEY, PersistentDataType.LONG, 0L);
            
            itemStack.setItemMeta(itemMeta);
        }
    }
    
    private @NotNull UUID generateInventoryUUID(final @NotNull Inventory inventory){
        // Courtesy of ChadGPT
        int x = (int) inventory.getLocation().getX();
        int y = (int) inventory.getLocation().getY();
        int z = (int) inventory.getLocation().getZ();
        long mostSignificantBits = ((long) x << 32) | (y & 0xFFFFFFFFL);
        long leastSignificantBits = (long) z << 32;
        return new UUID(mostSignificantBits, leastSignificantBits);
    }
    
    
}
