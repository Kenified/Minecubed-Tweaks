package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.SerializableLocation;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;

public class CustomLeavesPersistent implements Listener {
    Tweaks plugin;
    public static HashSet<SerializableLocation> persistentLeavesSet = new HashSet<>();

    public CustomLeavesPersistent(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeavesPlace(final BlockPlaceEvent event){
        if (event.isCancelled()) return;

        final Block block = event.getBlock();
        if (!(block.getBlockData() instanceof Leaves)) return;

        persistentLeavesSet.add(new SerializableLocation(block.getLocation()));
    }

    @EventHandler
    public void onLeavesBreak(final BlockBreakEvent event) {
        if (event.isCancelled()) return;

        final Block block = event.getBlock();
        if (!(block.getBlockData() instanceof Leaves)) return;

        SerializableLocation serializableLocation = new SerializableLocation(block.getLocation());
        if (persistentLeavesSet.contains(serializableLocation)) {
            persistentLeavesSet.remove(serializableLocation);
        }
    }


    public static void loadPersistentLeavesData() {
        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        File file = new File(plugin.getDataFolder(), "persistentLeaves.dat");
        if (file.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                CustomLeavesPersistent.persistentLeavesSet = (HashSet<SerializableLocation>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                plugin.getLogger().severe("Could not load persistent leaves data");
                plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
            }
        }
        plugin.getLogger().info("Loaded " + CustomLeavesPersistent.persistentLeavesSet.size() + " persistent leaves");
    }

    public static void savePersistentLeavesData() {

        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        try {
            File file = new File(plugin.getDataFolder(), "persistentLeaves.dat");
            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(CustomLeavesPersistent.persistentLeavesSet);
            objectOutputStream.close();
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save persistent leaves data data");
            plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
        }
    }

}
