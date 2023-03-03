package com.minecubedmc.runanbles;

import com.minecubedmc.Tweaks;
import me.casperge.realisticseasons.api.SeasonsAPI;
import org.bukkit.World;

public class GetSeason implements Runnable{

    Tweaks plugin;
    World world;

    public GetSeason(Tweaks plugin){
        this.plugin = plugin;
        world = plugin.getServer().getWorld("world");
    }

    @Override
    public void run() {
        
        String season = Tweaks.getSeasonsAPI().getSeason(world).toString();

        if (!season.equals(Tweaks.getSeason())){
            Tweaks.setSeason(season);
        }
    }
}
