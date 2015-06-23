package net.year4000.utilities.bukkit.gui;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@ToString
@EqualsAndHashCode
public class GUIManager implements Listener {
    private List<AbstractGUI> menus = Lists.newArrayList();
    @Setter
    private Function<Player, Locale> locale = (player) -> Locale.US;

    /** Add a menu to the GUIManger to be listen by an action */
    public void registerMenu(AbstractGUI gui) {
        checkArgument(!menus.contains(gui));
        menus.add(checkNotNull(gui, "gui"));
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

                gui.processAction(player, rows, cols);
                event.setCancelled(true);
            }
        }
    }
}
