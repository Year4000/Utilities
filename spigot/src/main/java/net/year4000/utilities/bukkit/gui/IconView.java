/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit.gui;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface IconView {
    /** Make the item that will be used for the menu view */
    ItemStack make();

    /** Process what happens when you click on the icon */
    boolean action(Player player, ActionMeta meta, InventoryGUI gui);
}
