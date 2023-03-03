//package com.minecubedmc.runanbles.disabled;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.Chunk;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.block.Biome;
//import org.bukkit.block.Block;
//import org.bukkit.block.BlockFace;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Random;
//
//public class MeltSnow implements Runnable{
//
//    Tweaks plugin;
//    World world;
//
//    public MeltSnow(Tweaks plugin){
//        this.plugin = plugin;
//        plugin.getLogger().warning("Started MeltSnow task");
//        world = plugin.getServer().getWorld("world");
//    }
//
//    @Override
//    public void run() {
//        @NotNull Chunk[] loadedChunks = world.getLoadedChunks();
//
//        for (Chunk chunk : loadedChunks){
//            //Get random local coordinates inside chunk 0-15 and then convert to global coordinates
//            Location worldLocation = chunk.getBlock(new Random().nextInt(16), 0 , new Random().nextInt(16)).getLocation();
//            //Get the coordinates of the highest solid block at that global location
//            Location blockLocation = world.getHighestBlockAt(worldLocation.getBlockX(), worldLocation.getBlockZ()).getRelative(BlockFace.UP).getLocation();
//
////            //Shift location up by 1
////            blockLocation.setY(blockLocation.getY() + 1);
//
//            //Get block at those coordinates
//            Block block = blockLocation.getBlock();
//            //Get biome that block is in
//            Biome biome = block.getBiome();
//
//            //Check if the block is snow and if its in plains biome
//            if (block.getType().equals(Material.SNOW) && biome.equals(Biome.PLAINS)){
//                //Replace that block with air
//                block.setType(Material.AIR);
//            }
//        }
//    }
//}
