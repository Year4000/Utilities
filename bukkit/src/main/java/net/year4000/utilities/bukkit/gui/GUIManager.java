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

import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class GUIManager implements Listener {
    private Set<AbstractGUI> menus = Sets.newHashSet();
    @Setter
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

    @EventHandler
    public void onIconClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        String title = inventory.getTitle();
        Locale locale;
        Player player;

        // If a player click inventory get their locale else return
        if (event.getWhoClicked() instanceof Player) {
            player = (Player) event.getWhoClicked();
            locale = this.locale.apply(player);
        }
        else {
            return;
        }

        // Get proper locale
        for (AbstractGUI gui : menus) {
            Inventory guiInventory = gui.getInventory(locale);

            if (guiInventory.getTitle().equals(title)) {
                int slot = event.getSlot();
                int rows = slot / InventoryGUI.COLS;
                int cols = slot % InventoryGUI.COLS;

                ActionMeta meta = new ActionMeta(locale, event);
                gui.processAction(player, meta, rows, cols);
                event.setCancelled(true);
                return;
            }
        }
    }
}
