package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class FixIABottleBug implements Listener {

    Tweaks plugin;

    public FixIABottleBug(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraft(CraftItemEvent e){
        ItemStack eventItem = e.getRecipe().getResult();

        //Check if it's a custom item
        if(CustomStack.byItemStack(eventItem) == null){
            return;
        }

        PlayerInventory inventory = e.getWhoClicked().getInventory();
        ItemStack soup_stock = CustomStack.getInstance("minecubed:soup_stock").getItemStack();
        ItemStack lasagna = CustomStack.getInstance("minecubed:lasagna").getItemStack();
        ItemStack stuffed_potato = CustomStack.getInstance("minecubed:stuffed_potato").getItemStack();
        ItemStack shepherds_pie = CustomStack.getInstance("minecubed:shepherds_pie").getItemStack();
        ItemStack honey_glazed_ham = CustomStack.getInstance("minecubed:honey_glazed_ham").getItemStack();

        if (eventItem.equals(soup_stock) ||
                eventItem.equals(lasagna) ||
                eventItem.equals(stuffed_potato) ||
                eventItem.equals(shepherds_pie) ||
                eventItem.equals(honey_glazed_ham)
        ){
            inventory.addItem(new ItemStack(Material.GLASS_BOTTLE));
        }
    }
}
