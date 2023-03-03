//package com.minecubedmc.listeners.disabled;
//
//import com.minecubedmc.Tweaks;
//import org.bukkit.entity.Axolotl;
//import org.bukkit.entity.LivingEntity;
//import org.bukkit.event.EventHandler;
//import org.bukkit.event.EventPriority;
//import org.bukkit.event.Listener;
//import org.bukkit.event.entity.CreatureSpawnEvent;
//
//import java.util.Random;
//
//public class MobSpawn implements Listener {
//
//    Tweaks plugin;
//
//    public MobSpawn(Tweaks plugin){
//        this.plugin = plugin;
//    }
//
//    @EventHandler(priority = EventPriority.HIGH)
//    public void entitySpawnEvent(CreatureSpawnEvent e){
//
//        LivingEntity entity = e.getEntity();
//        if (entity instanceof Axolotl axolotl) {
//
//            if (!axolotl.getEntitySpawnReason().equals(CreatureSpawnEvent.SpawnReason.BREEDING)) {
//                return;
//            }
//
//            Axolotl.Variant variant = axolotl.getVariant();
//            if (variant.equals(Axolotl.Variant.BLUE)){
//                return;
//            }
//
//            int chance = 2;
//            if ( new Random().nextInt(100) + 1 > chance ) {
//                return;
//            }
//
//            axolotl.setVariant(Axolotl.Variant.BLUE);
//
//        }
//
////        Location loc = e.getLocation();
////        ServerLevel worldServer = ((CraftWorld) loc.getWorld()).getHandle();
////        Holder<Biome> biomeHolder = worldServer.getBiome(new BlockPos(loc.getX(),loc.getY(),loc.getZ()));
////        Biome biome = biomeHolder.value();
////        plugin.getLogger().warning(biome.toString());
//
//        //        if (e.getEntity() instanceof Zombie && biome.equals("minecubed:sandy_catacombs") && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
////            e.setCancelled(true);
////            world.spawnEntity(loc, EntityType.HUSK, CreatureSpawnEvent.SpawnReason.NATURAL);
////        }
////        else if (e.getEntity() instanceof Skeleton && biome.equals("minecubed:frozen_depths") && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
////            e.setCancelled(true);
////            world.spawnEntity(loc, EntityType.STRAY, CreatureSpawnEvent.SpawnReason.NATURAL);
////        }
//
////        //Limit Frog Amount
////        if (entity instanceof Frog && e.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)){
////            if ( new Random().nextInt(4) + 1 > 1 ) {
////                e.setCancelled(true);
////            }
////        }
//    }
//}
