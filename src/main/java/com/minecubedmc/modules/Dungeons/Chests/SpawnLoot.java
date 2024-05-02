package com.minecubedmc.modules.Dungeons.Chests;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.loot.LootTable;

public class SpawnLoot {

    public static void generateLootChests(Location[] locations, NamespacedKey lootTableKey){
        for (Location location : locations) {

            // Set chest block
            location.getBlock().setType(Material.CHEST);

            // Get block as chest
            Chest lootChest = (Chest) location.getBlock();

            // Get loot table from namespaced key
            LootTable lootTable = Bukkit.getLootTable(lootTableKey);

            // Set loot table for chest
            lootChest.setLootTable(lootTable);
        }
    }

}
