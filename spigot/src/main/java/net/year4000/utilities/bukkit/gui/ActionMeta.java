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

import lombok.experimental.NonFinal;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;

public final class ActionMeta {
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

    @java.beans.ConstructorProperties({"event", "locale", "clickType", "item", "cursor"})
    private ActionMeta(InventoryClickEvent event, Locale locale, ClickType clickType, ItemStack item, ItemStack cursor) {
        this.event = event;
        this.locale = locale;
        this.clickType = clickType;
        this.item = item;
        this.cursor = cursor;
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ActionMeta)) return false;
        final ActionMeta other = (ActionMeta) o;
        final Object this$event = this.event;
        final Object other$event = other.event;
        if (this$event == null ? other$event != null : !this$event.equals(other$event)) return false;
        final Object this$locale = this.locale;
        final Object other$locale = other.locale;
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        final Object this$clickType = this.clickType;
        final Object other$clickType = other.clickType;
        if (this$clickType == null ? other$clickType != null : !this$clickType.equals(other$clickType)) return false;
        final Object this$item = this.item;
        final Object other$item = other.item;
        if (this$item == null ? other$item != null : !this$item.equals(other$item)) return false;
        final Object this$cursor = this.cursor;
        final Object other$cursor = other.cursor;
        if (this$cursor == null ? other$cursor != null : !this$cursor.equals(other$cursor)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $event = this.event;
        result = result * PRIME + ($event == null ? 0 : $event.hashCode());
        final Object $locale = this.locale;
        result = result * PRIME + ($locale == null ? 0 : $locale.hashCode());
        final Object $clickType = this.clickType;
        result = result * PRIME + ($clickType == null ? 0 : $clickType.hashCode());
        final Object $item = this.item;
        result = result * PRIME + ($item == null ? 0 : $item.hashCode());
        final Object $cursor = this.cursor;
        result = result * PRIME + ($cursor == null ? 0 : $cursor.hashCode());
        return result;
    }

    public String toString() {
        return "net.year4000.utilities.bukkit.gui.ActionMeta(event=" + this.event + ", locale=" + this.locale + ", clickType=" + this.clickType + ", item=" + this.item + ", cursor=" + this.cursor + ")";
    }
}
