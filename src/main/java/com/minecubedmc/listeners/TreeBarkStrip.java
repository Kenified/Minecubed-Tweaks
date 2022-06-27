package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TreeBarkStrip implements Listener {

    Tweaks plugin;

    public TreeBarkStrip(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        //Check if it is not in offhand
        if (e.getHand() != EquipmentSlot.HAND) {
            return;
        }

        //Check if it is right click action
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        //Check if item isn't null and is axe
        ItemStack item = e.getItem();
        if (item == null ) {
            return;
        }

        String itemName = item.getType().toString();
        if (!itemName.endsWith("_AXE")){
            return;
        }

        //Check if its non stripped wood block
        String blockName = block.getType().toString();
        if (!blockName.contains("_LOG") && !blockName.contains("_WOOD")) {
            return;
        }

        if (blockName.contains("STRIPPED")) {
            return;
        }

        Player player = e.getPlayer();
        ItemStack customItem = CustomStack.getInstance("minecubed:tree_bark").getItemStack();
        player.getWorld().dropItem(e.getInteractionPoint(), customItem);
    }
}
