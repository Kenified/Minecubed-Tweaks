package com.minecubedmc.test;

import com.minecubedmc.Tweaks;
import org.bukkit.event.Listener;

public class Test implements Listener {
    
    Tweaks plugin;
    
    public Test(Tweaks plugin){
        this.plugin = plugin;
    }
    
//    @EventHandler
//    public void onGrow(StructureGrowEvent event){
//        List<BlockState> blocks = event.getBlocks();
//        blocks.forEach(blockState -> {
//            Leaves leaves = (Leaves) blockState.getBlockData();
//            leaves.setDistance(1);
//            blockState.setBlockData(leaves);
//        });
//    }
//    @EventHandler
//    public void test(BlockEvent event){
//        plugin.getLogger().info(event.getBlock().toString());
//    }
    

}
