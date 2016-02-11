/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.mc;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** Simplistic enumeration of all supported color values. */
public enum ChatColor {

    BLACK('0', "black"),
    DARK_BLUE('1', "dark_blue"),
    DARK_GREEN('2', "dark_green"),
    DARK_AQUA('3', "dark_aqua"),
    DARK_RED('4', "dark_red"),
    DARK_PURPLE('5', "dark_purple"),
    GOLD('6', "gold"),
    GRAY('7', "gray"),
    DARK_GRAY('8', "dark_gray"),
    BLUE('9', "blue"),
    GREEN('a', "green"),
    AQUA('b', "aqua"),
    RED('c', "red"),
    LIGHT_PURPLE('d', "light_purple"),
    YELLOW('e', "yellow"),
    WHITE('f', "white"),
    MAGIC('k', "obfuscated"),
    BOLD('l', "bold"),
    STRIKETHROUGH('m', "strikethrough"),
    UNDERLINE('n', "underline"),
    ITALIC('o', "italic"),
    RESET('r', "reset");

    /**
     * The special character which prefixes all chat colour codes. Use this if
     * you need to dynamically convert colour codes from your custom format.
     */
    public static final char COLOR_CHAR = '\u00A7';
    public static final String ALL_CODES = "0123456789AaBbCcDdEeFfKkLlMmNnOoRr";
    /** Pattern to remove all colour codes. */
    public static final Pattern STRIP_COLOR_PATTERN = Pattern.compile( "(?i)" + String.valueOf( COLOR_CHAR ) + "[0-9A-FK-OR]" );
    /** Colour instances keyed by their active character. */
    private static final Map<Character, ChatColor> BY_CHAR = new HashMap<>();
    /** The code appended to {@link #COLOR_CHAR} to make usable colour. */
    private final char code;
    /** This colour's colour char prefixed by the {@link #COLOR_CHAR}. */
    private final String toString;
    @Getter
    private final String name;

    static {
        for (ChatColor colour : values()) {
            BY_CHAR.put(colour.code, colour);
        }
    }

    ChatColor(char code, String name) {
        this.code = code;
        this.name = name;
        this.toString = new String( new char[] {
            COLOR_CHAR, code
        });
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(final String input) {
        if (input == null) {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        char[] b = textToTranslate.toCharArray();

        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && ALL_CODES.indexOf(b[i + 1] ) > -1) {
                b[i] = ChatColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }

        return new String(b);
    }

    /**
     * Get the colour represented by the specified code.
     *
     * @param code the code to search for
     * @return the mapped colour, or null if non exists
     */
    public static ChatColor getByChar(char code) {
        return BY_CHAR.get(code);
    }
}