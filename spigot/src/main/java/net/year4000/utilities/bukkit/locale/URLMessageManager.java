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

package net.year4000.utilities.bukkit.locale;

import net.year4000.utilities.bukkit.Utilities;
import net.year4000.utilities.cache.QuickCache;
import net.year4000.utilities.locale.URLLocaleManager;

class URLMessageManager extends URLLocaleManager {
    private static final String LOCALE_URL = "https://raw.githubusercontent.com/Year4000/Locales/master/net/year4000/utilities/locales/";
    private static QuickCache<URLMessageManager> inst = QuickCache.builder(URLMessageManager.class).build();

    public URLMessageManager() {
        super(Utilities.getInst().getLog(), LOCALE_URL, parseJson(LOCALE_URL + LOCALES_JSON));
    }

    static URLMessageManager get() {
        return inst.get();
    }
}
