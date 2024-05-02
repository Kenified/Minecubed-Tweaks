package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Comparator;

public class SortingTool implements Listener {

    Tweaks plugin;

    public SortingTool(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event) {
//        plugin.getLogger().info("Player interacted with " + event.getClickedBlock());
//        if (event.isCancelled()) {
//            return;
//        }

//        final Action action = event.getAction();
//        if (action != Action.RIGHT_CLICK_BLOCK) {
//            return;
//        }

        final ItemStack eventItem = event.getItem();
        if (eventItem == null) {
            return;
        }

        if (!eventItem.isSimilar(Cache.getCustomItem("minecubed:wither_bone") )) {
            return;
        }

        final Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        final BlockState state = block.getState();
        if (!(state instanceof Container)) {
            return;
        }

        // Check cooldown
        final Player player = event.getPlayer();
        if (player.getCooldown(eventItem.getType()) > 0) {
            event.setCancelled(true);
            return;
        }

        if (state instanceof Chest) {
            // Sort chest
            sort_chest((Chest) block.getState());
            // Set cool down
            player.setCooldown(eventItem.getType(), 100);

            plugin.getLogger().info("Sorted chest");

            // Cancel the interaction
            event.setCancelled(true);

        }
        else if (state instanceof Barrel) {
            sort_container((Container) block.getState());
            player.setCooldown(eventItem.getType(), 20);
            plugin.getLogger().info("Sorted Barrel");
            event.setCancelled(true);
        }
    }

    private void sort_inventory(final Inventory inventory) {
        // Find amount of non-null item stacks
        final var saved_contents = inventory.getStorageContents();
        int non_null = 0;
        for (final var i : saved_contents) {
            if (i != null) {
                ++non_null;
            }
        }

        // Make new array without null items
        final var saved_contents_condensed = new ItemStack[non_null];
        int cur = 0;
        for (final var i : saved_contents) {
            if (i != null) {
                saved_contents_condensed[cur++] = i.clone();
            }
        }

        // Clear and add all items again to stack them. Restore saved contents on failure.
        try {
            inventory.clear();
            final var leftovers = inventory.addItem(saved_contents_condensed);
            if (leftovers.size() != 0) {
                // Abort! Something went totally wrong!
                inventory.setStorageContents(saved_contents_condensed);
                plugin.getLogger().warning("Sorting inventory " + inventory + " produced leftovers!");
            }
        } catch (Exception e) {
            inventory.setStorageContents(saved_contents_condensed);
            throw e;
        }

        // Sort
        final var contents = inventory.getStorageContents();
        // TODO: figure out if this works
        // Sort items stacks by display name and skipping null items into last place
        Arrays.sort(contents, Comparator.nullsLast(
                Comparator.comparing(item ->
                        item == null
                                ? ""
                                : item.getItemMeta().displayName() == null
                                        ? item.getType().name()
                                        : PlainTextComponentSerializer.plainText().serialize(item.getItemMeta().displayName())
                        )
                )
        );


        inventory.setStorageContents(contents);
    }

    private void sort_container(final Container container) {
//        // Check cooldown
//        if (!cooldown_data.check_or_update_cooldown(container)) {
//            return;
//        }

        sort_inventory(container.getInventory());
    }

    private void sort_chest(final Chest chest) {
        final var inventory = chest.getInventory();

        // Get persistent data
        final Chest persistent_chest;
        if (inventory instanceof DoubleChestInventory) {
            final var left_side = (((DoubleChestInventory) inventory).getLeftSide()).getHolder();
            if (!(left_side instanceof Chest)) {
                return;
            }
            persistent_chest = (Chest) left_side;
        } else {
            persistent_chest = chest;
        }

//        // Check cooldown
//        if (!cooldown_data.check_or_update_cooldown(persistent_chest)) {
//            return;
//        }

        if (persistent_chest != chest) {
            // Save left side block state if we are the right side
            persistent_chest.update(true, false);
        }

        sort_inventory(inventory);
    }
}
