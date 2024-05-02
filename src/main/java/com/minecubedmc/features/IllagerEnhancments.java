package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import com.willfp.ecoenchants.enchants.EcoEnchants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class IllagerEnhancments implements Listener {

    Tweaks plugin;

    public IllagerEnhancments(Tweaks instance){
        plugin = instance;
    }

    @EventHandler
    public void onVindicatorSpawn(CreatureSpawnEvent event){
        Entity entity = event.getEntity();

        if (entity instanceof Vindicator vindicator){
            ItemStack theVindicator = Cache.getCustomItem("minecubed:the_vindicator");
            // TODO: modify itemsadder nbt data for custom durability
//            theVindicator.setDurability(vindicator.getEquipment().getItemInMainHand().getDurability());
            vindicator.getEquipment().setItemInMainHand(theVindicator);
        }

        if (entity instanceof Vex vex){
            ItemStack ironKnife = Cache.getCustomItem("minecubed:iron_knife");
            // TODO: modify itemsadder nbt data for custom durability
            vex.getEquipment().setItemInMainHand(ironKnife);
        }
    }

    @EventHandler
    public void onPatrolMemberSpawn(CreatureSpawnEvent event){
        if (event.getSpawnReason() != CreatureSpawnEvent.SpawnReason.PATROL){
            return;
        }

        Illager illager = (Illager) event.getEntity();
        // Check if entity is the captain
        if (illager.isPatrolLeader()){

            if (illager instanceof Pillager){
                ItemStack patrolLeaderCrossbow = patrolLeaderCrossbow();
                patrolLeaderCrossbow.setDurability(illager.getEquipment().getItemInMainHand().getDurability());
                illager.getEquipment().setItemInMainHand(patrolLeaderCrossbow);
            } else if (illager instanceof Vindicator vindicator) {
                ItemStack theVindicator = Cache.getCustomItem("minecubed:the_vindicator");
                theVindicator.addEnchantment(Enchantment.DAMAGE_ALL, 5);
                theVindicator.setDurability(vindicator.getEquipment().getItemInMainHand().getDurability());
                vindicator.getEquipment().setItemInMainHand(theVindicator);
            }
            return;
        }

        Random random = new Random();
        if (random.nextFloat() > 0.5f){
            return;
        }

        // Remove the illager
        illager.remove();
        Location location = event.getLocation();

        // 50% chance to spawn a vindicator
        if (random.nextFloat() > 0.5f){
            // Spawn vindicator
            Vindicator vindicator = (Vindicator) location.getWorld().spawnEntity(location, EntityType.VINDICATOR);

            ItemStack theVindicator = Cache.getCustomItem("minecubed:the_vindicator");
            theVindicator.setDurability(vindicator.getEquipment().getItemInMainHand().getDurability());
            vindicator.getEquipment().setItemInMainHand(theVindicator);
        } else {
            // 25% chance to spawn an Illusioner
            if (random.nextFloat() > 0.5f) {
                // Spawn illusioner
                Illusioner illusioner = (Illusioner) location.getWorld().spawnEntity(location, EntityType.ILLUSIONER);
            }
            // 25% chance to spawn an Evoker
            else {
                // Spawn evoker
                Evoker evoker = (Evoker) location.getWorld().spawnEntity(location, EntityType.EVOKER);
            }
        }
    }

    @EventHandler
    public void onCaptainDeath(EntityDeathEvent event){
        Entity entity = event.getEntity();

        if (entity instanceof Illager illager){
            if (illager.isPatrolLeader()){
                event.getDrops().removeIf(itemStack -> itemStack.getType() == Material.CROSSBOW);
            }
        }
    }
    private ItemStack patrolLeaderCrossbow(){
        ItemStack enchantedCrossbow = new ItemStack(Material.CROSSBOW);

        enchantedCrossbow.addEnchantment(Enchantment.QUICK_CHARGE, 3);
        enchantedCrossbow.addEnchantment(Enchantment.MULTISHOT, 1);
        enchantedCrossbow.addEnchantment(Enchantment.PIERCING, 4);
        enchantedCrossbow.addEnchantment(Enchantment.MENDING, 1);

//        if (plugin.getServer().getPluginManager().isPluginEnabled("EcoEnchants")){
//            enchantedCrossbow.addEnchantment(EcoEnchants.getByName("wound"), 7);
//            enchantedCrossbow.addEnchantment(EcoEnchants.getByName("skull_puncture"), 3);
//            enchantedCrossbow.addEnchantment(EcoEnchants.getByName("snipe"), 4);
//        }

        return enchantedCrossbow;
    }
}
