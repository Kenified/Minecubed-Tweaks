package com.minecubedmc;

import com.minecubedmc.listeners.*;

import me.casperge.realisticseasons.api.SeasonsAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Tweaks extends JavaPlugin {

    private SeasonsAPI sapi;

    @Override
    public void onEnable() {
        // Plugin startup logic
        sapi = SeasonsAPI.getInstance();
        saveDefaultConfig();

        this.registerListeners();
        this.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
    }

    private void registerListeners() {
        List.of(
                        new EntityDamageByEntityListener(this),
                        new PlayerInteractEntityListener(this),
                        new PlayerInteractAtEntityListener(this),
                        new HangingBreakByEntityListener(this),
                        new MobDeath(this),
//                        new SaplingGrowEvent(this),
                        new CropGrowEvent(this),
                        new PlayerItemConsumeListener(this),
                        new DirtyWaterBottle(this),
                        new PurifiedWaterCauldron(this),
                        new FillCauldronWithDirtyWater(this),
                        new MilkBottleFromCow(this),
                        new TreeBarkStrip(this),
                        new ChickenFeather(this),
                        new LeafDecay(this),
                        new TorchInDeepDark(this),
                        new MobSpawn(this),
                        new FoodDrain(this),
                        new NoXPDrop(this)
                ).
                forEach(
                        listener -> this.getServer().getPluginManager()
                                .registerEvents(listener, this)
                );
    }

    public SeasonsAPI getSapi(){
        return sapi;
    }
}
