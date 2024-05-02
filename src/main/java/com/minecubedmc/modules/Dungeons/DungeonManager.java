package com.minecubedmc.modules.Dungeons;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;

public class DungeonManager {

    private static final HashMap<String, Dungeon> dungeons = new HashMap<>();

    public DungeonManager() {
        World world = Bukkit.getWorld("world");

        dungeons.put(
                // ID
                "test",
                // Dungeon Object
                new Dungeon(
                        // Arena Entrances
                        List.of(
                                new Location(world, 0, 0, 0),
                                new Location(world, 0, 0, 0)
                        )
                )
        );


    }

    public static HashMap<String, Dungeon> getDungeons() {
        return dungeons;
    }

    public static void loadDungeons() {
        dungeons.forEach((name, dungeon) -> {
            dungeon.loadData();
        });
    }

    // Create dungeons that are active when the server starts
    public void createDungeons() {
        dungeons.forEach((name, dungeon) -> {
            if (dungeon.getCooldown() >= 0 ) {
                return;
            }

            dungeon.refresh();
        });
    }

    public void saveDungeonData() {
        dungeons.forEach((name, dungeon) -> {
            dungeon.saveData();
        });

    }
}
