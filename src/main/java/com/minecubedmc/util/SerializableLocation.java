package com.minecubedmc.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.Serializable;
import java.util.Objects;

public class SerializableLocation implements Serializable {

    private final String world;
    final int x;
    final int y;
    final int z;

    public SerializableLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

    public SerializableLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }



    public Location getBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableLocation other = (SerializableLocation) o;
        return x == other.x && y == other.y && z == other.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
