package com.minecubedmc.modules.Disease;

import com.minecubedmc.Tweaks;
import com.minecubedmc.modules.Disease.Diseases;
import com.minecubedmc.util.BasicUtils;
import com.minecubedmc.modules.Disease.PlayerInfectionsData;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Infect extends BukkitRunnable {

    private static final int DISEASE_MAX_TRIES = 5; // For 5 tries:
    private static final float DISEASE_CHANCE = 0.25f; // 76.27% chance to get disease
    private static final float INFECTION_SPREAD_CHANCE = 0.1f; // 40.95% chance to spread disease

    private static final String[] FLAVOR_TEXT_SICK = new String[] {
            "You feel odd.",
            "Fatigue creeps in.",
            "You feel under the weather.",
            "Your body aches.",
            "You feel queasy.",
            "Dizziness clouds your thoughts.",
            "You feel a bit strange.",
            "You're not feeling your best.",
            "A subtle discomfort starts to grow.",
            "You suddenly don't feel so great.",
            "You sense a mild unease.",
            "Your muscles feel weary.",
            "A slight discomfort begins to emerge.",
            "You're not feeling your usual self."
    };

    private static final String[] FLAVOR_TEXT_CURED = new String[] {
            "You feel refreshed for some reason.",
            "You feel better.",
            "Your energy returns unexpectedly.",
            "You feel surprisingly well.",
            "A sudden burst of vitality courses through you.",
            "You feel relieved.",
            "You feel unexpected relief.",
            "You suddenly feel revitalized.",
            "A feeling of relief envelops you.",
            "Your spirits lift unexpectedly.",
            "You find yourself surprisingly rejuvenated."
    };

    Tweaks plugin;

    public Infect(Tweaks plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        Random random = new Random();

        // Get all online players and for each:
        Bukkit.getServer().getOnlinePlayers().parallelStream().forEach(player -> {

            // If player is invincible, skip
            if (player.hasPermission("epidemic.invincible ")) {
                return;
            }

            // Get player infections
            PlayerInfectionsData playerInfectionsData = Diseases.playerDiseaseDataMap.get(player.getUniqueId());

            // For each infection:
            playerInfectionsData.getInfections().forEach((infectionName, infectionData) -> {

                // Check if infection has expired
                if (infectionData.getInfectionTries() >= DISEASE_MAX_TRIES) {
                    // Remove infection
                    playerInfectionsData.removeInfection(infectionName);
                    // Give flavor text
                    player.sendMessage(FLAVOR_TEXT_CURED[random.nextInt(FLAVOR_TEXT_CURED.length)]);
                    return;
                }

                // If it didn't, apply infection disease on random chance
                if (random.nextFloat() < DISEASE_CHANCE) {
                    // Remove infection
                    playerInfectionsData.removeInfection(infectionName);
                    // Add disease
                    Diseases.addDiseaseOrInjury(player, infectionName);
                    // TODO: remove debug message
                    plugin.getLogger().warning("Player " + player.getName() + " got infected with " + infectionName);

                }
                // If it failed to apply disease, increment infection tries and give flavor text
                else {
                    infectionData.incrementInfectionTries();
                    player.sendMessage(FLAVOR_TEXT_SICK[random.nextInt(FLAVOR_TEXT_SICK.length)]);
                }

                // For each nearby player, try to infect them if player is infected
                player.getLocation().getNearbyPlayers(5, 2, 5).parallelStream().forEach(nearbyPlayer -> {
                    // Get nearby player's infection data
                    PlayerInfectionsData nearbyPlayerInfectionsData = Diseases.playerDiseaseDataMap.get(nearbyPlayer.getUniqueId());

                    // Get nearby player's diseases
                    String nearbyPlayerDiseasesList = Diseases.getDiseases(nearbyPlayer);

                    // Chance to spread infection
                    if (random.nextFloat() < INFECTION_SPREAD_CHANCE) {
                        // If nearby player is not does not have the disease
                        if (nearbyPlayerDiseasesList.contains(infectionName)) {
                            // Infect them
                            nearbyPlayerInfectionsData.addInfection(infectionName);
                        }
                    }

                });
            });

            // Secondary diseases
            String diseases = Diseases.getDiseases(player);
            if (diseases.contains("wound") || diseases.contains("cut")) {
                String biomeKey = BasicUtils.getBiomeKey(player.getLocation().getBlock());

                if (biomeKey.equals("minecraft:jungle") ||
                    biomeKey.equals("minecraft:bamboo_jungle") ||
                    biomeKey.equals("minecraft:swamp") ||
                    biomeKey.equals("minecraft:mangrove_swamp") ||
                    biomeKey.equals("minecraft:lush_caves") ||
                    biomeKey.equals("minecubed:dark_jungle")
                ) {
                    if (random.nextFloat() < 0.1f && !diseases.contains("typhus")) {
                        playerInfectionsData.addInfection("typhus");
                    }
                }
            }
        });
    }
}
