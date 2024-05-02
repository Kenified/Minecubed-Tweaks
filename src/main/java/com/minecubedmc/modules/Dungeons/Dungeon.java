package com.minecubedmc.modules.Dungeons;

import com.minecubedmc.Tweaks;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;

public class Dungeon {

    private final List<Location> arenaEntrances;

    private DungeonData dungeonData;

    public Dungeon(List<Location> arenaEntrances) {
        this.arenaEntrances = arenaEntrances;
    }

    public void setCooldown(int minutes) {
        // Convert minutes to milliseconds
        int milliseconds = minutes * 60 * 1000;


        dungeonData.cooldown = System.currentTimeMillis() + milliseconds;
    }

    private void disableCooldown() {
        dungeonData.cooldown = -1;
    }

    public long getCooldown() {
        return dungeonData.cooldown;
    }

    public void refresh() {
        // Respawn chests

        // Pick a random arena entrance location

        // Kill the boss if it is still alive
        // Spawn the boss

        // Set the dungeon to active
        disableCooldown();

        // Clear the loot chests
        dungeonData.lootChests.clear();
    }

    public void saveData() {
        // Save the dungeon data to a file
        // Save itemstack in inventories to a file
        dungeonData.lootChests.forEach((location, lootedChest) -> {

            // Save the looted chest to a file with the name being the hash of the location
            Tweaks plugin = Tweaks.getInstance();
            int locationHash = location.hashCode();

            // Convert the inventory to an array of itemstacks that is serializable and store into an intermediary hashmap
            HashMap<UUID, ItemStack[]> intermediary = new HashMap<>();
            lootedChest.forEach((uuid, inventory) -> {
                intermediary.put(uuid, inventory.getContents());
            });

            // Try to save the intermediary hashmap to a file
            try {
                File file = new File(plugin.getDataFolder(), "LootChests/" + locationHash + ".dat");

                if (!file.exists()) {
                    file.createNewFile();
                }

                // Write the intermediary hashmap to the file
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                objectOutputStream.writeObject(intermediary);
                objectOutputStream.close();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not save persistent loot chest data");
                plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
            }

        });

    }

    public void loadData() {
        // Load the dungeon data from a file
        // Load itemstack in file to inventories
        dungeonData.lootChests.forEach((location, lootedChest) -> {

            Tweaks plugin = Tweaks.getInstance();
            int locationHash = location.hashCode();

            File file = new File(plugin.getDataFolder(), "Lootchests/" + locationHash + ".dat");

            if (file.exists()) {
                try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                    HashMap<UUID, ItemStack[]> intermediary = (HashMap<UUID, ItemStack[]>) objectInputStream.readObject();
                    intermediary.forEach((uuid, itemStacks) -> {
                        lootedChest.get(uuid).setContents(intermediary.get(uuid));
                    });
                } catch (IOException | ClassNotFoundException e) {
                    plugin.getLogger().severe("Could not load persistent leaves data");
                    plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
                }
            }
        });
    }
}
