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

package net.year4000.ducktape.bukkit;

import net.year4000.ducktape.GlobalSettings;
import net.year4000.utilities.config.InvalidConfigurationException;

import java.io.File;

public class Settings extends GlobalSettings {
    private static Settings inst = null;

    private Settings() {
        try {
            init(new File(DuckTape.get().getDataFolder(), "config.yml"));
        } catch (InvalidConfigurationException e) {
            DuckTape.debug(e, false);
        }
    }

    public static Settings get() {
        if (inst == null) {
            inst = new Settings();
        }

        return inst;
    }

    /** Get the data folder for the modules */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getModulesFolder() {
        File file = new File(DuckTape.get().getDataFolder(), getModulesData());

        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    /** Get the data folder for the modules */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File getModulesDataFolder() {
        File file = new File(DuckTape.get().getDataFolder(), getModulesPath());

        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }
}
