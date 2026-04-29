package com.minecubedmc.commands;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SetHeight extends Command {
    private static final int REF_HEIGHT_MIN = 150;
    private static final double REF_MIN_SCALE = 0.833333;
    private static final double DEFAULT_HEIGHT = 180;
    
    private static final int MIN_HEIGHT_CM = 160;
    private static final int MAX_HEIGHT_CM = 205;
    // Cooldown for each player
    private final ConcurrentHashMap<UUID, Long> cooldowns = new ConcurrentHashMap<>();
    
    public SetHeight(@NotNull String label) {

        super(label);
    }
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        
        
        if (args.length <= 0){
            sender.sendMessage("Usage: /setheight <height>");
            return false;
        }
        
        if (args.length == 1) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("This command can only be used by players.");
                return false;
            }
            
            runCommand(args[0], (Player) sender, sender);
        }
        else if (args.length == 2) {
            // only allow staff and console to set other players' height
            if (!sender.hasPermission("group.helper") && !(sender instanceof Player)) {
                sender.sendMessage("You do not have permission to set other players' height.");
                return false;
            }
            
            Player player = sender.getServer().getPlayer(args[0]);
            
            if (player == null) {
                sender.sendMessage("Player not found.");
                return false;
            }
            
            if (Objects.equals(args[1], "random")) {
                int randomHeight = (int) (Math.random() * (200 - MIN_HEIGHT_CM + 1) + MIN_HEIGHT_CM);
                runCommand(String.valueOf(randomHeight), player, sender);
            }
            else {
                runCommand(args[1], player, sender);
            }
        }
        
        return false;
    }
    
    private boolean runCommand(String height, Player player, CommandSender sender) {
        int cmHeight;
        
        try {
            cmHeight = Integer.parseInt(height);
        } catch (NumberFormatException e) {
            sender.sendMessage("Usage: /setheight <height>");
            sender.sendMessage("Please provide height between " + MIN_HEIGHT_CM + "cm and " + MAX_HEIGHT_CM + "cm.");
            return false;
        }
        
        if (cmHeight < MIN_HEIGHT_CM || cmHeight > MAX_HEIGHT_CM) {
            sender.sendMessage("Please provide height between " + MIN_HEIGHT_CM + "cm and " + MAX_HEIGHT_CM + "cm.");
            return false;
        }
        if (!sender.hasPermission("group.helper")) {
            // Cooldown of 12 hours
            
            if (cooldowns.containsKey(player.getUniqueId())) {
                long lastUse = cooldowns.get(player.getUniqueId());
                long currentTime = System.currentTimeMillis();
                long timePassed = currentTime - lastUse;
                long delay = 12 * 60 * 60 * 1000;
                long timeLeft = delay - timePassed;
                if (timeLeft > 0) {
                    sender.sendMessage("You can only use this command once every 12 hours.");
                    sender.sendMessage("Time left: " + timeLeft / 1000 / 60 / 60 + " hours.");
                    return false;
                }
            }
            
        }
        // Set cooldown only if player is running the command not if someone else sets the height
        if (player.equals(sender)) {
            cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
        }
        
        setPlayerHeight(player, cmHeight);
        sender.sendMessage("Height set to " + cmHeight + "cm.");
        return true;
    }
    
    private void setPlayerHeight(Player player, int cmHeight) {
        if (player == null) {
            return;
        }
        
        // Clamp height to valid range
        int clampedHeight = Math.max(MIN_HEIGHT_CM, Math.min(cmHeight, 200));
        
        // Calculate scale based on height
        double scale = calculateScaleFromHeight(clampedHeight);
        
        // Apply the scale to the player
        applyScaleToPlayer(player, scale);
    }
    
    private double calculateScaleFromHeight(int cmHeight) {
        // Due to floating point precision, 200cm is not exactly 1.111111
        // otherwise player won't fit through 2 block high gap
        if (cmHeight == 200) return 1.111111;
        else return REF_MIN_SCALE + (cmHeight - REF_HEIGHT_MIN) * (1 - REF_MIN_SCALE) / (DEFAULT_HEIGHT - REF_HEIGHT_MIN);
    }
    
    private void applyScaleToPlayer(Player player, double scale) {
        AttributeInstance scaleAttribute = player.getAttribute(Attribute.SCALE);
        
        if (scaleAttribute == null) {
            player.registerAttribute(Attribute.SCALE);
            scaleAttribute = player.getAttribute(Attribute.SCALE);
        }
        
        if (scaleAttribute != null) {
            scaleAttribute.setBaseValue(scale);
        }
    }
}
