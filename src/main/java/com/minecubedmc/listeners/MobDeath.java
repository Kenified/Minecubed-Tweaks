package com.minecubedmc.listeners;

import com.minecubedmc.Tweaks;
import dev.lone.itemsadder.api.CustomStack;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Locale;
import java.util.Random;

public class MobDeath implements Listener {

    private final Tweaks plugin;
    HeadDatabaseAPI headDatabaseAPI;

    public MobDeath(Tweaks plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDatabaseLoad(DatabaseLoadEvent e) {
        headDatabaseAPI = new HeadDatabaseAPI();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e){
        LivingEntity entity = e.getEntity();

        //Only drop head for player kills
        if ( !(entity.getKiller() instanceof Player)){
            return;
        }

        EntityType eType = entity.getType();
        switch (eType){
            case ZOMBIE:
            {
                dropVanillaSkull(e, Material.ZOMBIE_HEAD, 1.5f);
                break;
            }
            case CREEPER:
            {
                dropVanillaSkull(e, Material.CREEPER_HEAD, 2);
                break;
            }
            case SKELETON:
            {
                dropVanillaSkull(e, Material.SKELETON_SKULL, 2);
                break;
            }
            case BLAZE:
            {
                dropCustomSkull(e, "47778",  2);
                break;
            }
            case IRON_GOLEM:
            {
                ItemStack ironScrap = CustomStack.getInstance("minecubed:iron_scrap").getItemStack();
                entity.getLocation().getWorld().dropItem(entity.getLocation(), ironScrap);
                dropCustomSkull(e, "45422",  10);
                break;
            }
            case ZOMBIE_VILLAGER:
            {
                dropCustomSkull(e, "39103",  6);
                break;
            }
            case HUSK:
            {
                dropCustomSkull(e, "38782",  1.5f);
                break;
            }
            case GHAST:
            {
                dropCustomSkull(e, "40638",  10);
                break;
            }
            case ENDERMAN:
            {
                dropCustomSkull(e, "23778",  5);
                break;
            }
            case DROWNED:
            {
                dropCustomSkull(e, "47290",  3);
                break;
            }
            case PHANTOM:
            {
                dropCustomSkull(e, "18091",  2);
                break;
            }
            case VINDICATOR:
            case PILLAGER:
            {
                dropCustomSkull(e, "25149", 3);
                break;
            }
            case SILVERFISH:
            {
                dropCustomSkull(e, "3936",  6);
                break;
            }
            case SLIME:
            {
                dropCustomSkull(e, "22210",  2);
                break;
            }
            case MAGMA_CUBE:
            {
                dropCustomSkull(e, "323",  2);
                break;
            }
            case SNOWMAN:
            {
                dropCustomSkull(e, "24080", 10);
                break;
            }
            case SPIDER:
            {
                dropCustomSkull(e, "32706",  1.5f);
                break;
            }
            case CAVE_SPIDER:
            {
                dropCustomSkull(e, "26009",  1.5f);
                break;
            }
            case STRAY:
            {
                dropCustomSkull(e, "3244",  1.5f);
                break;
            }
            case WITCH:
            {
                dropCustomSkull(e, "3864",  5);
                break;
            }
            case ELDER_GUARDIAN:
            case GUARDIAN:
            {
                dropCustomSkull(e, "3135" , 4);
                break;
            }
            case GLOW_SQUID:
                dropCustomSkull(e, "42563" , 8);
                break;
            case SQUID:
                dropCustomSkull(e, "20119" , 8);
                break;
        }
    }

    private void dropCustomSkull(EntityDeathEvent e, String hdbID, float chance){
        if ( new Random().nextInt(1000) + 1 > chance * 10 ) {
            return;
        }

        Location location = e.getEntity().getLocation();
        World world = location.getWorld();
        ItemStack item = headDatabaseAPI.getItemHead(hdbID);

        world.dropItemNaturally(location, item);
    }

    private void dropVanillaSkull(EntityDeathEvent e, Material material, float chance){
        if ( new Random().nextInt(1000) + 1 > chance * 10 ) {
            return;
        }

        Location location = e.getEntity().getLocation();
        World world = location.getWorld();
        ItemStack item = new ItemStack(material);

        world.dropItemNaturally(location, item);
    }
}
