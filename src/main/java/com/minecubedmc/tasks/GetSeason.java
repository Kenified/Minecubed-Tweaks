package com.minecubedmc.tasks;

import com.minecubedmc.Tweaks;
import me.casperge.realisticseasons.api.SeasonsAPI;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class GetSeason extends BukkitRunnable {

    Tweaks plugin;
    SeasonsAPI seasonsAPI;
    World world;

    public GetSeason(Tweaks plugin, SeasonsAPI seasonsAPI) {
        this.plugin = plugin;
        this.seasonsAPI = seasonsAPI;
        world = plugin.getServer().getWorld("world");
    }

    @Override
    public void run() {
        
        String season = seasonsAPI.getSeason(world).toString();

        if (!season.equals(Tweaks.getSeason())){
            Tweaks.setSeason(season);
        }
    }
}
