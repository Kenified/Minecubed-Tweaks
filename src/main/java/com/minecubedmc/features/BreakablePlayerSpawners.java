package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.SerializableLocation;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class BreakablePlayerSpawners implements Listener {
    Tweaks plugin;
    public static HashSet<SerializableLocation> breakableSpawnersSet = new HashSet<>();

    public BreakablePlayerSpawners(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerPlace(final BlockPlaceEvent event){
        if (event.isCancelled()) return;

        final Block block = event.getBlock();
        if (!(block.getBlockData() instanceof CreatureSpawner)) return;

        breakableSpawnersSet.add(new SerializableLocation(block.getLocation()));
    }

    @EventHandler
    public void onSpawnerBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) return;

        final Block block = event.getBlock();
        if (!(block.getBlockData() instanceof CreatureSpawner creatureSpawner)) return;

        SerializableLocation serializableLocation = new SerializableLocation(block.getLocation());
        if (breakableSpawnersSet.contains(serializableLocation)) {
            breakableSpawnersSet.remove(serializableLocation);
            ItemStack spawnerItem = getSpawnerItem(creatureSpawner.getSpawnedType());
            block.getLocation().getWorld().dropItemNaturally(block.getLocation(), spawnerItem);
        }

    }

    public static ItemStack getSpawnerItem(EntityType entityType) {
        ItemStack spawnerItem = new ItemStack(Material.SPAWNER);

        ItemMeta meta = spawnerItem.getItemMeta();
        if (meta instanceof BlockStateMeta) {
            BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
            if (blockStateMeta.getBlockState() instanceof CreatureSpawner spawner) {
                spawner.setSpawnedType(entityType);
                blockStateMeta.setBlockState(spawner);
                spawnerItem.setItemMeta(blockStateMeta);
            }
        }
        return spawnerItem;
    }

    public static void loadBreakableSpawnersData() {
        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        File file = new File(plugin.getDataFolder(), "breakableSpawners.dat");
        if (file.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                BreakablePlayerSpawners.breakableSpawnersSet = (HashSet<SerializableLocation>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                plugin.getLogger().severe("Could not load breakable spawners data");
                plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
            }
        }
        plugin.getLogger().info("Loaded " + BreakablePlayerSpawners.breakableSpawnersSet.size() + " breakable spawners");
    }

    public static void saveBreakableSpawnersData() {

        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        try {
            File file = new File(plugin.getDataFolder(), "breakableSpawners.dat");
            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(BreakablePlayerSpawners.breakableSpawnersSet);
            objectOutputStream.close();
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save breakable spawners data");
            plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
        }
    }

}
