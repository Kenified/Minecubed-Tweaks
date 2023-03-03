package com.minecubedmc.features;

import com.minecubedmc.items.CustomItems;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FixIABottleBug implements Listener {
    
    private final ArrayList<ItemStack> items = new ArrayList<>(Arrays.asList(
        CustomItems.getCustomItem("minecubed:soup_stock"),
        CustomItems.getCustomItem("minecubed:lasagna"),
        CustomItems.getCustomItem("minecubed:stuffed_potato"),
        CustomItems.getCustomItem("minecubed:shepherds_pie"),
        CustomItems.getCustomItem("minecubed:honey_glazed_ham")
    ));
    
    @EventHandler(priority = EventPriority.HIGH)
    public void onCraft(final CraftItemEvent event) {
        final ItemStack eventItem = event.getRecipe().getResult();

        //Check if it's a custom item
        if (CustomStack.byItemStack(eventItem) == null) {
            return;
        }
    
        final PlayerInventory inventory = event.getWhoClicked().getInventory();
        
        if (items.contains(eventItem)) {
            inventory.addItem(new ItemStack(Material.GLASS_BOTTLE));
        }
    }
}
