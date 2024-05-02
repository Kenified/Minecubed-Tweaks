package com.minecubedmc.commands;

import com.minecubedmc.modules.Disease.Diseases;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveInfectionCommand extends Command {

    public RemoveInfectionCommand(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {


        //Check if sender has permission
        if (!commandSender.hasPermission("group.helper") || !commandSender.isOp()){
            commandSender.sendMessage("You do not have permission to use this command.");
            return false;
        }

        //Return if there aren't any arguments
        if (strings.length == 0){
            commandSender.sendMessage("Please provide player username and disease ID.");
            return false;
        }

        Player player = Bukkit.getPlayer(strings[0]);

        if (player == null){
            commandSender.sendMessage("Player not found.");
            return false;
        }

        if(!Diseases.playerDiseaseDataMap.containsKey(player.getUniqueId())){
            commandSender.sendMessage("Player not found.");
            return false;
        }

        Diseases.playerDiseaseDataMap.get(player.getUniqueId()).removeInfection(strings[1]);

        return true;
    }
}
