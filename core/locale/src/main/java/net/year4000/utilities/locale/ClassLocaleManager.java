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

package net.year4000.utilities.locale;

import net.year4000.utilities.LogUtil;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unused")
public class ClassLocaleManager extends AbstractLocaleManager {
    private Class clazz;

    /**
     * Load the manager using this class as a reference point.
     *
     * @param path    The path to use.
     * @param locales The locale codes to use.
     */
    public ClassLocaleManager(String path, String... locales) {
        this(ClassLocaleManager.class, path, locales);
    }

    /**
     * Load the manager with resources inside the class.
     *
     * @param clazz   The class to use as a reference.
     * @param path    The path to use.
     * @param locales The locale codes to use.
     */
    public ClassLocaleManager(Class clazz, String path, String... locales) {
        super(path, locales);
        this.clazz = checkNotNull(clazz);
    }

    /**
     * Load the class files and provide a LogUtil
     *
     * @param log     The LogUtil to use when creating the locale manager.
     * @param path    The path to use.
     * @param locales The locale codes to use.
     */
    public ClassLocaleManager(LogUtil log, String path, String... locales) {
        super(path, locales, log);
    }

    /** Load all the locales that are in the folder */
    @Override
    public void loadLocales() {
        try {
            for (String locale : codes) {
                loadLocale(locale, clazz.getResourceAsStream(path + locale + EXTENSION));
            }
        }
        catch (NullPointerException e) {
            log.log(e, true);
        }
    }
}
