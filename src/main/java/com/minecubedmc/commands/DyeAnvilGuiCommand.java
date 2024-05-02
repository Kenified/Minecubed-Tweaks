package com.minecubedmc.commands;

import com.minecubedmc.Tweaks;
import com.minecubedmc.util.Cache;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;

public class DyeAnvilGuiCommand extends Command {

    Tweaks plugin;
    protected DyeAnvilGuiCommand(@NotNull String name, Tweaks plugin) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        //Check if sender is player
        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return false;
        }

        //Check if sender has permission
        if (!commandSender.hasPermission("group.helper")){
            commandSender.sendMessage("You do not have permission to use this command.");
            return false;
        }

        final ItemStack[] itemInHand = {player.getInventory().getItemInMainHand()};
        if (itemInHand[0].getType() == Material.LEATHER_HORSE_ARMOR) {
            commandSender.sendMessage("That Item is not dyeable.");
            return false;
        }

        ItemStack itemCopy = itemInHand[0].clone();
        itemCopy.setAmount(1);

        new AnvilGUI.Builder()
                .onClose(stateSnapshot -> {
                    itemCopy.setAmount(itemInHand[0].getAmount());
                    itemInHand[0] = itemCopy;
                })
                .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                    if(slot != AnvilGUI.Slot.OUTPUT) {
                        return Collections.emptyList();
                    }

                    String text = stateSnapshot.getText();
                    if (text.length() < 6) text = text.concat("0".repeat( 6 - text.length()));

                    Color color;
                    try {
                        color = hex2Rgb(text);
                    } catch (StringIndexOutOfBoundsException e) {
                        color = Color.BLACK;
                        return Collections.emptyList();
                    }
                    ItemMeta itemMeta = itemCopy.getItemMeta();
                    if (itemMeta instanceof LeatherArmorMeta meta) {
                        meta.setColor(color);
                    }
                    else if (itemMeta instanceof PotionMeta meta) {
                        meta.setColor(color);
                    }

                    itemCopy.setItemMeta(itemMeta);
                    return Collections.emptyList();
                })
                .itemLeft(Cache.getCustomItem("_iainternal:icon_cancel"))
                .itemRight(itemCopy)
                .text("#")
                .title("Color menu.")
                .plugin(plugin)
                .open(player);

        return true;
    }

    //TODO: move this to a util class
    private Color hex2Rgb(String colorStr) {
        return Color.fromRGB(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16)
        );
    }

}
