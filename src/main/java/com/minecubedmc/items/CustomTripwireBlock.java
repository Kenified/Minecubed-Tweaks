package com.minecubedmc.items;

import com.minecubedmc.util.BasicUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Tripwire;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class CustomTripwireBlock {
    private final Tripwire customBlockData;
    
    public CustomTripwireBlock(int ID) {
        boolean[] states = BasicUtils.convertToBinaryArray(ID);
        customBlockData = (Tripwire) Material.TRIPWIRE.createBlockData();
        customBlockData.setAttached(states[0]);
        customBlockData.setDisarmed(states[1]);
        customBlockData.setFace(BlockFace.EAST, states[2]);
        customBlockData.setFace(BlockFace.NORTH, states[3]);
        customBlockData.setPowered(states[4]);
        customBlockData.setFace(BlockFace.SOUTH, states[5]);
        customBlockData.setFace(BlockFace.WEST, states[6]);
        
    }
    
    public void place(final @NotNull Location location){
        // Get block in world
        Block block = location.getBlock();
        
        // Set the block to tripwire
        block.setType(Material.TRIPWIRE);
        
        // Get the Blockstate
        BlockState tripwireBlockState = block.getState();
        
        // Set the new Blockdata
        tripwireBlockState.setBlockData(customBlockData);
        
        // Update the Blockstate with the new Blockdata
        tripwireBlockState.update();
    }
    
    public BlockData getBlockData(){
        return customBlockData;
    }
    
    
    public static void seedBlocks(ConcurrentHashMap<String, CustomTripwireBlock> hashMap){
        hashMap.put("minecubed:tripwire",           new CustomTripwireBlock(0));
        hashMap.put("minecubed:grown_eggplant",     new CustomTripwireBlock(1));
        hashMap.put("minecubed:grown_corn",         new CustomTripwireBlock(2));
        hashMap.put("minecubed:giant_pumpkin",      new CustomTripwireBlock(3));
        hashMap.put("minecubed:giant_strawberry",   new CustomTripwireBlock(4));
        hashMap.put("minecubed:giant_onion",        new CustomTripwireBlock(5));
        hashMap.put("minecubed:giant_lettuce",      new CustomTripwireBlock(6));
        hashMap.put("minecubed:giant_cocoa",        new CustomTripwireBlock(7));
        hashMap.put("minecubed:giant_cocoa_north",  new CustomTripwireBlock(8));
        hashMap.put("minecubed:giant_cocoa_east",   new CustomTripwireBlock(9));
        hashMap.put("minecubed:giant_cocoa_south",  new CustomTripwireBlock(10));
        hashMap.put("minecubed:giant_cocoa_west",   new CustomTripwireBlock(11));
        hashMap.put("minecubed:wild_eggplants",     new CustomTripwireBlock(12));
        hashMap.put("minecubed:wild_corn",          new CustomTripwireBlock(13));
        hashMap.put("minecubed:wild_tomatoes",      new CustomTripwireBlock(14));
        hashMap.put("minecubed:wild_potatoes",      new CustomTripwireBlock(15));
        hashMap.put("minecubed:wild_onions",        new CustomTripwireBlock(16));
        hashMap.put("minecubed:wild_lettuce",       new CustomTripwireBlock(17));
        hashMap.put("minecubed:wild_carrots",       new CustomTripwireBlock(18));
        hashMap.put("minecubed:jacaranda_droopy",   new CustomTripwireBlock(19));
        hashMap.put("minecubed:toadstool",          new CustomTripwireBlock(20));
    }
    
    
}
