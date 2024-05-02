package com.minecubedmc.tasks.effects;

import org.bukkit.Particle;
import org.bukkit.entity.Snowball;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class FireSpellEffect extends BukkitRunnable {

    private final Snowball snowball;
    private final Particle particle;
    private final int particleCount;

    public FireSpellEffect(@NotNull Snowball snowball, Particle particle, int particleCount) {
        this.snowball = snowball;
        this.particle = particle;
        this.particleCount = particleCount;
    }

    @Override
    public void run() {
        // Check if the snowball still exists and is still in motion
        if (!snowball.isValid() || snowball.isOnGround() || snowball.getTicksLived() > 200) {
            snowball.remove();
            this.cancel(); // Stop the task if the snowball is no longer valid or has hit the ground
            return;
        }
        if (particle != null && particleCount > 0)
            // Spawn particles at the snowball's location to create the trail
            snowball.getWorld().spawnParticle(particle, snowball.getLocation(), particleCount, 0, 0, 0);

    }

}
