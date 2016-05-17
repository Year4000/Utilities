/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit.gui;

import com.google.common.collect.Sets;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.bukkit.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class GUIManager implements Listener {
    private Set<AbstractGUI> menus = Sets.newHashSet();
    private Function<Player, Locale> locale = (player) -> Locale.US;

    /** Add a menu to the GUIManger to be listen by an action */
    public void registerMenu(AbstractGUI gui) {
        checkArgument(!menus.contains(gui));
        menus.add(checkNotNull(gui, "gui"));
    }

    /** Remove a menu to the GUIManger to be listen by an action */
    public void unregisterMenu(AbstractGUI gui) {
        checkArgument(menus.contains(gui));
        menus.remove(checkNotNull(gui, "gui"));
    }

    /** Remove a menu to the GUIManger to be listen by an action */
    public void unregisterMenus() {
        Sets.newHashSet(menus).forEach(this::unregisterMenu);
        menus = Sets.newHashSet();
    }

    /** Get all menus including sub menus */
    private Set<AbstractGUI> getMenus() {
        Set<AbstractGUI> allMenus = Sets.newHashSet(menus);
        menus.stream()
            .map(gui -> gui.subMenus)
            .filter(menu -> menu != null)
            .forEach(allMenus::addAll);
        return allMenus;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onIconClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Locale locale;
        Player player;

        // If a player click inventory get their locale else return
        if (inventory != null && inventory.getHolder() != null && event.getWhoClicked() instanceof Player) {
            player = (Player) event.getWhoClicked();
            locale = this.locale.apply(player);
        }
        else {
            return;
        }

        // Get proper locale
        for (AbstractGUI gui : getMenus()) {
            Inventory guiInventory = gui.getInventory(locale);

            if (inventory.getHolder().equals(guiInventory.getHolder())) {
                int slot = event.getSlot();
                int rows = slot / InventoryGUI.COLS;
                int cols = slot % InventoryGUI.COLS;

                ActionMeta meta = new ActionMeta(locale, event);
                boolean cancel = gui.processAction(player, meta, rows, cols);

                if (cancel) {
                    event.setCancelled(true);
                    event.setCursor(ItemUtil.makeItem(Material.AIR));
                }

                return;
            }
        }
    }

    public void setLocale(Function<Player, Locale> locale) {
        this.locale = locale;
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
