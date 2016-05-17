/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit.gui;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.year4000.utilities.Utils;
import net.year4000.utilities.bukkit.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

public abstract class AbstractGUI implements Runnable {
    /** The locales for the menus */
    protected final Map<Locale, InventoryGUI> menus = Maps.newConcurrentMap();
    /** A random uuid for the GUI */
    private final UUID uuid = UUID.randomUUID();
    /** The last state of the generate method */
    protected Map<Locale, IconView[][]> last = Maps.newConcurrentMap();
    protected Set<AbstractGUI> subMenus;
    /** Has populateMenu() been ran */
    private boolean populatedMenu = false;
    private boolean generate = false;

    /** Get all the locales this menu can offer */
    public abstract Locale[] getLocales();

    /** Get the locale for the current player */
    public abstract Locale getLocale(Player player);

    /** Register a submenu with the GUI, lazy init sub menu tracker */
    public void registerSubGUI(GUIManager manager, AbstractGUI gui) {
        if (subMenus == null) {
            subMenus = Sets.newHashSet();
        }

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

        Locale language = locale.stripExtensions();
        locale = menus.containsKey(locale) ? locale : menus.containsKey(language) ? language : menus.keySet().iterator().next();
        return menus.get(locale).getInventory();
    }

    /** Handle the preProcess of the menu */
    public void preProcess() throws Exception {
    }

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
                view = new IconView[][]{{}};
            }

            last.put(l, view);
            i.setIcons(view);
            i.populate();
        });

        generate = true;
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
