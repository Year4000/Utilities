/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit.items;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Lists;
import net.year4000.utilities.bukkit.MessageUtil;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NBTBuilder {
    private ItemStack item;

    private NBTBuilder(ItemStack item) {
        this.item = checkNotNull(item);
    }

    /** Create a new instance of NBT builder for the item */
    public static NBTBuilder of(ItemStack item) {
        return new NBTBuilder(item);
    }

    /** Set the display name for the item */
    public NBTBuilder setDisplayName(String string) {
        handleMeta(meta -> {
            String display = MessageUtil.replaceColors(string);
            meta.setDisplayName(display);
        });

        return this;
    }

    /** Set the lore for the item */
    public NBTBuilder setLore(String... strings) {
        handleMeta(meta -> {
            List<String> display = Lists.newArrayList();

            for (String string : strings) {
                display.add(MessageUtil.replaceColors(string));
            }

            meta.setLore(display);
        });

        return this;
    }

    /** Set the lore for the item */
    public NBTBuilder setLore(List<String> strings) {
        handleMeta(meta -> {
            List<String> display = Lists.newArrayList();
            display.addAll(strings.stream().map(MessageUtil::replaceColors).collect(Collectors.toList()));
            meta.setLore(display);
        });

        return this;
    }

    /** Force add the enchantment to the item */
    public NBTBuilder addEnchantment(Enchantment enchantment, int level) {
        handleMeta(meta -> meta.addEnchant(enchantment, level, true));
        return this;
    }

    /** Remove the enchantment to the item */
    public NBTBuilder removeEnchantment(Enchantment enchantment) {
        handleMeta(meta -> meta.removeEnchant(enchantment));
        return this;
    }

    /** Remove all enchantments of the items */
    public NBTBuilder removeEnchantments() {
        handleMeta(meta -> meta.getEnchants().keySet().forEach(meta::removeEnchant));
        return this;
    }

    /** Add item flags to the item */
    public NBTBuilder addItemFlag(ItemFlag... flags) {
        handleMeta(meta -> meta.addItemFlags(flags));
        return this;
    }

    /** Remove item flags to the item */
    public NBTBuilder removeItemFlag(ItemFlag... flags) {
        handleMeta(meta -> meta.removeItemFlags(flags));
        return this;
    }

    /** Make the item unbreakable */
    public NBTBuilder setUnbreakable() {
        handleMeta(meta -> meta.spigot().setUnbreakable(true));
        return this;
    }

    /** Make the item breakable */
    public NBTBuilder unsetUnbreakable() {
        handleMeta(meta -> meta.spigot().setUnbreakable(false));
        return this;
    }

    /** Add a glow to the item */
    public NBTBuilder addGlow() {
        handleMeta(meta -> {
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });

        return this;
    }

    /** Remove a glow to the item */
    public NBTBuilder removeGlow() {
        handleMeta(meta -> {
            meta.removeEnchant(Enchantment.LURE);
            meta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        });

        return this;
    }

    /** Handle the meta of the item, must be synchronized */
    private synchronized void handleMeta(Consumer<ItemMeta> handler) {
        ItemMeta meta = item.getItemMeta();
        handler.accept(meta);
        item.setItemMeta(meta);
    }
}
