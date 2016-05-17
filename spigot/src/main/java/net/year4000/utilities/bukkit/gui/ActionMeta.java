/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit.gui;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
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
