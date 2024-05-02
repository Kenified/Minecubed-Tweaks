package com.minecubedmc.tasks;

import com.minecubedmc.features.CustomLeavesPersistent;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveData extends BukkitRunnable {
    @Override
    public void run() {
        CustomLeavesPersistent.savePersistentLeavesData();
//        Diseases.saveInfectionData();
    }
}
