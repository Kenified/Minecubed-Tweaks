package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.items.CustomItems;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Random;

public class MobDrops implements Listener {

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event){
        final LivingEntity entity = event.getEntity();
        final String worldName = entity.getWorld().getName();

        //Prevent mob drops in dungeon worlds
        if (worldName.equals("dungeon_vexkin")){
            event.getDrops().clear();
            return;
        }

        //AFK check
        Collection<Player> players = event.getEntity().getLocation().getNearbyPlayers(( entity.getWorld().getSimulationDistance() * 16) + 8 );
        if (players.stream().allMatch(player -> Tweaks.getEssentials().getUser(player).isAfk())) {
            event.getDrops().clear();
            return;
        }


        //Ignore custom mobs unless its custom ID matches a creeper variant
        if (MythicBukkit.inst().getMobManager().isMythicMob(entity)){
            ActiveMob mythicMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(entity);
            String id = mythicMob.getType().getInternalName().toUpperCase();
            if (!id.contains("CREEPER")){
                return;
            }
        }

        //Iron golem
        if (entity instanceof IronGolem){
            entity.getLocation().getWorld().dropItem(entity.getLocation(), CustomItems.getCustomItem("minecubed:iron_scrap"));
        }

        //Only drop head for player kills and get Looting enchant level
        float lootingBonus = 0;
        if (event.getEntity().getKiller() != null){
            lootingBonus = event.getEntity().getKiller().getInventory().getItemInMainHand().getEnchantments().getOrDefault(Enchantment.LOOT_BONUS_MOBS, 0);
        }
        else return;

        //Ignore spawner mobs and split slimes
        if (entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER) ||
            entity.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.SLIME_SPLIT)
        ){
            return;
        }
    
        final Location location = entity.getLocation();
        final EntityType eType = entity.getType();
        switch (eType) {
            case ZOMBIE -> {
                dropSkull(location, Material.ZOMBIE_HEAD, 2.5f + lootingBonus);
            }
            case CREEPER -> {
                dropSkull(location, Material.CREEPER_HEAD, 2.5f + lootingBonus);
            }
            case SKELETON -> {
                dropSkull(location, Material.SKELETON_SKULL, 2.5f + lootingBonus);
            }
            case BLAZE -> {
                dropSkull(location, 47778, 2.5f + lootingBonus);
            }
            case IRON_GOLEM -> {
                dropSkull(location, 45422, 10 * (lootingBonus + 1));
            }
            case ZOMBIE_VILLAGER -> {
                dropSkull(location, 39103, 2.5f + lootingBonus);
            }
            case HUSK -> {
                dropSkull(location, 38782, 2.5f + lootingBonus);
            }
            case GHAST -> {
                dropSkull(location, 40638, 6 * (lootingBonus + 1));
            }
            case ENDERMAN -> {
                dropSkull(location, 23778, 2.5f + lootingBonus);
            }
            case DROWNED -> {
                dropSkull(location, 47290, 2.5f + lootingBonus);
            }
            case PHANTOM -> {
                dropSkull(location, 18091, 2.5f + lootingBonus);
            }
            case VINDICATOR, PILLAGER -> {
                dropSkull(location, 25149, 1.0f + lootingBonus);
            }
            case SILVERFISH -> {
                dropSkull(location, 3936, 10f + lootingBonus);
            }
            case SLIME -> {
                dropSkull(location, 22210, 1.25f + lootingBonus);
            }
            case MAGMA_CUBE -> {
                dropSkull(location, 323, 1.25f + lootingBonus);
            }
            case SNOWMAN -> {
                dropSkull(location, 24080, 8 * (lootingBonus + 1));
            }
            case SPIDER -> {
                dropSkull(location, 32706, 2.5f + lootingBonus);
            }
            case CAVE_SPIDER -> {
                dropSkull(location, 26009, 2.5f + lootingBonus);
            }
            case STRAY -> {
                dropSkull(location, 3244, 2.5f + lootingBonus);
            }
            case WITCH -> {
                dropSkull(location, 3864, 5f + lootingBonus);
            }
            case ELDER_GUARDIAN, GUARDIAN -> {
                dropSkull(location, 3135, 2.5f + lootingBonus);
            }
            case GLOW_SQUID -> {
                dropSkull(location, 42563, 5f + lootingBonus);
            }
            case SQUID -> {
                dropSkull(location, 20119, 5f + lootingBonus);
            }
        }
    }
    
    private void dropSkull(Location location, Material material, float chance){
        ItemStack item = new ItemStack(material);
        dropItem(location, item, chance);
    }

    private void dropSkull(Location location, int hdbID, float chance){
        ItemStack item = CustomItems.getHeadItem(hdbID);
        dropItem(location, item, chance);
        
    }
    
    private void dropItem(Location location, ItemStack item, float chance) {
        if ( new Random().nextInt(1000) + 1 > chance * 10 ) {
            return;
        }
        
        World world = location.getWorld();
        world.dropItemNaturally(location, item);
    }
}
