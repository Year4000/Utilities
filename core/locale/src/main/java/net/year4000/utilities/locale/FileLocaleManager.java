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

import java.io.File;
import java.io.FileInputStream;

import static com.google.common.base.Preconditions.checkArgument;

@SuppressWarnings("unused")
public class FileLocaleManager extends AbstractLocaleManager {
    /**
     * Load the locales provided and use a File object as a base location.
     *
     * @param file    The file object to use as a base location.
     * @param locales The locales to use by default
     */
    public FileLocaleManager(File file, String... locales) {
        this(isFileDirectory(file).toPath().toString() + "/", locales);
    }

    /**
     * Load the locales provided and use a File object as a base location.
     *
     * @param path    The file path to use when getting the files.
     * @param locales The locales to use by default
     */
    public FileLocaleManager(String path, String... locales) {
        super(path, locales);
    }

    /**
     * Load the locales provided and use a File object as a base location.
     *
     * @param log     The LogUtil to use when creating this locale manager.
     * @param path    The file path to use when getting the files.
     * @param locales The locales to use by default.
     */
    public FileLocaleManager(LogUtil log, String path, String... locales) {
        super(path, locales, log);
    }

    /** Check is the argument is a file and an a directory, internal usage. */
    private static <T> T isFileDirectory(T file) {
        checkArgument(file instanceof File && ((File) file).isDirectory());
        return file;
    }

    /** Load all the locales that are in the folder */
    @Override
    public void loadLocales() {
        for (String code : codes) {
            try {
                File file = new File(path, code + EXTENSION);

                loadLocale(code, new FileInputStream(file));
            }
            catch (Exception e) {
                //log.log(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
