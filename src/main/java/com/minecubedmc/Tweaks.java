package com.minecubedmc;

import com.earth2me.essentials.Essentials;
import com.minecubedmc.commands.DyeCommand;
import com.minecubedmc.commands.EndRatingPeriod;
import com.minecubedmc.commands.PlayerInfoCommand;
import com.minecubedmc.features.*;
import com.minecubedmc.tasks.*;
import com.minecubedmc.util.HeadDBLoad;
import com.minecubedmc.util.ItemsAdderLoad;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.casperge.realisticseasons.api.SeasonsAPI;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public final class Tweaks extends JavaPlugin{

    private static Tweaks instance;
    private static final String[] worldsList = new String[]{/*"world_spawn",*/ "world_shops", "dungeon_vexkin"}; //arena_pvp
    private static PluginManager pluginManager;
    private static SeasonsAPI seasonsAPI;
    private static String season;
    private static Essentials essentials;
    private static HeadDatabaseAPI headDatabaseAPI;
    private static boolean isIALoaded = false;
    private static boolean isHeadDBLoaded = false;
    
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        pluginManager = getServer().getPluginManager();

        if (pluginManager.isPluginEnabled("RealisticSeasons")) {
            // Get the seasons API
            seasonsAPI = SeasonsAPI.getInstance();
            //Check for season in overworld every 10 seconds
            new GetSeason(this, seasonsAPI).runTaskTimer(this, 0, 200);
            //Freeze players if it's winter, and they are inside water; every 2 ticks
            new FreezeInWinter(this, 2).runTaskTimer(this, 0, 2);
        }

        if (pluginManager.isPluginEnabled("Essentials")) {
            essentials = (Essentials) this.getServer().getPluginManager().getPlugin("Essentials");
        }

//        loadInfectionData();
//        Bukkit.getScheduler().runTaskTimer(this, new Infect(this), 0, 200);
//        new Infect(this).runTaskTimer(this, 0, 200);

        saveDefaultConfig();
        this.registerListeners();
        this.registerCommands();
        CustomTripwireBlockSystem.registerBlocks();
        CustomLeavesPersistent.loadPersistentLeavesData();

        //Sync world time with overworld
        if (Arrays.stream(worldsList)
            .allMatch(worldName -> this.getServer().getWorld(worldName) != null)
        ){
            new SyncWorldTimeEvent(this, worldsList).runTaskTimer(this, 0, 2);
        }

        //Faster paths
//        new FasterPaths(this).runTaskTimer(this, 0, 2);

        // Save plugin data every 3 minutes
        new SaveData().runTaskTimer(this, 0, 3600);
        
        this.getLogger().info("Plugin enabled");
    }

    @Override
    public void onDisable() {
//        saveInfectionData();
        CustomLeavesPersistent.savePersistentLeavesData();
    }

    private void registerListeners() {
        List.of(
            new ItemsAdderLoad(this),
            new HeadDBLoad(this),
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
            new CustomTripwireBlockSystem(this),
            new FertilizableFlowers(this),
            new SelfPlantingSaplings(this),
            new CustomLeavesDrops(this),
            new CustomSaplings(),
            new NerfChiseledBookshelfLoot(this),
            new IllagerEnhancments(this),
            new NerfCoinDrops(this),
            new SortingTool(this),
            new StaffsAndGuns(this),
            new CustomLeavesPersistent(this),
            new DangerousDark(this),
            new TougherPets(this),
            new RankingSystem(this)
//            new BreakablePlayerSpawners(this)
//            , new Diseases()
        ).
        forEach(
            listener -> this.getServer().getPluginManager()
                .registerEvents(listener, this)
        );
    }
    
    private void registerCommands(){
        List.of(
            new DyeCommand("dye"),
//            new InfectCommand("addinfection"),
//            new InfectionsCommand("getinfections"),
//            new RemoveInfectionCommand("removeinfection"),
            new PlayerInfoCommand(this, "playerinfo"),
            new EndRatingPeriod( "endratingperiod")
            
        ).
        forEach(
            command ->  this.getServer().getCommandMap()
                .register("minecubed", command)
        );
    }

    public static Tweaks getInstance(){
        return instance;
    }
    public static void setIALoaded() {
        isIALoaded = true;
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

    public static void setHeadDBLoaded() {
        isHeadDBLoaded = true;
    }

    public static @Nullable HeadDatabaseAPI getHeadDatabaseAPI() {
        if (!isHeadDBLoaded){
            // TODO: find more elegant way when not lazy
            Tweaks.getPlugin(Tweaks.class).getLogger().warning("HeadDatabaseAPI is not loaded");
            return null;
        }

        if (headDatabaseAPI == null) {
            headDatabaseAPI = new HeadDatabaseAPI();
        }

        return headDatabaseAPI;
    }
    
    public static PluginManager getPluginManager(){ return pluginManager; }
}
