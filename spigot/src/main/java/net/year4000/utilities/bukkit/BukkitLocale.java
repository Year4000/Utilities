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

import net.year4000.utilities.locale.AbstractLocaleManager;
import net.year4000.utilities.locale.AbstractStringTranslation;

import java.util.Locale;
import java.util.Optional;

import static net.year4000.utilities.locale.AbstractLocaleManager.DEFAULT_LOCALE;

@SuppressWarnings("unused")
public class BukkitLocale extends AbstractStringTranslation {
    /** Start creating locales for the specific player's locale */
    public BukkitLocale(AbstractLocaleManager localeManager, Optional<Locale> locale) {
        super(localeManager, locale.orElse(DEFAULT_LOCALE).toString());
    }

    /** Translate to the specific locale with formatting */
    public String get(String key, Object... args) {
        return MessageUtil.replaceColors(super.get(key, args));
    }
}
