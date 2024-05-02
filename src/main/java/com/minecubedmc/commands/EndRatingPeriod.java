package com.minecubedmc.commands;

import com.minecubedmc.modules.Ranking.GlickoRanking;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class EndRatingPeriod extends Command {

    public EndRatingPeriod(@NotNull String name) {
        super(name);
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {


        //Check if sender has permission
        if (!commandSender.hasPermission("group.helper") || !commandSender.isOp()){
            commandSender.sendMessage("You do not have permission to use this command.");
            return false;
        }

        GlickoRanking.endRatingPeriod();

        return true;
    }
}
