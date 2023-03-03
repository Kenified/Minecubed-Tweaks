package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class FallToOverworld implements Listener {
    Tweaks plugin;
    public FallToOverworld(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void moveEvent(final PlayerMoveEvent event){
        final Player player = event.getPlayer();
        final World playerWorld = player.getWorld();

        if (playerWorld.getEnvironment().equals(World.Environment.THE_END)){
            final Location playerLocation = player.getLocation();

            if (playerLocation.getY() < -63 ){
                final World overworld = plugin.getServer().getWorld("world");
                final Location overworldLocation = new Location(overworld, playerLocation.getX(), 500, playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());

                player.teleport(overworldLocation, PlayerTeleportEvent.TeleportCause.END_PORTAL);
            }
        }
    }
}
