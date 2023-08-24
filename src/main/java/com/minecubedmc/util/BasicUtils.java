package com.minecubedmc.util;

import com.minecubedmc.Tweaks;
import litebans.api.Database;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class BasicUtils {
    
    public static class PlayerInfoReport {
        private static final String ANTI_CHEAT_ICON = "\uD83E\uDF1E  \uD83E\uDF97";
        private static final String ANTI_CHEAT_NAME = " Minecubed Anticheat";
        private static final Component TEXT_PADDING = Component.text(" ".repeat(15)).toBuilder().build();
        private static final TextColor TITLE_COLOR = TextColor.fromHexString("#ffffff");       // "#17cab4"
        private static final TextColor HIGHLIGHT_COLOR = TextColor.fromHexString("#65dacc");   // "#58c6b8"
        private static final TextColor PRIMARY_COLOR = TextColor.fromHexString("#94aba8");     // "#9bb5b2"
        private static final TextColor SECONDARY_COLOR = TextColor.fromHexString("#6c8381");   // "#7e9290"
        private static final Component COPY_TEXT = Component.text("[Click to copy]", SECONDARY_COLOR).toBuilder().build();
        private static final Component OPEN_TEXT = Component.text("[Click to open link]", SECONDARY_COLOR).toBuilder().build();
        public static @NotNull String capitalizeFirstLetter(final @NotNull String str) {
            if (str.isEmpty()) {
                return str;
            }
            return Character.toUpperCase(str.charAt(0)) + str.substring(1);
        }
    
        public static String getClientVersion(final @NotNull Player player) {
            // https://wiki.vg/Protocol_version_numbers
            final int protocolVersion = player.getProtocolVersion();
            switch (protocolVersion){
                case 759 -> {
                    return "1.19";
                }
                case 760 -> {
                    return "1.19.2";
                }
                case 761 -> {
                    return "1.19.3";
                }
                default -> {
                    return String.format("Unknown Version %s (Ping Ken)", protocolVersion);
                }
            }
        }
    
        public static String getClientBrand(final @NotNull Player player) {
            String clientBrand = player.getClientBrandName();
        
            if (clientBrand == null) {
                clientBrand = "Unknown";
            }
            else {
                // Remove velocity proxy tag from version
                if (clientBrand.contains("Velocity")) clientBrand = clientBrand.replace(" (Velocity)", "");
                // Special case for Salwyrr since it provides unnecessary info
                if (clientBrand.contains("Salwyrr")) clientBrand = clientBrand.split(";")[0];
            
                clientBrand = switch (clientBrand.toLowerCase()) {
                    case "fabric" -> "Fabric Modloader";
                    case "forge" -> "Forge Modloader";
                    default -> capitalizeFirstLetter(clientBrand);
                };
            }
        
            return clientBrand;
        }
    
        private static long getCount(final @NotNull UUID uuid, final @NotNull String table) {
            long count = 0;
            String query = String.format("SELECT COUNT(*) FROM {%s} WHERE uuid=?", table);
            try (PreparedStatement st = Database.get().prepareStatement(query)) {
                st.setString(1, uuid.toString());
                try (ResultSet rs = st.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getLong(1);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return count;
        }
    
        public static long getBanCount(final @NotNull Player player) {
            return getCount(player.getUniqueId(), "bans");
        }
    
        public static long getMuteCount(final @NotNull Player player) {
            return getCount(player.getUniqueId(), "mutes");
        }
    
        public static @NotNull Collection<String> getNoteList(final @NotNull Player player) {
            Collection<String> notes = new ArrayList<>();
            String query = "SELECT reason FROM litebans_warnings WHERE uuid=?";
            try (PreparedStatement st = Database.get().prepareStatement(query)) {
                st.setString(1, player.getUniqueId().toString());
                try (ResultSet rs = st.executeQuery()) {
                    while (rs.next()) {
                        notes.add(rs.getString("reason"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return notes;
        }
    
        public static String[] getClientBrandInfo(final @NotNull String clientBrand) {
            return switch (clientBrand.toLowerCase()) {
                case "fabric modloader" -> new String[]{
                    "Fabric Modloader is a tool that allows you to run Fabric mods on Minecraft",
                    "https://fabricmc.net/"
                };
                case "forge modloader" -> new String[]{
                    "Forge Modloader is a tool that allows you run Forge mods on Minecraft.",
                    "https://forums.minecraftforge.net/"
                };
                case "salwyrr" -> new String[]{
                    "Salwyrr is a Minecraft client that has built in performance, cosmetics and chat mods.",
                    "https://www.salwyrr.com/"
                };
                case "lunar" -> new String[]{
                    "Lunar is a Minecraft client that offers built in mods, cosmetics and performance enhancements.",
                    "https://www.lunarclient.com/"
                };
                case "badlion" -> new String[]{
                    "Badlion Client is a Minecraft client offers built in mods, cosmetics and performance enhancements.",
                    "https://www.badlion.net/"
                };
                case "chatcraft" -> new String[]{
                    "ChatCraft is a mobile app that allows you to connect to any Minecraft server and use chat.",
                    "https://chatcraft.app/"
                };
                case "vanilla" -> new String[]{
                    "Vanilla is the base Minecraft client made by Mojang.",
                    "https://www.minecraft.net/"
                };
                default -> new String[]{
                    "No info for this client, ping Ken to add description.",
                    ""
                };
            };
        }
    
        public static @NotNull Component getPlayerInfoMessage(final @NotNull Player player){
            // Get joined player info
            final String username = player.getName();
            final String uuid = player.getUniqueId().toString();
            final String address = player.getAddress().toString()
                .split(":")[0]
                .replace("/", "");
            String geoLocation = Tweaks.getEssentials().getUser(username).getGeoLocation();
            geoLocation = (geoLocation == null) ? "" : geoLocation;
            final String clientBrand = getClientBrand(player);
            final String clientVersion = getClientVersion(player);
            final long banCount = getBanCount(player);
            final long muteCount = getMuteCount(player);
            final Collection<String> notes = getNoteList(player);
            final int noteCount = notes.size();
        
            // Events for UUID hover
            ClickEvent copyUUID = ClickEvent.copyToClipboard(uuid);
            HoverEvent<Component> showUUID = HoverEvent.showText(Component
                .text("UUID: ", HIGHLIGHT_COLOR)
                .append(Component.text(uuid, PRIMARY_COLOR))
                .append(Component.newline())
                .append(COPY_TEXT)
            );
        
            // Events for address hover
            ClickEvent copyAddress = ClickEvent.copyToClipboard(address);
            HoverEvent<Component> showCopyText = HoverEvent.showText(COPY_TEXT);
        
            // Events for Geolocation hover
            final String mapsURL = "https://www.google.com/maps/search/" + geoLocation
                .replace(" ", "%20");
            ClickEvent openMapsURL = ClickEvent.openUrl(mapsURL);
            HoverEvent<Component> showMapsURL = HoverEvent.showText(Component.text(mapsURL, PRIMARY_COLOR)
                .append(Component.newline())
                .append(OPEN_TEXT)
            );
        
            // Events for client hover
            ClickEvent openClientHomepage = ClickEvent.openUrl(getClientBrandInfo(clientBrand)[1]);
            HoverEvent<Component> showClientDescription = HoverEvent.showText(Component
                .text(getClientBrandInfo(clientBrand)[0], PRIMARY_COLOR)
                .append(Component.newline())
                .append(OPEN_TEXT)
            );
        
            // Events for note list
            Component noteText = Component.text("Notes:", HIGHLIGHT_COLOR);
            for (String note : notes) {
                noteText = noteText.append(Component.newline()
                    .append(Component.text(String.format("• %s", note), PRIMARY_COLOR))
                );
            }
            HoverEvent<Component> showNoteList = HoverEvent.showText(noteText);
        
            return Component.text(ANTI_CHEAT_ICON, TITLE_COLOR)
                .append(Component.text(ANTI_CHEAT_NAME, TITLE_COLOR, TextDecoration.BOLD))
                .append(Component.newline())
                .append(TEXT_PADDING
                    .append(Component.text("Username: ", HIGHLIGHT_COLOR))
                    .append(Component.text(username, PRIMARY_COLOR))
                    .append(Component.text(" (", SECONDARY_COLOR))
                    .append(Component.text(player.getDisplayName(), SECONDARY_COLOR))
                    .append(Component.text(")", SECONDARY_COLOR))
                    .clickEvent(copyUUID)
                    .hoverEvent(showUUID)
                )
                .append(Component.newline())
                .append(TEXT_PADDING
                    .append(Component.text("IP: ", HIGHLIGHT_COLOR))
                    .append(Component.text(
                            (address.length() > 45) ? (address.substring(0, 45) + "...") : address,
                            PRIMARY_COLOR
                        )
                    )
                    .clickEvent(copyAddress)
                    .hoverEvent(showCopyText)
                )
                .append(Component.newline())
                .append(TEXT_PADDING
                    .append(Component.text("Location: ", HIGHLIGHT_COLOR))
                    .append(Component.text(geoLocation, PRIMARY_COLOR))
                    .clickEvent(openMapsURL)
                    .hoverEvent(showMapsURL)
                )
                .append(Component.newline())
                .append(TEXT_PADDING
                    .append(Component.text("Client: ", HIGHLIGHT_COLOR))
                    .append(Component.text(clientBrand, PRIMARY_COLOR))
                    .append(Component.text(" (", NamedTextColor.DARK_GRAY))
                    .append(Component.text(clientVersion, SECONDARY_COLOR))
                    .append(Component.text(")", NamedTextColor.DARK_GRAY))
                    .clickEvent(openClientHomepage)
                    .hoverEvent(showClientDescription)
                )
                .append(Component.newline())
                .append(TEXT_PADDING
                    .append(Component.text("Bans: ", HIGHLIGHT_COLOR))
                    .append(Component.text(banCount, PRIMARY_COLOR))
                    .append(Component.text(", ", PRIMARY_COLOR))
                    .append(Component.text("Mutes: ", HIGHLIGHT_COLOR))
                    .append(Component.text(muteCount, PRIMARY_COLOR))
                    .append(Component.text(", ", PRIMARY_COLOR))
                    .append(Component.text("Notes: ", HIGHLIGHT_COLOR))
                    .append(Component.text(noteCount, PRIMARY_COLOR))
                    .hoverEvent(showNoteList)
                )
                .toBuilder().build();
        }
    }
    
    public static boolean[] convertToBinaryArray(final int number) {
        boolean[] binaryDigits = new boolean[7];
        for (int i = 0; i < 7; i++) {
            binaryDigits[6 - i] = (number & (1 << i)) != 0;
        }
        return binaryDigits;
    }
    
    public static class TickInventories {
        
        public static NamespacedKey TOTAL_AGE_KEY;
        public static NamespacedKey START_TIME_KEY;
        public static NamespacedKey TEMP_AGE_KEY;
        
        private static final HashMap<UUID, Inventory> tickingInventories = new HashMap<>();
        public static HashMap<UUID, Inventory> getTickingInventories () {
            return tickingInventories;
        }
    
        public static void addTickingInventory (final @NotNull UUID uuid, final @NotNull Inventory inventory) {
           TickInventories.tickingInventories.put(uuid, inventory);
        }
    
        public static void removeTickingInventory(final @NotNull UUID uuid) {
            TickInventories.tickingInventories.remove(uuid);
        }
    
        //TODO
        public static boolean isAgingItem(final @NotNull ItemStack itemStack) {
            return itemStack.getType().equals(Material.DIAMOND);
//            if (itemStack.equals(CustomItems.getCustomItem("minecubed:wine"))) return true;
//            else if (itemStack.equals(CustomItems.getCustomItem("minecubed:whiskey"))) return true;
//            else return false;
        }
    }
    
    
    
    
}
