package net.year4000.utilities.bukkit.gui;

import com.google.common.base.Ascii;
import lombok.Getter;
import net.year4000.utilities.MessageUtil;
import net.year4000.utilities.bukkit.BukkitUtil;
import net.year4000.utilities.bukkit.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryGUI {
    public static final int COLS = 9;
    @Getter
    private Inventory inventory;
    private IconView[][] icons;
    private String title;
    private int size;

    /** Create an inventory view with a title and row */
    public InventoryGUI(String title, int rows) {
        this.icons = new IconView[rows][COLS];
        this.title = Ascii.truncate(MessageUtil.replaceColors(title), 32, "");
        this.size = rows * COLS;
        this.inventory = Bukkit.createInventory(null, BukkitUtil.invBase(size), this.title);
    }

    /** Create an inventory view with a title and row */
    public InventoryGUI(String title, IconView[][] icons) {
        this.icons = icons;
        this.title = Ascii.truncate(MessageUtil.replaceColors(title), 32, "");
        this.size = icons.length * COLS;
        this.inventory = Bukkit.createInventory(null, BukkitUtil.invBase(size), this.title);
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
}
