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
import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.year4000.utilities.bukkit.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static net.year4000.utilities.locale.AbstractLocaleManager.toLanguage;

@ToString
@EqualsAndHashCode(of = {"uuid"})
public abstract class AbstractGUI implements Runnable {
    /** A random uuid for the GUI */
    private final UUID uuid = UUID.randomUUID();

    /** The locales for the menus */
    protected final Map<Locale, InventoryGUI> menus = Maps.newConcurrentMap();
    /** The last state of the generate method */
    protected Map<Locale, IconView[][]> last = Maps.newConcurrentMap();

    /** Get all the locales this menu can offer */
    public abstract Locale[] getLocales();

    /** Get the locale for the current player */
    public abstract Locale getLocale(Player player);

    /** Has populateMenu() been ran */
    private boolean populatedMenu = false;
    private boolean generate = false;
    protected Set<AbstractGUI> subMenus = Sets.newHashSet();

    /** Register a submenu with the GUI */
    public void registerSubGUI(GUIManager manager, AbstractGUI gui) {
        if (subMenus.add(gui)) {
            manager.registerMenu(gui);
        }
    }

    /** Populates the menus with known locales */
    public void populateMenu(Function<Locale, String> function, int rows) {
        checkArgument(rows > -1, "Rows must be 0 or larger");
        Collection<Locale> locales = Lists.newArrayList(getLocales());

        for (Locale locale : locales) {
            String title = function == null ? "null" : function.apply(locale);
            InventoryGUI inventoryGUI = new InventoryGUI(title, rows);
            menus.put(locale, inventoryGUI);
        }

        checkState(menus.size() > 0, "Menu must contain a locale");
        populatedMenu = true;
    }

    /** Open the inventory that matches the player locale */
    public void openInventory(Player player) {
        checkNotNull(player, "player");
        checkState(populatedMenu, "Must run AbstractGUI::populateMenu() before AbstractGUI::openInventory(Locale)");
        checkState(generate, "Must run AbstractGUI::run() before AbstractGUI::getInventory(Locale)");

        Locale locale = getLocale(player);
        player.openInventory(getInventory(locale));
    }

    /** Process the action for the given IconView */
    boolean processAction(Player player, ActionMeta meta, int row, int col) {
        try {
            final Locale locale = getLocale(player);
            IconView[][] views = last.get(last.containsKey(locale) ? locale : Locale.US);

            if (views != null && row >= 0 && views.length > row && col >= 0 && views[row].length > col) {
                Optional<IconView> view = Optional.ofNullable(views[row][col]);

                if (view.isPresent()) {
                    return view.get().action(player, meta, menus.get(locale));
                }
            }
        }
        catch (Exception e) {
            Utilities.debug("AbstractGUI processAction(): ");
            Utilities.debug(e, true);
        }

        return false;
    }

    /** Get the inventory for the specific locale or english by default */
    public Inventory getInventory(Locale locale) {
        checkNotNull(locale, "locale");
        checkState(populatedMenu, "Must run AbstractGUI::populateMenu() before AbstractGUI::getInventory(Locale)");
        checkState(generate, "Must run AbstractGUI::run() before AbstractGUI::getInventory(Locale)");

        Locale language = toLanguage(locale);
        locale = menus.containsKey(locale) ? locale : menus.containsKey(language) ? language : menus.keySet().iterator().next();
        return menus.get(locale).getInventory();
    }

    /** Handle the preProcess of the menu */
    public void preProcess() throws Exception {}

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
                view = new IconView[][] {{}};
            }

            last.put(l, view);
            i.setIcons(view);
            i.populate();
        });

        generate = true;
    }
}
