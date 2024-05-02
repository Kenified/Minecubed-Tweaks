package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SpawnerReroll implements Listener {

    Tweaks plugin;



    public SpawnerReroll(Tweaks plugin) {
        this.plugin = plugin;
    }

    private static Inventory buildMenu(){
        ItemStack emptyItem = new ItemStack(Material.BARRIER);

        Map<Integer, ItemStack> buttonMap = Map.of(
            30, emptyItem,
            31, emptyItem,
            32, emptyItem
        );

        Inventory rerollMenu = Bukkit.getServer().createInventory(null, 5, Component.text("Spawner Reroll"));
        buttonMap.forEach(rerollMenu::setItem);

        return rerollMenu;
    }

    public static void openMenu(Player player){
        player.openInventory(buildMenu());
    }

    @EventHandler
    public void inventoryInteract(InventoryClickEvent event){
        if (event.getView().getOriginalTitle() != "Spawner Reroll") return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getWhoClicked().getActiveItem();
        Inventory inventory = event.getInventory();

        if (item.getType() == Material.BARRIER) event.setCancelled(true);
        if (event.getSlot() != 13) event.setCancelled(true);
    }

}
