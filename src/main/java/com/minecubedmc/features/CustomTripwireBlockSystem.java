package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CustomTripwireBlockSystem implements Listener {
    Tweaks plugin;
    
    public CustomTripwireBlockSystem(Tweaks plugin){
        this.plugin = plugin;
    }
    
    @EventHandler
    public void blockPlaceEvent(final BlockPlaceEvent event){
        Block eventBlock = event.getBlock();
        Player eventPlayer = event.getPlayer();
        final ItemStack eventItem = event.getItemInHand();
        
        if (eventBlock.getType().equals(Material.TRIPWIRE)){
            if (eventBlock.getBlockData().equals(Cache.getCustomBlock("minecubed:tripwire").getBlockData())){
                event.setCancelled(true);
                return;
            }
        }
        
        handlePlacement(eventItem, eventBlock, "minecubed:toadstool", eventPlayer);
        handlePlacement(eventItem, eventBlock, "minecubed:wild_carrots", eventPlayer);
        
    }
    private void handlePlacement(ItemStack eventItem, Block eventBlock, String customBlockID, Player eventPlayer){
        handlePlacement(eventItem, eventBlock, customBlockID, customBlockID, eventPlayer);
    }
    
    private void handlePlacement(ItemStack eventItem, Block eventBlock, String customBlockID, String customItemID, Player eventPlayer){
        if (eventItem.isSimilar(Cache.getCustomItem(customItemID))) {
            Cache.getCustomBlock(customBlockID).place(eventBlock.getLocation());
            if (!eventPlayer.getGameMode().equals(GameMode.CREATIVE)){
                eventItem.subtract(1);
            }
        }
    }
}
