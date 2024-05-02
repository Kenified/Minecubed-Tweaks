package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.items.CustomTripwireBlock;
import com.minecubedmc.util.Cache;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CustomTripwireBlockSystem implements Listener {
    Tweaks plugin;
    
    public CustomTripwireBlockSystem(Tweaks plugin){
        this.plugin = plugin;
    }
    
    private static final ConcurrentHashMap<String, CustomTripwireBlock> CustomTripwireBlocks = new ConcurrentHashMap<>();
    
    public static void registerBlocks(){
        CustomTripwireBlocks.put("minecubed:tripwire",              new CustomTripwireBlock(0));
        CustomTripwireBlocks.put("minecubed:grown_eggplant",        new CustomTripwireBlock(1));
        CustomTripwireBlocks.put("minecubed:grown_corn",            new CustomTripwireBlock(2));
        CustomTripwireBlocks.put("minecubed:giant_pumpkin",         new CustomTripwireBlock(3));
        CustomTripwireBlocks.put("minecubed:giant_strawberry",      new CustomTripwireBlock(4));
        CustomTripwireBlocks.put("minecubed:giant_onion",           new CustomTripwireBlock(5));
        CustomTripwireBlocks.put("minecubed:giant_lettuce",         new CustomTripwireBlock(6));
        CustomTripwireBlocks.put("minecubed:giant_cocoa",           new CustomTripwireBlock(7));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_north",     new CustomTripwireBlock(8));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_east",      new CustomTripwireBlock(9));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_south",     new CustomTripwireBlock(10));
        CustomTripwireBlocks.put("minecubed:giant_cocoa_west",      new CustomTripwireBlock(11));
        CustomTripwireBlocks.put("minecubed:wild_eggplants",        new CustomTripwireBlock(12));
        CustomTripwireBlocks.put("minecubed:wild_corn",             new CustomTripwireBlock(13));
        CustomTripwireBlocks.put("minecubed:wild_tomatoes",         new CustomTripwireBlock(14));
        CustomTripwireBlocks.put("minecubed:wild_potatoes",         new CustomTripwireBlock(15));
        CustomTripwireBlocks.put("minecubed:wild_onions",           new CustomTripwireBlock(16));
        CustomTripwireBlocks.put("minecubed:wild_lettuce",          new CustomTripwireBlock(17));
        CustomTripwireBlocks.put("minecubed:wild_carrots",          new CustomTripwireBlock(18));
        CustomTripwireBlocks.put("minecubed:jacaranda_droopy",      new CustomTripwireBlock(19));
        CustomTripwireBlocks.put("minecubed:toadstool",             new CustomTripwireBlock(20));
        CustomTripwireBlocks.put("minecubed:flower_cover_aqua",     new CustomTripwireBlock(21));
        CustomTripwireBlocks.put("minecubed:flower_cover_blue",     new CustomTripwireBlock(22));
        CustomTripwireBlocks.put("minecubed:flower_cover_indigo",   new CustomTripwireBlock(23));
        CustomTripwireBlocks.put("minecubed:flower_cover_pink",     new CustomTripwireBlock(24));
        CustomTripwireBlocks.put("minecubed:flower_cover_red",      new CustomTripwireBlock(25));
        CustomTripwireBlocks.put("minecubed:flower_cover_yellow",   new CustomTripwireBlock(26));
        CustomTripwireBlocks.put("minecubed:wild_wheat",            new CustomTripwireBlock(27));
        CustomTripwireBlocks.put("minecubed:cartwheel_flower",      new CustomTripwireBlock(28));
        CustomTripwireBlocks.put("minecubed:seashell",              new CustomTripwireBlock(29));
        CustomTripwireBlocks.put("minecubed:sea_oats",              new CustomTripwireBlock(30));
        CustomTripwireBlocks.put("minecubed:beachgrass",            new CustomTripwireBlock(31));
        CustomTripwireBlocks.put("minecubed:tall_beachgrass",       new CustomTripwireBlock(32));
        CustomTripwireBlocks.put("minecubed:white_searocket",       new CustomTripwireBlock(33));
        CustomTripwireBlocks.put("minecubed:pink_searocket",        new CustomTripwireBlock(34));
        CustomTripwireBlocks.put("minecubed:mycelium_sprouts",      new CustomTripwireBlock(35));
        CustomTripwireBlocks.put("minecubed:mycelium_roots",        new CustomTripwireBlock(36));
        CustomTripwireBlocks.put("minecubed:torchflower",           new CustomTripwireBlock(37));
        CustomTripwireBlocks.put("minecubed:frosty_fern",           new CustomTripwireBlock(38));
        CustomTripwireBlocks.put("minecubed:frosty_grass",          new CustomTripwireBlock(39));
        CustomTripwireBlocks.put("minecubed:cotton_grass",          new CustomTripwireBlock(40));
        CustomTripwireBlocks.put("minecubed:goldenrod",             new CustomTripwireBlock(41));
        CustomTripwireBlocks.put("minecubed:giant_carrot",          new CustomTripwireBlock(42));
        CustomTripwireBlocks.put("minecubed:giant_melon",           new CustomTripwireBlock(43));
        CustomTripwireBlocks.put("minecubed:giant_potato",          new CustomTripwireBlock(44));
        CustomTripwireBlocks.put("minecubed:giant_wheat",           new CustomTripwireBlock(45));
        CustomTripwireBlocks.put("minecubed:giant_wart",            new CustomTripwireBlock(46));
        CustomTripwireBlocks.put("minecubed:giant_tomato",          new CustomTripwireBlock(47));
        
        // TODO: find more elegant way when not lazy
        Tweaks.getPlugin(Tweaks.class).getLogger().info("Registered tripwire blocks");
    }
    
    public static CustomTripwireBlock getCustomBlock(final @NotNull String ID){
        return CustomTripwireBlocks.get(ID);
    }
    
    public static String[] getCustomBlockList(){
        return CustomTripwireBlocks.keySet().toArray(new String[0]);
    }
    
    @EventHandler
    public void blockBreakRelativeUp(final BlockBreakEvent event) {
        Block blockRelativeUp = event.getBlock().getRelative(BlockFace.UP);
        
        // If block is not tripwire, return
        if (!blockRelativeUp.getType().equals(Material.TRIPWIRE)){
            return;
        }

        handleBlockBreak(blockRelativeUp, null);
        event.setDropItems(false);
    }
    
    @EventHandler
    public void blockBreakEvent(final BlockBreakEvent event){
        if (event.isCancelled()){
            return;
        }

        Block brokenBlock = event.getBlock();
        
        if (!brokenBlock.getType().equals(Material.TRIPWIRE)){
            return;
        }
        
        // Don't drop custom item if player is in creative
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            return;
        }

        Player player = event.getPlayer();
        handleBlockBreak(brokenBlock, player);
        event.setDropItems(false);
    }
    
    @EventHandler
    public void blockBreakRelativeDown(final BlockBreakEvent event) {
        if (event.isCancelled()){
            return;
        }

        Block blockRelativeDown = event.getBlock().getRelative(BlockFace.DOWN);
        
        // If block is not tripwire, return
        if (!blockRelativeDown.getType().equals(Material.TRIPWIRE)){
            return;
        }

        handleBlockBreak(blockRelativeDown, null);
        event.setDropItems(false);
    }
    
    public void handleBlockBreak(Block block, Player player){


        Arrays.stream(getCustomBlockList()).sequential().forEach(customBlockID -> {
        
            // Check if block matches custom block
            if (!block.getBlockData().equals(getCustomBlock(customBlockID).getBlockData())){
                return;
            }
        
            Random random = new Random();
        
            // Special cases
            switch (customBlockID) {
                case "minecubed:grown_eggplant" -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem("minecubed:eggplant"));

                    ItemStack eggplantSeeds = Cache.getCustomItem("minecubed:eggplant_seeds");
                    eggplantSeeds.setAmount(random.nextInt(2) + 1);
                    block.getWorld().dropItemNaturally(block.getLocation(), eggplantSeeds);
                }
                case "minecubed:wild_eggplants" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null && player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else {
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem("minecubed:eggplant"));
    
                        ItemStack eggplantSeeds = Cache.getCustomItem("minecubed:eggplant_seeds");
                        eggplantSeeds.setAmount(random.nextInt(2) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), eggplantSeeds);
                    }
                }
                case "minecubed:grown_corn" -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem("minecubed:corn"));

                    ItemStack cornSeeds = Cache.getCustomItem("minecubed:corn_seeds");
                    cornSeeds.setAmount(random.nextInt(2) + 1);
                    block.getWorld().dropItemNaturally(block.getLocation(), cornSeeds);
                }
                case "minecubed:wild_corn" -> {
                    // Delete the block
//                    block.setType(Material.AIR);
    
                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem("minecubed:corn"));
    
                        ItemStack cornSeeds = Cache.getCustomItem("minecubed:corn_seeds");
                        cornSeeds.setAmount(random.nextInt(2) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), cornSeeds);
                    }
                }
                case "minecubed:wild_tomatoes" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BEETROOT));

                        ItemStack tomatoSeeds = new ItemStack(Material.BEETROOT_SEEDS);
                        tomatoSeeds.setAmount(random.nextInt(2) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), tomatoSeeds);
                    }
                }
                case "minecubed:wild_potatoes" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{

                        ItemStack potatoes = new ItemStack(Material.POTATO);
                        potatoes.setAmount(random.nextInt(3) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), potatoes);
                    }
                }
                case "minecubed:wild_onions" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem("minecubed:onion"));

                        ItemStack onionSeeds = Cache.getCustomItem("minecubed:onion_seeds");
                        onionSeeds.setAmount(random.nextInt(2) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), onionSeeds);
                    }
                }
                case "minecubed:wild_lettuce" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem("minecubed:lettuce"));

                        ItemStack lettuceSeeds = Cache.getCustomItem("minecubed:lettuce_seeds");
                        lettuceSeeds.setAmount(random.nextInt(2) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), lettuceSeeds);
                    }
                }
                case "minecubed:wild_carrots" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{

                        ItemStack carrots = new ItemStack(Material.CARROT);
                        carrots.setAmount(random.nextInt(3) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), carrots);
                    }
                }
                case "minecubed:wild_wheat" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null &&player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)){
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                    // else drop the produce and seeds
                    else{
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WHEAT));

                        ItemStack tomatoSeeds = new ItemStack(Material.WHEAT_SEEDS);
                        tomatoSeeds.setAmount(random.nextInt(2) + 1);
                        block.getWorld().dropItemNaturally(block.getLocation(), tomatoSeeds);
                    }
                }
                case "minecubed:mycelium_sprouts", "minecubed:mycelium_roots", "minecubed:frosty_fern", "minecubed:frosty_grass" -> {
                    // Delete the block
//                    block.setType(Material.AIR);

                    // If player is holding shears, drop the block
                    if (player != null && player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS)) {
                        block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                    }
                }
                // Logic for non-special blocks
                default -> {
                    // Set block to air
//                    block.setType(Material.AIR);
                    // Drop custom item
                    block.getWorld().dropItemNaturally(block.getLocation(), Cache.getCustomItem(customBlockID));
                }
            }
        });
    }
    @EventHandler
    public void blockPhysicsEvent(final BlockFromToEvent event) {
        if (event.isCancelled()){
            return;
        }

        Block brokenBlock = event.getToBlock();
        
        if (!brokenBlock.getType().equals(Material.TRIPWIRE)){
            return;
        }
        
        // Iterate through the custom block list and verify if the broken block matches any of them
        Arrays.stream(getCustomBlockList()).sequential().forEach(customBlockID -> {
            
            // Check if block matches custom block
            if (!brokenBlock.getBlockData().equals(getCustomBlock(customBlockID).getBlockData())){
                return;
            }
            
            // Set block to air
            brokenBlock.setType(Material.AIR);
            event.setCancelled(true);

            // Drop custom item
            brokenBlock.getWorld().dropItemNaturally(brokenBlock.getLocation(), Cache.getCustomItem(customBlockID));
            
            
        });
    }
    
    @EventHandler
    public void blockPlaceEvent(final BlockPlaceEvent event){
        if (event.isCancelled()){
            return;
        }

        Block eventBlock = event.getBlock();
        final ItemStack eventItem = event.getItemInHand();

        // Don't allow placing tripwire
        if (eventBlock.getType().equals(Material.TRIPWIRE)){
            if (eventBlock.getBlockData().equals(getCustomBlock("minecubed:tripwire").getBlockData())){
                event.setCancelled(true);
                return;
            }
        }

        // Iterate through the list of custom blocks and determine if they have been placed
        Arrays.stream(getCustomBlockList()).sequential().forEach(customBlockID -> {
            // Ignore grown blocks since they cannot be placed
            if (customBlockID.equals("minecubed:grown_eggplant") ||
                customBlockID.equals("minecubed:grown_corn")
            ){
                return;
            }

            ItemStack customItem;


            // Check if item exists
            try {
                customItem = Cache.getCustomItem(customBlockID);
            } catch (NullPointerException e) {
                plugin.getLogger().severe("Could not find custom item: " + customBlockID);
                return;
            }

            // If item is null, return
            if (customItem == null) {
                return;
            }

            // If item is not custom block, return
            if (!eventItem.isSimilar(customItem)) {
                return;
            }

            // Place custom block
            getCustomBlock(customBlockID).place(eventBlock.getLocation());

            // Remove item from player if not in creative
            if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                eventItem.subtract(1);
            }
        });
    }
}
