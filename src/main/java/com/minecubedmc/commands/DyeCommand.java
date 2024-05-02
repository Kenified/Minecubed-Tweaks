package com.minecubedmc.commands;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class DyeCommand extends Command {

    public DyeCommand(String label) {
        super(label);
    }


    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        //Check if sender is player
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        //Check if sender has permission
        if (!sender.hasPermission("group.helper")){
            sender.sendMessage("You do not have permission to use this command.");
            return false;
        }

        //Return if there aren't any arguments
        if (args.length <= 0){
            sender.sendMessage("Please provide a hex color code or RGB values.");
            return false;
        }

        //Convert the HEX or get RGB value
        Color hexColor;
        if (args.length ==  1){

            //Get the hex code from input and convert it to rgb
            try {
                hexColor = hex2Rgb(args[0]);
            } catch (StringIndexOutOfBoundsException e) {
                sender.sendMessage("Invalid color code. Please specify a valid HEX color code.");
                return false;
            }
        }
        else if (args.length ==  3){
            try {
                hexColor = Color.fromRGB(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            } catch (StringIndexOutOfBoundsException e) {
                sender.sendMessage("Invalid color format. Please specify a valid \"RED GREEN BLUE\" value.");
                return false;
            }
        }
        else {
            sender.sendMessage("This command can only be used by players.");
            return false;
        }

        //Get the player
        Player player = ((Player) sender).getPlayer();

        //Get the item in players hand
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() ==  Material.AIR) {
            sender.sendMessage("You are not holding any item.");
            return false;
        }

        //Get the item meta and check if item is dyable
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta instanceof LeatherArmorMeta meta) {
            meta.setColor(hexColor);
        }
        else if (itemMeta instanceof PotionMeta meta) {
            meta.setColor(hexColor);
        }
        else {
            sender.sendMessage("This item cannot be dyed.");
            return false;
        }

        //Set the color
        item.setItemMeta(itemMeta);
        sender.sendMessage("Successfully dyed the item in your main hand with the specified color.");
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
