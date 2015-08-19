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

package net.year4000.ducktape.bungee.module;

import net.year4000.ducktape.bungee.DuckTape;
import net.year4000.ducktape.bungee.Settings;
import net.year4000.ducktape.module.AbstractModule;
import net.year4000.utilities.LogUtil;

import java.io.File;

public class BungeeModule extends AbstractModule {
    @Override
    public File getDataFolder() {
        return new File(Settings.get().getModulesFolder(), getModuleInfo().name());
    }

    @Override
    public void registerCommand(Class<?> clazz) {
        DuckTape.get().registerCommand(clazz);
    }

    public static LogUtil getLog() {
        return DuckTape.get().getLog();
    }

    public static void log(String message, Object... args) {
        DuckTape.log(message, args);
    }

    public static void debug(String message, Object... args) {
        DuckTape.debug(message, args);
    }

    public static void debug(Exception e, boolean simple) {
        DuckTape.debug(e, simple);
    }

    public static void log(Exception e, boolean simple) {
        DuckTape.log(e, simple);
    }
}
