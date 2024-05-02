package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Warden;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class DangerousDark implements Listener {

    Tweaks plugin;

    public DangerousDark(Tweaks plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBlockBreak(BlockBreakEvent event){
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        Material blockType = block.getType();

        if (!blockType.equals(Material.SCULK_SHRIEKER) && !blockType.equals(Material.SCULK_SENSOR) ) {
            return;
        }

        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;

        Biome biome = block.getBiome();
        if (!biome.equals(Biome.DEEP_DARK)){
            return;
        }

        Location blockLocation = block.getLocation();
        World world = blockLocation.getWorld();
        world.playSound(blockLocation, "block.sculk_shrieker.shriek", SoundCategory.BLOCKS, 1, 1);

        player.setWardenWarningLevel(player.getWardenWarningLevel() + 1);

        Random random = new Random();
        // 70% chance to not spawn
        if (random.nextFloat() < 0.70) return;
        world.playSound(blockLocation, "entity.warden.nearby_closest", SoundCategory.BLOCKS, 1, 1);

        Warden entity = world.spawn(blockLocation, Warden.class, warden ->{
            NBTEntity nbtEntity = new NBTEntity(warden);
            NBTContainer nbtContainer = new NBTContainer("{Brain:{memories:{\"minecraft:dig_cooldown\":{ttl:1200L,value:{}}, \"minecraft:is_emerging\":{ttl:134L,value:{}}}}}");
            nbtEntity.mergeCompound(nbtContainer);
            warden.setInvisible(true);

            blockLocation.getNearbyPlayers(32).forEach(nearbyPlayer -> {
                player.addPotionEffect(PotionEffectType.DARKNESS.createEffect(260, 0));

                if (nearbyPlayer.equals(player)) warden.setAnger(nearbyPlayer, 150);
                else warden.setAnger(nearbyPlayer, 50);
            });
        });

        Bukkit.getScheduler().runTaskLater(plugin, () -> entity.setInvisible(false), 4);

    }
}
