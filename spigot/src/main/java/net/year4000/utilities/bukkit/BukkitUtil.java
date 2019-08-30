/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit;

import com.google.common.collect.ImmutableMap;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public final class BukkitUtil {
    /** Color to DyeColor Map */
    public static final Map<Color, DyeColor> COLOR_MAP = ImmutableMap.<Color, DyeColor>builder()
        .putAll(new HashMap<Color, DyeColor>() {{
            put(Color.AQUA, DyeColor.LIGHT_BLUE);
            put(Color.BLACK, DyeColor.BLACK);
            put(Color.BLUE, DyeColor.LIGHT_BLUE);
            put(Color.FUCHSIA, DyeColor.PINK);
            put(Color.GRAY, DyeColor.GRAY);
            put(Color.GREEN, DyeColor.GREEN);
            put(Color.LIME, DyeColor.LIME);
            put(Color.MAROON, DyeColor.MAGENTA);
            put(Color.NAVY, DyeColor.BLUE);
            put(Color.OLIVE, DyeColor.GREEN);
            put(Color.ORANGE, DyeColor.ORANGE);
            put(Color.PURPLE, DyeColor.PURPLE);
            put(Color.RED, DyeColor.RED);
            put(Color.SILVER, DyeColor.LIGHT_GRAY);
            put(Color.YELLOW, DyeColor.YELLOW);
            put(Color.WHITE, DyeColor.WHITE);
            put(Color.TEAL, DyeColor.LIGHT_BLUE);
        }}).build();
    /** DyeColor to Color Map */
    public static final Map<DyeColor, Color> DYECOLOR_MAP = ImmutableMap.<DyeColor, Color>builder()
        .putAll(new HashMap<DyeColor, Color>() {{
            put(DyeColor.LIGHT_BLUE, Color.AQUA);
            put(DyeColor.BLACK, Color.BLACK);
            put(DyeColor.PINK, Color.FUCHSIA);
            put(DyeColor.GRAY, Color.GRAY);
            put(DyeColor.GREEN, Color.GREEN);
            put(DyeColor.LIME, Color.LIME);
            put(DyeColor.MAGENTA, Color.MAROON);
            put(DyeColor.BLUE, Color.BLUE);
            put(DyeColor.ORANGE, Color.ORANGE);
            put(DyeColor.PURPLE, Color.PURPLE);
            put(DyeColor.RED, Color.RED);
            put(DyeColor.LIGHT_GRAY, Color.SILVER);
            put(DyeColor.YELLOW, Color.YELLOW);
            put(DyeColor.WHITE, Color.WHITE);
            put(DyeColor.CYAN, Color.TEAL);
        }}).build();
    /** ChatColor to DyeColor */
    public static final Map<ChatColor, DyeColor> CHATCOLOR_MAP = ImmutableMap.<ChatColor, DyeColor>builder()
        .putAll(new HashMap<ChatColor, DyeColor>() {{
            put(ChatColor.AQUA, DyeColor.LIGHT_BLUE);
            put(ChatColor.BLACK, DyeColor.BLACK);
            put(ChatColor.BLUE, DyeColor.BLUE);
            put(ChatColor.DARK_AQUA, DyeColor.CYAN);
            put(ChatColor.DARK_BLUE, DyeColor.BLUE);
            put(ChatColor.DARK_GRAY, DyeColor.GRAY);
            put(ChatColor.DARK_GREEN, DyeColor.GREEN);
            put(ChatColor.DARK_PURPLE, DyeColor.PURPLE);
            put(ChatColor.DARK_RED, DyeColor.RED);
            put(ChatColor.GOLD, DyeColor.ORANGE);
            put(ChatColor.GRAY, DyeColor.LIGHT_GRAY);
            put(ChatColor.GREEN, DyeColor.LIME);
            put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA);
            put(ChatColor.RED, DyeColor.RED);
            put(ChatColor.WHITE, DyeColor.WHITE);
            put(ChatColor.YELLOW, DyeColor.YELLOW);
        }}).build();

    private BukkitUtil() {
    }

    /**
     * Get the Color for the DyeColor match.
     *
     * @param dyeColor The DyeColor.
     * @return The Color.
     */
    public static Color dyeColorToColor(DyeColor dyeColor) {
        Color color = DYECOLOR_MAP.get(dyeColor);
        return color == null ? Color.WHITE : color;
    }

    /**
     * Get the DyeColor for the Color match.
     *
     * @param color The Color.
     * @return The DyeColor.
     */
    public static DyeColor colorToDyeColor(Color color) {
        DyeColor dyeColor = COLOR_MAP.get(color);
        return dyeColor == null ? DyeColor.WHITE : dyeColor;
    }

    /**
     * Get the  for the Color match.
     *
     * @param color The Color.
     * @return The DyeColor.
     */
    public static DyeColor chatColorToDyeColor(ChatColor color) {
        DyeColor dyeColor = CHATCOLOR_MAP.get(color);
        return dyeColor == null ? DyeColor.WHITE : dyeColor;
    }

    /**
     * Gets the multiples of 9 for inventories.
     *
     * @param size The size that will do math to get the multiple of 9.
     * @return the base size of the inventory.
     */
    public static int invBase(int size) {
        return (size % 9 == 0) ? (size / 9) * 9 : (1 + (size / 9)) * 9;
    }
}
