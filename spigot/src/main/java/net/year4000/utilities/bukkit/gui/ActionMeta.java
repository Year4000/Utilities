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

import net.year4000.utilities.Conditions;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

public final class ActionMeta {
    private final InventoryClickEvent event;
    private final Locale locale;
    private final ClickType clickType;
    private ItemStack item;
    private ItemStack cursor;

    /** Populate Action meta from player, locale, and event */
    public ActionMeta(Locale locale, InventoryClickEvent event) {
        this.locale = Conditions.nonNull(locale, "locale");
        this.event = Conditions.nonNull(event, "event");
        clickType = event.getClick();
        setItem(event.getCurrentItem());
        setCursor(event.getCursor());
    }

    private ActionMeta(InventoryClickEvent event, Locale locale, ClickType clickType, ItemStack item, ItemStack cursor) {
        this.event = Conditions.nonNull(event, "event");
        this.locale = Conditions.nonNull(locale, "locale");
        this.clickType = Conditions.nonNull(clickType, "clickType");
        this.item = Conditions.nonNull(item, "item");
        this.cursor = Conditions.nonNull(cursor, "cursor");
    }

    /** Set the item */
    public void setItem(ItemStack item) {
        this.item = item == null ? new ItemStack(Material.AIR) : item;
        event.setCurrentItem(item);
    }

    /** Set the cursor */
    public void setCursor(ItemStack cursor) {
        this.cursor = cursor == null ? new ItemStack(Material.AIR) : cursor;
        event.setCursor(item);
    }

    public Locale getLocale() {
        return this.locale;
    }

    public ClickType getClickType() {
        return this.clickType;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public ItemStack getCursor() {
        return this.cursor;
    }

    @Override
    public String toString() {
        return Conditions.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Conditions.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Conditions.hashCode(this);
    }
}
