package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ChiseledBookshelf;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;

public class NerfChiseledBookshelfLoot implements Listener {

    Tweaks plugin;

    public NerfChiseledBookshelfLoot(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBookshelfInteract(PlayerInteractEvent event) {
        PlayerInventory playerInventory = event.getPlayer().getInventory();
        Block block = event.getClickedBlock();

        if (playerInventory.getItemInMainHand().getType().equals(Material.BOOK) ||
                playerInventory.getItemInMainHand().getType().equals(Material.ENCHANTED_BOOK))
        {
            event.setCancelled(true);
            return;
        };

        if (block != null && block.getType().equals(Material.CHISELED_BOOKSHELF)) {
            Vector interactionPosition = event.getClickedPosition();
            if (interactionPosition == null) return;

            ChiseledBookshelf bookshelf = (ChiseledBookshelf) block.getState();
            ChiseledBookshelfInventory bookshelfInventory = bookshelf.getInventory();

            for (int i = 0; i < bookshelfInventory.getSize(); i++) {
                if (bookshelfInventory.isEmpty()) break;

                ItemStack inventoryItem = bookshelfInventory.getItem(i);

                if (inventoryItem == null) continue;

                if (inventoryItem.getType().equals(Material.ENCHANTED_BOOK) || inventoryItem.getType().equals(Material.BOOK)){
                    bookshelfInventory.removeItem(inventoryItem);
                    ItemStack writableBook = new ItemStack(Material.WRITABLE_BOOK);
                    bookshelfInventory.setItem(i, writableBook);
                }
            }

            ItemStack bookShelfItem = bookshelfInventory.getItem(bookshelf.getSlot(interactionPosition));
            if (bookShelfItem == null) return;

            if (bookShelfItem.getType().equals(Material.BOOK)){
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (playerInventory.contains(Material.BOOK)) {
                        playerInventory.removeItem(new ItemStack(Material.BOOK, 1));

                    }

                    event.getPlayer().getNearbyEntities(0.7, 0.7, 0.7).stream()
                            .filter(entity -> entity instanceof Item)
                            .forEach(itemEntity -> {
                                if (((Item) itemEntity).getItemStack().getType().equals(Material.BOOK)) {
                                    itemEntity.remove();
                                }
                            });
                });

                ItemStack emptyBook = Cache.getCustomItem("minecubed:empty_book");
                if (playerInventory.addItem(emptyBook).size() > 0) {
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), emptyBook);
                }
            }

        }
    }

    @EventHandler
    public void onBookshelfBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();

        if (block.getType().equals(Material.CHISELED_BOOKSHELF)) {
            event.setCancelled(true);
            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onDisenchant(PrepareGrindstoneEvent event) {
        GrindstoneInventory inventory = event.getInventory();

        if (inventory.getResult() == null) return;

        ItemStack emptyBook = Cache.getCustomItem("minecubed:empty_book");

        if (inventory.getResult().getType().equals(Material.BOOK)){
            emptyBook.setAmount(inventory.getResult().getAmount());
            event.setResult(emptyBook);
        }
    }
}
