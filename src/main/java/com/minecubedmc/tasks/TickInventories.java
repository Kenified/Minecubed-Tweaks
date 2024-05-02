package com.minecubedmc.tasks;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.BasicUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class TickInventories extends BukkitRunnable {
    
    private final Tweaks plugin;

    public TickInventories(Tweaks plugin){
        this.plugin = plugin;
        BasicUtils.TickInventories.TOTAL_AGE_KEY = new NamespacedKey(plugin, "total_age");
        BasicUtils.TickInventories.TEMP_AGE_KEY = new NamespacedKey(plugin, "temp_age");
        BasicUtils.TickInventories.START_TIME_KEY = new NamespacedKey(plugin, "start_time");
    }

    @Override
    public void run() {
        BasicUtils.TickInventories.getTickingInventories().forEach((uuid, inventory) -> {
            inventory.forEach(itemStack -> {
                if (itemStack == null) {
                    return;
                }
                
                if (!BasicUtils.TickInventories.isAgingItem(itemStack)){
                    return;
                }
    
                List<Component> lore = itemStack.lore();
                ItemMeta itemMeta = itemStack.getItemMeta();
                PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
                final long currentWorldTime = plugin.getServer().getWorld("world").getFullTime();
                
                if (lore == null){
                    lore = new ArrayList<>();
                }
                
                if (!dataContainer.has(BasicUtils.TickInventories.TOTAL_AGE_KEY)) {
                    dataContainer.set(BasicUtils.TickInventories.TOTAL_AGE_KEY, PersistentDataType.LONG, 0L);
                    dataContainer.set(BasicUtils.TickInventories.TEMP_AGE_KEY, PersistentDataType.LONG, 0L);
                    dataContainer.set(BasicUtils.TickInventories.START_TIME_KEY, PersistentDataType.LONG, 0L);
                    plugin.getLogger().warning("Initialized data");
                    
                }
                
                long tempAge = dataContainer.get(BasicUtils.TickInventories.TEMP_AGE_KEY, PersistentDataType.LONG);
                long startTime = dataContainer.get(BasicUtils.TickInventories.START_TIME_KEY, PersistentDataType.LONG);

                if (startTime < 1 ) {
                    startTime = currentWorldTime;
                    dataContainer.set(BasicUtils.TickInventories.START_TIME_KEY, PersistentDataType.LONG, startTime);
                }
                
                // Scenario added item u barrel, start_time = 100 -> age = 0
                // removed at world_time = 200, age = 100 -> start_time = 0
                // added again at world_time = 300, age = 100 -> start time = 300
                // world_time = 400, start_time = 300 -> age = 200
                tempAge = currentWorldTime - startTime;
                plugin.getLogger().info("Start time: " + startTime);
                plugin.getLogger().info("Current time: " + currentWorldTime);
                plugin.getLogger().info("Age: " + tempAge);
                
                int days = (int) (tempAge/24000);
                int months = days/30;
                int years = months/12;

                Component component = Component.text(String.format("Age: %s ", tempAge), NamedTextColor.GRAY);
                if (lore.isEmpty()) {
                    lore.add(component);
                } else {
                    lore.set(0, component);
                }
//                    if (days <= 30 ){
//                        Component component = Component.text(String.format("Age: %s Day/s", days), NamedTextColor.GRAY);
//                        if (lore.isEmpty()) {
//                            lore.add(component);
//                        } else {
//                            lore.set(0, component);
//                        }
//                    }
//                    else if (months <= 12 ){
//                        Component component = Component.text(String.format("Age: %s Month/s, %s Days", months, days - (months * 30)), NamedTextColor.GRAY);
//                        if (lore.isEmpty()) {
//                            lore.add(component);
//                        } else {
//                            lore.set(0, component);
//                        }
//                    }
//                    else {
//                        Component component = Component.text(String.format("Age: %s Year/s, %s Month/s", years, months - (years * 12)), NamedTextColor.GRAY);
//                        if (lore.isEmpty()) {
//                            lore.add(component);
//                        } else {
//                            lore.set(0, component);
//                        }
//                    }
                
                dataContainer.set(BasicUtils.TickInventories.TEMP_AGE_KEY, PersistentDataType.LONG, tempAge);
                
                
                
                itemMeta.lore(lore);
                itemStack.setItemMeta(itemMeta);
            });
        });
    }

    
}
