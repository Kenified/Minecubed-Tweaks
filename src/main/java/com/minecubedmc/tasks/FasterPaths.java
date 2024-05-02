package com.minecubedmc.tasks;

import com.minecubedmc.Tweaks;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class FasterPaths extends BukkitRunnable {

    Tweaks plugin;

    public FasterPaths(Tweaks plugin){
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Block block = player.getLocation().getBlock();
            Block[] blocks = new Block[]{
                    block.getRelative(BlockFace.UP),
                    block,
                    block.getRelative(BlockFace.DOWN)
            };

            if (Arrays.stream(blocks).anyMatch(b -> b.getType().toString().toLowerCase().contains("path") || b.getType().toString().toLowerCase().contains("petrified"))) {

                // Use Bukkit API synchronously
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 1, 1, false, false));
                    }
                }.runTaskLater(plugin, 0);
            }
        }
    }
}
