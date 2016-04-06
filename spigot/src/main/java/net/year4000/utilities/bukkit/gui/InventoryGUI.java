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

package net.year4000.utilities.bukkit.gui;

import com.google.common.base.Ascii;
import net.year4000.utilities.bukkit.BukkitUtil;
import net.year4000.utilities.bukkit.ItemUtil;
import net.year4000.utilities.mc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public final class InventoryGUI implements InventoryHolder {
    public static final int COLS = 9;
    private final UUID uuid = UUID.randomUUID();
    private Inventory inventory;
    private IconView[][] icons;
    private String title;
    private int size;

    /** Create an inventory view with a title and row */
    public InventoryGUI(String title, int rows) {
        this.icons = new IconView[rows][COLS];
        this.title = Ascii.truncate(MessageUtil.replaceColors(title), 32, "");
        this.size = rows * COLS;
        this.inventory = Bukkit.createInventory(this, BukkitUtil.invBase(size), this.title);
    }

    /** Create an inventory view with a title and row */
    public InventoryGUI(String title, IconView[][] icons) {
        this.icons = icons;
        this.title = Ascii.truncate(MessageUtil.replaceColors(title), 32, "");
        this.size = icons.length * COLS;
        this.inventory = Bukkit.createInventory(this, BukkitUtil.invBase(size), this.title);
    }

    /** Set the icon view 2d array */
    public void setIcons(IconView[][] icons) {
        this.icons = new IconView[icons.length][COLS];

        // Copy the array, but with limit restrictions
        for (int x = 0; x < icons.length; x++) {
            for (int y = 0; y < COLS; y++) {
                this.icons[x][y] = icons[x].length < y ? null : icons[x][y];
            }
        }
    }

    /** Populate the inventory with the ItemView[][] and return the item inventory */
    public Inventory populate() {
        inventory.clear();
        int counter = 0;

        for (IconView[] icon : icons) {
            for (int y = 0; y < COLS; y++) {
                ItemStack item = icon[y] == null ? null : icon[y].make();
                inventory.setItem(counter++, item == null ? ItemUtil.makeItem("air") : item);
            }
        }

        return inventory;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof InventoryGUI)) return false;
        final InventoryGUI other = (InventoryGUI) o;
        final Object this$uuid = this.uuid;
        final Object other$uuid = other.uuid;
        if (this$uuid == null ? other$uuid != null : !this$uuid.equals(other$uuid)) return false;
        final Object this$title = this.title;
        final Object other$title = other.title;
        if (this$title == null ? other$title != null : !this$title.equals(other$title)) return false;
        if (this.size != other.size) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $uuid = this.uuid;
        result = result * PRIME + ($uuid == null ? 0 : $uuid.hashCode());
        final Object $title = this.title;
        result = result * PRIME + ($title == null ? 0 : $title.hashCode());
        result = result * PRIME + this.size;
        return result;
    }

    public String toString() {
        return "net.year4000.utilities.bukkit.gui.InventoryGUI(uuid=" + this.uuid + ", icons=" + java.util.Arrays.deepToString(this.icons) + ", title=" + this.title + ", size=" + this.size + ")";
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
