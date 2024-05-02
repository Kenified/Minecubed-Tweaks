package com.minecubedmc.features;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class Pokeballs implements Listener {

    Tweaks plugin;

    NamespacedKey pokeballTag;
    NamespacedKey entityKey;

    public Pokeballs(Tweaks plugin) {
        this.plugin = plugin;
        // Create namespaced key for entity data
        entityKey = new NamespacedKey(plugin, "entity");
        pokeballTag = new NamespacedKey(plugin, "pokeballTag");
    }

    @EventHandler
    public void pokeballThrow(PlayerInteractEvent event){
        // Ignore offhand interactions
        if (event.getHand().equals(EquipmentSlot.OFF_HAND)) return;

        // If player didn't right-click, ignore
        Action action = event.getAction();
        if (!action.equals(Action.RIGHT_CLICK_BLOCK) && !action.equals(Action.RIGHT_CLICK_AIR)) return;

        // If held item is not a pokeball, skip
        ItemStack pokeballItem = event.getItem();

        if (pokeballItem == null) return;

        if (!pokeballItem.isSimilar(Cache.getCustomItem("minecubed:passive_pokeball")) &&
            !pokeballItem.isSimilar(Cache.getCustomItem("minecubed:water_pokeball")) &&
            !pokeballItem.isSimilar(Cache.getCustomItem("minecubed:hostile_pokeball"))
        ) {
            return;
        }

        // If player has cool down for pokeball item don't allow throwing another pokeball
        Player player = event.getPlayer();
        if (player.hasCooldown(pokeballItem.getType())) return;

        // Create snowball projectile for pokeball
        Snowball thrownPokeball = player.launchProjectile(Snowball.class);

        // Set snowball display item to pokeball item
        thrownPokeball.setItem(pokeballItem);

        // If pokeball item has stored entity data, set it to the thrown pokeball; otherwise throw empty pokeball
        PersistentDataContainer container = pokeballItem.getItemMeta().getPersistentDataContainer();
        if (container.has(entityKey)) {
            // Get serialized entity from pokeball item
            byte[] serializedEntity = container.get(entityKey, PersistentDataType.BYTE_ARRAY);

            // Set entity data to thrown pokeball projectile
            thrownPokeball.getPersistentDataContainer().set(entityKey, PersistentDataType.BYTE_ARRAY, serializedEntity);
        }

        // Set pokeball tag to thrown pokeball projectile
        thrownPokeball.getPersistentDataContainer().set(pokeballTag, PersistentDataType.BOOLEAN, true);

        // Remove pokeball from player inventory
        pokeballItem.subtract();

        // Set cooldown for pokeball item for 10 seconds
        player.setCooldown(pokeballItem.getType(), 200);
    }

    @EventHandler
    public void pokeballRelease(ProjectileHitEvent event) {
        // If projectile is not a snowball, skip
        Projectile projectile = event.getEntity();
        if( !(projectile instanceof Snowball snowball)){
            return;
        }

        // Check if projectile has pokeball tag
        if (!projectile.getPersistentDataContainer().has(pokeballTag)){
            return;
        }

        // If there is no entity data in the pokeball, skip
        if (!projectile.getPersistentDataContainer().has(entityKey)){
            return;
        }

        Entity hitEntity = event.getHitEntity();
        Block hitBlock = event.getHitBlock();
        Location location;

        // Get location of hit entity or block
        if (hitEntity != null){
            location = hitEntity.getLocation();
        } else if (hitBlock != null){
            location = hitBlock.getLocation();
        } else {
            return;
        }

        // Get persistent data stored in pokeball projectile
        byte[] serializedEntity = projectile.getPersistentDataContainer().get(entityKey, PersistentDataType.BYTE_ARRAY);
        // Deserialize the data into an entity
        Entity deserializedEntity = Bukkit.getUnsafe().deserializeEntity(serializedEntity, location.getWorld());


        // Spawn entity
        deserializedEntity.spawnAt(location);

        // Get which pokeball item was used to throw the pokeball
        ItemStack pokeballItem = getEmptyPokeball(snowball.getItem());

        // Drop empty pokeball
        location.getWorld().dropItemNaturally(location, pokeballItem);
    }

    @EventHandler
    public void pokeballCapture(ProjectileHitEvent event) {
        // If projectile is not a snowball, skip
        Projectile projectile = event.getEntity();
        if( !(projectile instanceof Snowball snowball)){
            return;
        }

        // Check if projectile has pokeball tag
        if (!projectile.getPersistentDataContainer().has(pokeballTag)){
            return;
        }

        // Don't allow filled pokeballs to capture entities
        if (projectile.getPersistentDataContainer().has(entityKey)){
            return;
        }


        // If projectile did not hit an entity, skip
        Entity hitEntity = event.getHitEntity();
        if(hitEntity == null) {
            // If projectile hit a block, drop empty pokeball
            event.getHitBlock().getLocation().getWorld().dropItemNaturally(event.getHitBlock().getLocation(), getEmptyPokeball(snowball.getItem()));
            return;
        }

        // If entity is a player, skip
        if (hitEntity instanceof Player player) {
            // If projectile hit a player, drop empty pokeball
            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), getEmptyPokeball(snowball.getItem()));
            return;
        }

        // If entity is a MythicMob, skip
        if ( Tweaks.getPluginManager().isPluginEnabled("MythicMobs") ) {
            if (MythicBukkit.inst().getMobManager().isMythicMob(hitEntity)){
                // If projectile hit a MythicMob, drop empty pokeball
                hitEntity.getLocation().getWorld().dropItemNaturally(hitEntity.getLocation(), getEmptyPokeball(snowball.getItem()));
                return;
            }
        }

        Location location = hitEntity.getLocation();
        ItemStack passivePokeball = Cache.getCustomItem("minecubed:passive_pokeball");
        ItemStack waterPokeball = Cache.getCustomItem("minecubed:water_pokeball");
        ItemStack hostilePokeball = Cache.getCustomItem("minecubed:hostile_pokeball");

        if (isPassive(hitEntity)) {
            storeEntityInPokeball(passivePokeball, hitEntity);
        }
        else if (isWater(hitEntity)) {
            storeEntityInPokeball(waterPokeball, hitEntity);
        }
        else if (isHostile(hitEntity)) {
            storeEntityInPokeball(hostilePokeball, hitEntity);
        } else {
            // If entity does not belong to any category, drop empty pokeball based on pokeball item
            location.getWorld().dropItemNaturally(location, getEmptyPokeball(snowball.getItem()));
        }
    }

    private boolean isPassive(Entity entity){
        SpawnCategory spawnCategory = entity.getSpawnCategory();
        return  spawnCategory.equals(SpawnCategory.ANIMAL) ||
                spawnCategory.equals(SpawnCategory.AMBIENT);
    }

    private boolean isWater(Entity entity){
        SpawnCategory spawnCategory = entity.getSpawnCategory();
        return  spawnCategory.equals(SpawnCategory.WATER_ANIMAL) ||
                spawnCategory.equals(SpawnCategory.WATER_AMBIENT) ||
                spawnCategory.equals(SpawnCategory.WATER_UNDERGROUND_CREATURE) ||
                spawnCategory.equals(SpawnCategory.AXOLOTL);
    }

    private boolean isHostile(Entity entity){
        SpawnCategory spawnCategory = entity.getSpawnCategory();

        // Don't allow boss mobs to be caught
        if (entity instanceof Boss) return false;

        return spawnCategory.equals(SpawnCategory.MONSTER);
    }

    private void storeEntityInPokeball(ItemStack itemStack, Entity entity){
        // Get entity location
        Location location = entity.getLocation();

        // Serialize entity
        byte[] serializedEntity = Bukkit.getUnsafe().serializeEntity(entity);

        // Curious about the size of serialized entity
        plugin.getLogger().info("Entity serialized size= " + serializedEntity.length + "B");

        // Remove entity once serialized
        entity.remove();

        // Get item meta
        ItemMeta itemMeta = itemStack.getItemMeta();
        // Get persistent data container
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        // Set serialized entity into persistent data container
        container.set(entityKey, PersistentDataType.BYTE_ARRAY, serializedEntity);
        // Set item meta with persistent data container
        itemStack.setItemMeta(itemMeta);

        // Drop pokeball item containing entity data
        location.getWorld().dropItemNaturally(location, itemStack);

    }

    private ItemStack getEmptyPokeball(ItemStack pokeball){
        ItemStack passivePokeball = Cache.getCustomItem("minecubed:passive_pokeball");
        ItemStack waterPokeball = Cache.getCustomItem("minecubed:water_pokeball");
        ItemStack hostilePokeball = Cache.getCustomItem("minecubed:hostile_pokeball");

        if (pokeball.isSimilar(passivePokeball)) {
            return passivePokeball;
        }
        else if (pokeball.isSimilar(waterPokeball)) {
            return waterPokeball;
        }
        else if (pokeball.isSimilar(hostilePokeball)) {
            return hostilePokeball;
        }
        else {
            return new ItemStack(Material.AIR);
        }
    }
}
