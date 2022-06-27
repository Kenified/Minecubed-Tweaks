package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class LeafDecay implements Listener {

    Tweaks plugin;

    public LeafDecay(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent e){
        Material leaf = e.getBlock().getType();


        switch (leaf){
            case BIRCH_LEAVES:
            {
                dropCustomItem(e, "minecubed:cherry", 3);
                break;
            }
            case MANGROVE_LEAVES:
            {
                dropCustomItem(e, "minecubed:pitaya", 2);
                break;
            }
            case JUNGLE_LEAVES:
            {
                dropCustomItem(e, "minecubed:mango", 3);
                break;
            }
            case ACACIA_LEAVES:
            {
                dropCustomItem(e, "minecubed:banana", 3);
                break;
            }
            case FLOWERING_AZALEA_LEAVES:
            case AZALEA_LEAVES:
            {
                dropCustomItem(e, "minecubed:starfruit", 3);
                break;
            }
            default:
                break;
        }
    }

    private void dropCustomItem(LeavesDecayEvent e, String customItemID, int chance){

        if ( new Random().nextInt(100) > chance - 1 ) {
            return;
        }

        World world = e.getBlock().getWorld();
        ItemStack customItem = CustomStack.getInstance(customItemID).getItemStack();
        world.dropItemNaturally(e.getBlock().getLocation(), customItem);
    }
}

