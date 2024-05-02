package com.minecubedmc.util;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ItemsAdderLoad implements Listener {
    
    Tweaks plugin;
    
    public ItemsAdderLoad(Tweaks plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onItemAdderDataLoad(final ItemsAdderLoadDataEvent event) {
        Cache.resetItemCache();
        Tweaks.setIALoaded();
        plugin.getLogger().info("ItemsAdder hooked");
    }
}
