package com.minecubedmc.commands;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.BasicUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerInfoCommand extends Command {
    Tweaks plugin;
    public PlayerInfoCommand(Tweaks plugin, String label) {
        super(label);
        this.plugin = plugin;
    }
    
    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        
        //Check if sender has permission
        if (!sender.hasPermission("group.helper")){
            sender.sendMessage("You do not have permission to use this command.");
            return false;
        }
        
        //Return if there aren't any arguments
        if (args.length <= 0){
            sender.sendMessage("Please provide an username.");
            return false;
        }
        
        final Player player = plugin.getServer().getPlayer(args[0]);
        if (player == null) sender.sendMessage("That player doesn't exist");
        else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                final Component playerInfo = BasicUtils.PlayerInfoReport.getPlayerInfoMessage(player);
                
                sender.sendMessage(playerInfo);
            });
        }
        
        return true;
    }
}

