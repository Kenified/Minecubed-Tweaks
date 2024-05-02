package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.tasks.effects.FireSpellEffect;
import com.minecubedmc.util.Cache;
import io.papermc.paper.event.entity.EntityLoadCrossbowEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;



public class StaffsAndGuns implements Listener {

    class CastingItem<T> {
        public final String itemID;
        public final String soundEffectID;
        public final ItemStack ammoItem;
        public final T projectileDisplayItem;
        public final Particle projectileParticle;
        public final int particleCount;
        public final float projectileVelocityMul;

        public CastingItem(
                @NotNull String itemID,
                String soundEffectID,
                @NotNull ItemStack ammoItem,
                T projectileDisplayItem,
                Particle projectileParticle,
                int particleCount,
                @NotNull Float projectileVelocityMul
        ) {
            this.itemID = itemID;
            this.soundEffectID = soundEffectID;
            this.ammoItem = ammoItem;
            this.projectileDisplayItem = projectileDisplayItem;
            this.projectileParticle = projectileParticle;
            this.particleCount = particleCount;
            this.projectileVelocityMul = projectileVelocityMul;
        }
    }

    Tweaks plugin;

    NamespacedKey minecubedKey;

    List<CastingItem> staffList = new ArrayList<>();

    public StaffsAndGuns(Tweaks plugin) {
        this.plugin = plugin;

        minecubedKey = new NamespacedKey(plugin, "weapon_id");

        staffList.add(new CastingItem<>(
                "3dfantasyweaponscit:black_snake_staff",
                "entity.warden.sonic_boom",
                new ItemStack(Material.FIRE_CHARGE),
                "hmccosmetics:blank_icon",
                Particle.SONIC_BOOM,
                1,
                1f
        ));
    }

    @EventHandler
    public void crossbowChargeEvent(final EntityLoadCrossbowEvent event){
        if (event.isCancelled()) return;

        final Entity entity = event.getEntity();
        if (!(entity instanceof Player)) return;

        staffList.forEach(castingItem -> {
            handleCharging(castingItem.itemID, castingItem.ammoItem, event);
        });

    }

    @EventHandler
    public void onPlayerCrossbowShot(final EntityShootBowEvent event) {
        if (event.isCancelled()) return;

        if (!(event.getEntity() instanceof Player)) return;

        final ItemStack shootingItem = event.getBow();
        if (!(shootingItem.getType().equals(Material.CROSSBOW))) return;

        staffList.forEach(castingItem -> {
            handleShooting(
                    castingItem.itemID,
                    castingItem.soundEffectID,
                    castingItem.projectileDisplayItem,
                    castingItem.projectileParticle,
                    castingItem.particleCount,
                    castingItem.projectileVelocityMul,
                    event);
        });
        
    }

    public void handleCharging(
            final @NotNull String itemID,
            final @NotNull ItemStack ammoItem,
            final @NotNull EntityLoadCrossbowEvent event
    ){
        final ItemStack shootingItem = event.getCrossbow();
        if (!shootingItem.getItemMeta().hasCustomModelData() ) return;
        if (!(shootingItem.getItemMeta().getCustomModelData() == Cache.getCustomItem(itemID).getItemMeta().getCustomModelData())) return;

        final Player player = (Player) event.getEntity();
        final Inventory inventory = player.getInventory();

        if (!inventory.containsAtLeast(ammoItem, 1)){
            event.setCancelled(true);
            return;
        }

        inventory.forEach(itemStack -> {
            if (itemStack == null) return;
            if(itemStack.isSimilar(ammoItem)){
                event.setConsumeItem(false);
                itemStack.subtract();
            }
        });
    }

    private <T> void handleShooting(
            final @NotNull String itemID,
            final String soundEffectID,
            final T projectileDisplayItem,
            final Particle particle,
            final int particleCount,
            final @NotNull Float projectileVelocityMul,
            final @NotNull EntityShootBowEvent event
    ){
        ItemStack shootingItem = event.getBow();
        if (!shootingItem.getItemMeta().hasCustomModelData() ) return;
        if (!(shootingItem.getItemMeta().getCustomModelData() == Cache.getCustomItem(itemID).getItemMeta().getCustomModelData())) return;

        final Player player = (Player) event.getEntity();
        final Entity originalProjectile = event.getProjectile();

        Snowball customProjectile = player.launchProjectile(Snowball.class);
        customProjectile.setVelocity(originalProjectile.getVelocity().multiply(projectileVelocityMul));
        customProjectile.setGravity(false);
        customProjectile.setShooter(player);

        if (projectileDisplayItem instanceof String){
            customProjectile.setItem(Cache.getCustomItem((String) projectileDisplayItem));
        }
        else if (projectileDisplayItem != null){
            customProjectile.setItem((ItemStack) projectileDisplayItem);
        }

        originalProjectile.remove();

        new FireSpellEffect(customProjectile, particle, particleCount).runTaskTimer(plugin, 0, 1);
        // Custom data for identifying the projectile
        customProjectile.getPersistentDataContainer().set(minecubedKey, PersistentDataType.STRING, itemID );

        if (soundEffectID != null){
            Location location = player.getLocation();
            World world = player.getWorld();
            world.playSound(location, soundEffectID, SoundCategory.PLAYERS, 1, 1);
        }

    }
}
