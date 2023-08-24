package com.minecubedmc.features;

import com.minecubedmc.util.Cache;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TreeBarkStrip implements Listener {

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
        //Check if it is not in offhand
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        //Check if it is right click action
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        //Check if item isn't null and is an axe
        final ItemStack item = event.getItem();
        if (item == null ) {
            return;
        }
    
        final String itemName = item.getType().getKey().toString();
        if (!itemName.endsWith("_axe")){
            return;
        }

        //Check if its non stripped wood block
        final String blockName = block.getType().getKey().toString();
        if (!blockName.contains("_log") && !blockName.contains("_wood")) {
            return;
        }

        //Only allow stripping while crouching
        if ( !(event.getPlayer().isSneaking()) ){
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou need to crouch to strip the log."));
            event.setCancelled(true);
            return;
        }

        //You cannot strip stripped log
        if (blockName.contains("STRIPPED")) {
            return;
        }
    
        final Player player = event.getPlayer();
        
        // Offset drop 1/16th of distance towards player so the item doesn't fly away due to block collision
        Location interactionPoint = event.getInteractionPoint();
        Vector offset = player.getLocation().getDirection().multiply(1/16);
        
        if (interactionPoint == null ) return;
        
        Location dropLocation = interactionPoint.add(offset);
        
        player.getWorld().dropItem(dropLocation, Cache.getCustomItem("minecubed:tree_bark"));
    }
}
