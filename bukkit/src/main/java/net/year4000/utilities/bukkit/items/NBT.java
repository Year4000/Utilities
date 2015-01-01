package net.year4000.utilities.bukkit.items;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings("unused")
/** NBT data of the item. */
public class NBT {
    /** Book Title. */
    private String title;

    /** Book Author. */
    private String author;

    /** Each array is a page in a book. */
    private String[] pages;

    /** The enchants the item will have. */
    private Enchantments[] enchantments;

    /** Control the display of the item. */
    private Display display;

    @Data
    @NoArgsConstructor
    /** The enchants the item will have. */
    public class Enchantments {
        /** Enchantment id number. */
        private String name;

        /** Enchantment level amount. */
        private int level;
    }

    @Data
    @NoArgsConstructor
    /** Control the display of the item. */
    public class Display {
        /** The name of the item. */
        private String name;

        /** The lore of the item, each array is a new line. */
        private String[] lore;

        /** The color of leather armor. */
        private String color;
    }
}
