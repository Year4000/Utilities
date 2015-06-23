package net.year4000.utilities.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public interface IconView {
    /** Make the item that will be used for the menu view */
    ItemStack make();

    /** Process what happens when you click on the icon */
    void action(Locale locale, Player player, InventoryGUI gui);
}
