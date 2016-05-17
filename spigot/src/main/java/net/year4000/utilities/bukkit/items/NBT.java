/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit.items;

import com.google.gson.annotations.SerializedName;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

@SuppressWarnings("unused")
/** NBT data of the item. */
public class NBT {
    /** Book Title. */
    private String title;

    /** Book Author. */
    private String author;

    /** Each array is a page in a book. */
    private String[] pages;

    /** Is this item unbreakable */
    private boolean unbreakable = false;

    @SerializedName("hide_flags")
    private String[] hideFlags = new String[0];

    /** The enchants the item will have. */
    private Enchantments[] enchantments;

    /** Control the display of the item. */
    private Display display;

    public NBT() {
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String[] getPages() {
        return this.pages;
    }

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public String[] getHideFlags() {
        return this.hideFlags;
    }

    public Enchantments[] getEnchantments() {
        return this.enchantments;
    }

    public Display getDisplay() {
        return this.display;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPages(String[] pages) {
        this.pages = pages;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public void setHideFlags(String[] hideFlags) {
        this.hideFlags = hideFlags;
    }

    public void setEnchantments(Enchantments[] enchantments) {
        this.enchantments = enchantments;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }

    /** The enchants the item will have. */
    public class Enchantments {
        /** Enchantment id number. */
        private String name;

        /** Enchantment level amount. */
        private int level;

        public Enchantments() {
        }

        public String getName() {
            return this.name;
        }

        public int getLevel() {
            return this.level;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }

    /** Control the display of the item. */
    public class Display {
        /** The name of the item. */
        private String name;

        /** The lore of the item, each array is a new line. */
        private String[] lore;

        /** The color of leather armor. */
        private String color;

        public Display() {
        }

        public String getName() {
            return this.name;
        }

        public String[] getLore() {
            return this.lore;
        }

        public String getColor() {
            return this.color;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLore(String[] lore) {
            this.lore = lore;
        }

        public void setColor(String color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }
}
