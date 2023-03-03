package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.BasicUtils;
import fr.xephi.authme.events.LoginEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashSet;

public class PlayerInfoNotify implements Listener {
    private final Tweaks plugin;
    private final HashSet<Player> staffPlayers;
    public PlayerInfoNotify(Tweaks plugin) {
        this.plugin = plugin;
        staffPlayers = new HashSet<>();
    }
    @EventHandler
    public void onLogin(final LoginEvent event) {
        final Player eventPlayer = event.getPlayer();
    
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Component playerInfo = BasicUtils.PlayerInfoReport.getPlayerInfoMessage(eventPlayer);
            updateStaffPlayerList();
    
            // Send the info message to all online staff players
            staffPlayers.forEach(staffPlayer -> {
                staffPlayer.sendMessage(playerInfo);
            });
            
            // Send message to console
            plugin.getServer().getConsoleSender().sendMessage(playerInfo);
        });
    }
    
    private void updateStaffPlayerList() {
        plugin.getServer().getOnlinePlayers().forEach(onlinePlayer -> {
            // Only add staff players to list
            if (!onlinePlayer.hasPermission("minecubed.staff") && !onlinePlayer.isOp()) {
                return;
            }
        
            // If player is already in set ignore
            if (staffPlayers.contains(onlinePlayer)) {
                return;
            }
        
            staffPlayers.add(onlinePlayer);
        });
    }
}
