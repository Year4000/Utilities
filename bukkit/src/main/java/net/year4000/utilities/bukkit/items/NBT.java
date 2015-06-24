/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
