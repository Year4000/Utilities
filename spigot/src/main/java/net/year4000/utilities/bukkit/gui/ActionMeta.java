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

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ActionMeta {
    @Getter(AccessLevel.NONE)
    private final InventoryClickEvent event;
    private final Locale locale;
    private final ClickType clickType;
    @NonFinal
    private ItemStack item;
    @NonFinal
    private ItemStack cursor;

    /** Populate Action meta from player, locale, and event */
    public ActionMeta(Locale locale, InventoryClickEvent event) {
        this.locale = checkNotNull(locale, "locale");
        this.event = checkNotNull(event, "event");
        clickType = event.getClick();
        setItem(event.getCurrentItem());
        setCursor(event.getCursor());
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
}
