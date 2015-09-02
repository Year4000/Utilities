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

package net.year4000.utilities.bukkit;

import net.year4000.utilities.bukkit.items.NBT;
import com.google.gson.Gson;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.material.Colorable;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@SuppressWarnings("unused")
public final class ItemUtil {
    private static final Gson gson = new Gson();

    private ItemUtil() {}

    /**
     * Create a book with the data inside
     * @param title The title.
     * @param author The author.
     * @param pages The pages' content.
     * @return The book
     */
    public static ItemStack createBook(String title, String author, List<String> pages) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();

        meta.setTitle(MessageUtil.replaceColors(title));
        meta.setAuthor(MessageUtil.replaceColors(author));
        pages.forEach(MessageUtil::replaceColors);
        meta.setPages(pages);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Set the color of a item
     * @param item The item
     * @param color The color
     * @return Item with color
     * @throws InputMismatchException
     */
    public static ItemStack setColor(ItemStack item, DyeColor color) throws InputMismatchException {
        // Check is the item can have a color.
        if (!(item instanceof Colorable)) {
            throw new InputMismatchException("The item can not have a color.");
        }

        ((Colorable) item).setColor(color);
        return item;
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @param amount The amount of items.
     * @param damage The damage of the item.
     * @return ItemStack
     */
    public static ItemStack makeItem(String item, int amount, short damage) {
        return new ItemStack(Material.valueOf(item.toUpperCase()), amount, damage);
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @param amount The amount of items.
     * @return ItemStack
     */
    public static ItemStack makeItem(String item, int amount) {
        return makeItem(item.toUpperCase(), amount, (short) 0);
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @return ItemStack
     */
    public static ItemStack makeItem(String item) {
        return makeItem(item.toUpperCase(), 1);
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @param amount The amount of items.
     * @param damage The damage of the item.
     * @return ItemStack
     */
    public static ItemStack makeItem(Material item, int amount, short damage) {
        return new ItemStack(item, amount, damage);
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @param amount The amount of items.
     * @return ItemStack
     */
    public static ItemStack makeItem(Material item, int amount) {
        return makeItem(item, amount, (short) 0);
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @return ItemStack
     */
    public static ItemStack makeItem(Material item) {
        return makeItem(item, 1);
    }

    /**
     * Create a simple item.
     * @param item The name of the item.
     * @param nbt The nbt data of the item.
     * @return ItemStack
     */
    public static ItemStack makeItem(String item, String nbt) {
        ItemStack itemStack = makeItem(item.toUpperCase());
        itemStack.setItemMeta(addMeta(itemStack, nbt));
        return itemStack;
    }

    /**
     * Add ItemMeta to an item.
     * @param itemStack The item to use as a reference point.
     * @param nbtJson The json string to make the item.
     * @return The ItemMeta.
     */
    public static ItemMeta addMeta(ItemStack itemStack, String nbtJson) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        final NBT nbt = gson.fromJson(nbtJson, NBT.class);

        // Set the item's display
        if (nbt.getDisplay() != null) {
            if (nbt.getDisplay().getName() != null) {
                itemMeta.setDisplayName(MessageUtil.replaceColors(nbt.getDisplay().getName()));
            }

            // Set the item's lore
            if (nbt.getDisplay().getLore() != null) {
                itemMeta.setLore(new ArrayList<String>() {{
                    for (String line : nbt.getDisplay().getLore()) {
                        add(MessageUtil.replaceColors(line));
                    }
                }});
            }

            // Set the item's color if leather armor.
            if (nbt.getDisplay().getColor() != null) {
                if (itemMeta instanceof LeatherArmorMeta) {
                    ChatColor chatColor = ChatColor.valueOf(nbt.getDisplay().getColor().toUpperCase());
                    Color color = BukkitUtil.dyeColorToColor(BukkitUtil.chatColorToDyeColor(chatColor));
                    ((LeatherArmorMeta) itemMeta).setColor(color);
                }
            }
        }

        // Set the book's title author, and pages if its a book.
        if (nbt.getAuthor() != null && nbt.getTitle() != null && nbt.getPages() != null) {
            if (itemMeta instanceof BookMeta) {
                ((BookMeta) itemMeta).setAuthor(MessageUtil.replaceColors(nbt.getAuthor()));
                ((BookMeta) itemMeta).setTitle(MessageUtil.replaceColors(nbt.getTitle()));
                ((BookMeta) itemMeta).setPages(new ArrayList<String>() {{
                    for (String line : nbt.getPages()) {
                        add(MessageUtil.replaceColors(line));
                    }
                }});
            }
        }

        // Set the item's enchantment
        if (nbt.getEnchantments() != null) {
            for (NBT.Enchantments enchantment: nbt.getEnchantments()) {
                // The true is forcing the item to have enchantments even if the items can't have it.
                itemMeta.addEnchant(
                    Enchantment.getByName(enchantment.getName().toUpperCase()),
                    enchantment.getLevel(),
                    true
                );
            }
        }

        // Set the item's unbreakable tag
        itemMeta.spigot().setUnbreakable(nbt.isUnbreakable());

        // Set hide flags
        if (nbt.getHideFlags().length > 0) {
            for (String id : nbt.getHideFlags()) {
                itemMeta.addItemFlags(ItemFlag.valueOf(id.toUpperCase()));
            }
        }

        return itemMeta;
    }
}
