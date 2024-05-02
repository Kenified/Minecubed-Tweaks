package com.minecubedmc.modules.Dungeons;

import com.minecubedmc.modules.Dungeons.DungeonManager;
import org.bukkit.scheduler.BukkitRunnable;

public class DungeonRefresh extends BukkitRunnable {

        @Override
        public void run() {
            DungeonManager.getDungeons().forEach((name, dungeon) -> {

                if (dungeon.getCooldown() >= System.currentTimeMillis() ) {
                    dungeon.refresh();
                }
            });
        }
}
