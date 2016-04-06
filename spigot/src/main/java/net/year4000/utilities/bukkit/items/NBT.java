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

import com.google.gson.annotations.SerializedName;

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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof NBT)) return false;
        final NBT other = (NBT) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$title = this.title;
        final Object other$title = other.title;
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        final Object this$author = this.author;
        final Object other$author = other.author;
        if (this$author == null ? other$author != null : !this$author.equals(other$author)) return false;
        if (!java.util.Arrays.deepEquals(this.pages, other.pages)) return false;
        if (this.unbreakable != other.unbreakable) return false;
        if (!java.util.Arrays.deepEquals(this.hideFlags, other.hideFlags)) return false;
        if (!java.util.Arrays.deepEquals(this.enchantments, other.enchantments)) return false;
        final Object this$display = this.display;
        final Object other$display = other.display;
        if (this$display == null ? other$display != null : !this$display.equals(other$display)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.title;
        result = result * PRIME + ($title == null ? 0 : $title.hashCode());
        final Object $author = this.author;
        result = result * PRIME + ($author == null ? 0 : $author.hashCode());
        result = result * PRIME + java.util.Arrays.deepHashCode(this.pages);
        result = result * PRIME + (this.unbreakable ? 79 : 97);
        result = result * PRIME + java.util.Arrays.deepHashCode(this.hideFlags);
        result = result * PRIME + java.util.Arrays.deepHashCode(this.enchantments);
        final Object $display = this.display;
        result = result * PRIME + ($display == null ? 0 : $display.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof NBT;
    }

    public String toString() {
        return "net.year4000.utilities.bukkit.items.NBT(title=" + this.title + ", author=" + this.author + ", pages=" + java.util.Arrays.deepToString(this.pages) + ", unbreakable=" + this.unbreakable + ", hideFlags=" + java.util.Arrays.deepToString(this.hideFlags) + ", enchantments=" + java.util.Arrays.deepToString(this.enchantments) + ", display=" + this.display + ")";
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

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Enchantments)) return false;
            final Enchantments other = (Enchantments) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.name;
            final Object other$name = other.name;
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
            if (this.level != other.level) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.name;
            result = result * PRIME + ($name == null ? 0 : $name.hashCode());
            result = result * PRIME + this.level;
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Enchantments;
        }

        public String toString() {
            return "net.year4000.utilities.bukkit.items.NBT.Enchantments(name=" + this.name + ", level=" + this.level + ")";
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

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Display)) return false;
            final Display other = (Display) o;
            if (!other.canEqual((Object) this)) return false;
            final Object this$name = this.name;
            final Object other$name = other.name;
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
            if (!java.util.Arrays.deepEquals(this.lore, other.lore)) return false;
            final Object this$color = this.color;
            final Object other$color = other.color;
            if (this$color == null ? other$color != null : !this$color.equals(other$color)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.name;
            result = result * PRIME + ($name == null ? 0 : $name.hashCode());
            result = result * PRIME + java.util.Arrays.deepHashCode(this.lore);
            final Object $color = this.color;
            result = result * PRIME + ($color == null ? 0 : $color.hashCode());
            return result;
        }

        protected boolean canEqual(Object other) {
            return other instanceof Display;
        }

        public String toString() {
            return "net.year4000.utilities.bukkit.items.NBT.Display(name=" + this.name + ", lore=" + java.util.Arrays.deepToString(this.lore) + ", color=" + this.color + ")";
        }
    }
}
