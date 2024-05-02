package com.minecubedmc.modules.Disease;

import com.ibexmc.epidemic.Epidemic;
import com.ibexmc.epidemic.ailments.Afflicted;
import com.ibexmc.epidemic.ailments.Ailment;
import com.ibexmc.epidemic.api.EpidemicAPI;
import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Diseases implements Listener {

    public static ConcurrentHashMap<UUID, PlayerInfectionsData> playerDiseaseDataMap;
    Random random = new Random();

    public Diseases() {
        if (playerDiseaseDataMap == null)
            playerDiseaseDataMap = new ConcurrentHashMap<>();
    }

    @EventHandler
    public void playerLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();

        if (!Diseases.playerDiseaseDataMap.containsKey(player.getUniqueId())){
            Diseases.playerDiseaseDataMap.put(player.getUniqueId(), new PlayerInfectionsData());
        }
    }

    @EventHandler
    public void playerAttacked(EntityDamageByEntityEvent event){
        if (event.isCancelled()){
            return;
        }

        if ( !(event.getEntity() instanceof Player player)){
            return;
        }

        // If player is invincible, skip
        if (player.hasPermission("epidemic.invincible ")) {
            return;
        }

        Entity attacker = event.getDamager();
        EntityDamageEvent.DamageCause cause = event.getCause();
        PlayerInfectionsData playerInfectionsData = Diseases.playerDiseaseDataMap.get(player.getUniqueId());
        String diseases = getDiseases(player);

        switch (cause){
            case ENTITY_ATTACK -> {
                // Add infections based on attacker type
                switch (attacker.getType()){
                    case HUSK, ZOMBIE, ZOMBIE_VILLAGER, DROWNED -> {
                        // Chance to get infected with rabies
                        if (random.nextFloat() < 0.5f){
                            if (!diseases.contains("rabies")){
                                playerInfectionsData.addInfection("rabies");
                            }

                        }
                    }
                    case SPIDER, CAVE_SPIDER -> {
                        // Chance to get infected with bubonic plague
                        if (random.nextFloat() < 0.05f){
                            if (!diseases.contains("bubonic_plague")){
                                playerInfectionsData.addInfection("bubonic_plague");
                            }
                        }
                    }
                }

                // Chance to get injured with broken bone
                if (random.nextFloat() < 0.1f){
                    if (!diseases.contains("broken_leg")){
                        Diseases.addDiseaseOrInjury(player, "broken_leg");
                    }
                }


            }
            case ENTITY_EXPLOSION, BLOCK_EXPLOSION -> {

//                // Chance to get injured with concussion
//                if (random.nextFloat() < 0.2){
//                    if (!diseases.contains("concussion")){
//                        playerInfections.addDisease("concussion");
//                    }
//                }

                if ( random.nextFloat() < 0.3f){
                    if (!diseases.contains("broken_leg")){
                        Diseases.addDiseaseOrInjury(player, "broken_leg");
                    }
                }
            }

            case FIRE, FIRE_TICK, LAVA, HOT_FLOOR -> {
                // Chance to get injured with burn
                if (random.nextFloat() < 0.1f){
                    if (!diseases.contains("burn")){
                        Diseases.addDiseaseOrInjury(player, "burn");
                    }
                }
            }
            case FALL -> {
                // Chance to get injured with broken bone
                if (random.nextFloat() < 0.1f){
                    if (!diseases.contains("broken_leg")){
                        Diseases.addDiseaseOrInjury(player, "broken_leg");
                    }
                }

//                // Chance to get injured with sprained ankle
//                if (random.nextFloat() < 0.1){
//                    if (!diseases.contains("sprained_ankle")){
//                        playerInfections.addDisease("sprained_ankle");
//                    }
//                }
            }
        }
    }

//    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent event) {
        if (event.isCancelled()){
            return;
        }

        Player player = event.getPlayer();

        // If player is invincible, skip
        if (player.hasPermission("epidemic.invincible ")) {
            return;
        }

        ItemStack consumedItem = event.getItem();
        PlayerInfectionsData playerInfectionsData = Diseases.playerDiseaseDataMap.get(player.getUniqueId());
        String diseases = getDiseases(player);

        if (consumedItem.isSimilar(Cache.getCustomItem("minecubed:water_bottle"))) {
            if (random.nextFloat() < 0.25f) {
                if (diseases.contains("dysentery")) {
                    playerInfectionsData.addInfection("dysentery");
                }
            }
        }

        if (CustomStack.byItemStack(consumedItem) == null) {
            return;
        }

        switch (consumedItem.getType()){
            case CHICKEN, BEEF, PORKCHOP, RABBIT, MUTTON -> {
                if (random.nextFloat() < 0.25f) {
                    if (diseases.contains("dysentery")) {
                        playerInfectionsData.addInfection("dysentery");
                    }
                }
            }
            case ROTTEN_FLESH -> {
                if (random.nextFloat() < 0.25f) {
                    if (diseases.contains("dysentery")) {
                        playerInfectionsData.addInfection("dysentery");
                    }
                }

                if (random.nextFloat() < 0.3) {
                    if (diseases.contains("rabies")) {
                        playerInfectionsData.addInfection("rabies");
                    }
                }
            }
        }
    }

    public static String getDiseases(Player player){
        EpidemicAPI eAPI = Epidemic.instance().api();

        // Get the Set of Afflicted objects.
        Set<Afflicted> afflictedSet = eAPI.getAfflictions(player);

        // Initialize an array to store the snake_case display names.
        String[] ailmentDisplayNames = new String[afflictedSet.size()];

        int index = 0;
        for (Afflicted afflicted : afflictedSet) {
            Ailment ailment = afflicted.getAilment();
            String displayName = ailment.getDisplayName().replaceAll("\\s+", "_").toLowerCase();
            ailmentDisplayNames[index++] = displayName;
        }

        return String.join(" ", ailmentDisplayNames);
    }


    public static void addDiseaseOrInjury(Player player, String diseaseName){
        String playerName = player.getName();
        Server server = Bukkit.getServer();
        String command = String.format("infect %s %s", playerName, diseaseName);
        server.dispatchCommand(server.getConsoleSender(), command);
    }

    @SuppressWarnings("unused")
    private static void cureDisease (Player player, String diseaseName){
        String playerName = player.getName();
        Server server = Bukkit.getServer();
        String command = String.format("cure %s %s", playerName, diseaseName);
        server.dispatchCommand(server.getConsoleSender(), command);
    }

    @SuppressWarnings("unchecked")
    public static void loadInfectionData() {
        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        File file = new File(plugin.getDataFolder(), "infections.dat");
        if (file.exists()) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file))) {
                Diseases.playerDiseaseDataMap = (ConcurrentHashMap<UUID, PlayerInfectionsData>) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                plugin.getLogger().severe("Could not load infection data");
                plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
            }
        }
    }

    public static void saveInfectionData() {
        Tweaks plugin = Tweaks.getPlugin(Tweaks.class);
        try {
            File file = new File(plugin.getDataFolder(), "infections.dat");
            if (!file.exists()) {
                file.createNewFile();
            }

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(Diseases.playerDiseaseDataMap);
            objectOutputStream.close();
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save infection data");
            plugin.getLogger().severe(Arrays.toString(e.getStackTrace()));
        }
    }
}
