package com.minecubedmc;

//import com.minecubedmc.items.Items;
import com.minecubedmc.listeners.*;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Tweaks extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getLogger().info("Enabled");
        this.registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners() {
        List.of(
                        new EntityDamageByEntityListener(this),
                        new PlayerInteractEntityListener(this),
                        new PlayerInteractAtEntityListener(this),
                        new HangingBreakByEntityListener(this),
                        new IronGolemItemDrop(this),
                        new SaplingGrowEvent(this)

                ).
                forEach(
                        listener -> this.getServer().getPluginManager()
                                .registerEvents(listener, this)
                );
    }
}
