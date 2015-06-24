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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.year4000.utilities.bukkit.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public abstract class AbstractGUI implements Runnable {
    /** The locales for the menus */
    protected final Map<Locale, InventoryGUI> menus = Maps.newConcurrentMap();
    /** The last state of the generate method */
    protected Map<Locale, IconView[][]> last = Maps.newConcurrentMap();

    /** Get all the locales this menu can offer */
    public abstract Locale[] getLocales();

    /** Get the locale for the current player */
    public abstract Locale getLocale(Player player);

    /** Populates the menus with known locales */
    public void populateMenu(Function<Locale, String> function, int rows) {
        Collection<Locale> locales = Lists.newArrayList(getLocales());

        for (Locale locale : locales) {
            String title = function.apply(locale);
            InventoryGUI inventoryGUI = new InventoryGUI(title, rows);
            menus.put(locale, inventoryGUI);
        }
    }

    /** Open the inventory that matches the player locale */
    public void openInventory(Player player) {
        Locale locale = getLocale(player);
        Locale english = Locale.US;
        InventoryGUI gui = menus.getOrDefault(locale, menus.get(english));
        player.openInventory(gui.getInventory());
    }

    /** Process the action for the given IconView */
    public void processAction(Player player, int row, int col) {
        try {
            Locale locale = getLocale(player);
            locale = last.containsKey(locale) ? locale : Locale.US;
            IconView[][] views = last.get(locale);
            IconView view = views[row][col];
            view.action(locale, player, menus.get(locale));
        }
        catch (Exception e) {
            Utilities.debug("AbstractGUI processAction(): ");
            Utilities.debug(e, true);
        }
    }

    /** Get the inventory for the specific locale or english by default */
    public Inventory getInventory(Locale locale) {
        locale = menus.containsKey(locale) ? locale : Locale.US;
        return menus.get(locale).getInventory();
    }

    /** Handle the preProcess of the menu */
    public abstract void preProcess() throws Exception;

    /** Generate the 2d array for the menu */
    public abstract IconView[][] generate(Locale locale);

    /** Handle the pre and post processing of the menu gui */
    @Override
    public void run() {
        // Run preProcess
        try {
            preProcess();
        }
        catch (Exception e) {
            Utilities.debug(e, false);
        }

        // Store the IconView in the inventory
        menus.forEach((l, i) -> {
            IconView[][] view;

            // Run the generate
            try {
                view = generate(l);
            }
            catch (Exception e) {
                Utilities.debug(e, false);
                view = new IconView[][]{{null, null, null, null, null}};
            }

            last.put(l, view);
            i.setIcons(view);
            i.populate();
        });
    }
}
