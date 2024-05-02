package com.minecubedmc.modules.Dungeons;

import com.minecubedmc.util.SerializableLocation;
import org.bukkit.inventory.Inventory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DungeonData implements Serializable {
    public long cooldown;
    public HashMap<SerializableLocation, HashMap<UUID, Inventory>> lootChests;

    public DungeonData() {
        this.cooldown = -1;
        this.lootChests = new HashMap<>();
    }
}
