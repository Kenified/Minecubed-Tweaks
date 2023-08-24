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
    
    
}
