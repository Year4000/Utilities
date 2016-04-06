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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof GUIManager)) return false;
        final GUIManager other = (GUIManager) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$menus = this.getMenus();
        final Object other$menus = other.getMenus();
        if (this$menus == null ? other$menus != null : !this$menus.equals(other$menus)) return false;
        final Object this$locale = this.locale;
        final Object other$locale = other.locale;
        if (this$locale == null ? other$locale != null : !this$locale.equals(other$locale)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $menus = this.getMenus();
        result = result * PRIME + ($menus == null ? 0 : $menus.hashCode());
        final Object $locale = this.locale;
        result = result * PRIME + ($locale == null ? 0 : $locale.hashCode());
        return result;
    }

    protected boolean canEqual(Object other) {
        return other instanceof GUIManager;
    }

    public String toString() {
        return "net.year4000.utilities.bukkit.gui.GUIManager(menus=" + this.getMenus() + ", locale=" + this.locale + ")";
    }

    public void setLocale(Function<Player, Locale> locale) {
        this.locale = locale;
    }
}
