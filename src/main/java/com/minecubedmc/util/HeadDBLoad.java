package com.minecubedmc.util;

import com.minecubedmc.Tweaks;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class HeadDBLoad implements Listener {

    Tweaks plugin;

    public HeadDBLoad(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onHeadDBLoad(final me.arcaniax.hdb.api.DatabaseLoadEvent event) {
        Tweaks.setHeadDBLoaded();
        plugin.getLogger().info("HeadDB hooked");
    }
}
