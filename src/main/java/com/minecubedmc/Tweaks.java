package com.minecubedmc;

import com.earth2me.essentials.Essentials;
import com.minecubedmc.commands.DyeCommand;
import com.minecubedmc.features.*;
import com.minecubedmc.runanbles.FreezeInWinter;
import com.minecubedmc.runanbles.GetSeason;
import com.minecubedmc.runanbles.SyncWorldTimeEvent;
import com.minecubedmc.runanbles.TickInventories;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.casperge.realisticseasons.api.SeasonsAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class Tweaks extends JavaPlugin implements Listener{
    
    private static SeasonsAPI seasonsAPI;
    private static String season;
    private static Essentials essentials;
    private static HeadDatabaseAPI headDatabaseAPI;
//    private static BanManagerPlugin banManager;
    private static boolean isIALoaded = false;
    
    @Override
    public void onEnable() {
        // Plugin startup logic
        seasonsAPI = SeasonsAPI.getInstance();
        essentials = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
//        banManager = BanManagerPlugin.getInstance();
        
        
        saveDefaultConfig();
        this.registerListeners();
        this.registerCommands();
    
        //Check for season in overworld every 10 seconds
        Bukkit.getScheduler().runTaskTimer(this, new GetSeason(this), 0, 200);
        //Sync world time with overworld
//        Bukkit.getScheduler().runTaskAsynchronously(this, new SyncWorldTimeEvent(this,
//            new String[]{"world_spawn", "world_shops", "dungeon_vexkin", "arena_pvp"}
//        ));
        //Freeze players if it's winter, and they are inside water; every 2 ticks
        Bukkit.getScheduler().runTaskTimer(this, new FreezeInWinter(this, 2), 0, 2);
        Bukkit.getScheduler().runTaskTimer(this, new TickInventories(this), 0, 20);
        this.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() { }

    private void registerListeners() {
        List.of(
//            new SaplingGrowEvent(this),
//            new MobSpawn(this),
//            new EntityDamageByEntityListener(this),
//            new PlayerInteractEntityListener(this),
//            new PlayerInteractAtEntityListener(this),
//            new HangingBreakByEntityListener(this),
            this,
            new CopperBlockDecayAndWax(),
            new MobDrops(),
            new GiantCrops(this),
            new ChangeVanillaFoodLevels(this),
            new DirtyWaterBottle(),
            new WaterCauldronPurify(),
            new FillCauldronWithDirtyWater(),
            new MilkBottleFromCow(),
            new TreeBarkStrip(),
            new ChickenExtraFeathers(),
            new ExtraFruitDrops(),
            new NoTorchesInDeepDark(),
            new LimitFoodLevel(),
            new NoXPDrop(),
            new FixIABottleBug(),
            new BlockBarrierPlacement(),
            new LimitFallDamage(),
            new CustomCrops(),
            new FallToOverworld(this),
            new CustomBlockSilktouch(),
            new BeeNoAngy(),
            new AFKFoxHarvestBlock(this),
            new PlayerInfoNotify(this),
            new AgeItems(this)
        ).
        forEach(
            listener -> this.getServer().getPluginManager()
                .registerEvents(listener, this)
        );
    }
    
    private void registerCommands(){
        List.of(
            new DyeCommand("dye")
            
        ).
        forEach(
            command ->  this.getServer().getCommandMap()
                .register("minecubed", command)
        );
    }
    
    @EventHandler
    public void onItemAdderDataLoad(final ItemsAdderLoadDataEvent event) {
        Tweaks.isIALoaded = true;
        this.getLogger().warning("ItemsAdder hooked");
        this.getLogger().warning("Loaded: " + isIALoaded());
    }
    
    public static boolean isIALoaded() {
        return isIALoaded;
    }
    
    public static SeasonsAPI getSeasonsAPI(){
        return seasonsAPI;
    }

    public static void setSeason(String season) {
        Tweaks.season = season;
    }

    public static String getSeason() {
        return season;
    }
    
    public static Essentials getEssentials(){
        return essentials;
    }
    
    @EventHandler
    public void onDatabaseLoad(final DatabaseLoadEvent event) {
        headDatabaseAPI = new HeadDatabaseAPI();
    }
    
    public static HeadDatabaseAPI getHeadDatabaseAPI() {
        return headDatabaseAPI;
    }
    
//    public static BanManagerPlugin getBanManager() { return banManager; }
}
