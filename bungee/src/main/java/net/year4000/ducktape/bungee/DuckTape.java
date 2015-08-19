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

package net.year4000.ducktape.bungee;

import lombok.Getter;
import net.year4000.ducktape.bungee.module.BungeeModule;
import net.year4000.ducktape.bungee.module.ModuleManagerListener;
import net.year4000.ducktape.loader.ClassFolderModuleLoader;
import net.year4000.ducktape.loader.ClassModuleLoader;
import net.year4000.ducktape.loader.GroovyModuleLoader;
import net.year4000.ducktape.loader.JarFileModuleLoader;
import net.year4000.ducktape.module.ModuleManager;
import net.year4000.utilities.bungee.BungeePlugin;

public class DuckTape extends BungeePlugin {
    private static DuckTape inst;
    @Getter
    private ModuleManager<BungeeModule> modules = new ModuleManager<>(getLog());

    @Override
    public void onLoad() {
        inst = this;

        modules.getEventBus().register(new ModuleManagerListener());

        // register internals
        try {
            new ClassModuleLoader(modules).add(DuckTapeModule.class);
        }
        catch (Exception error) {
            log(error, false);
        }

        // register jar files
        try {
            new JarFileModuleLoader(modules).load(Settings.get().getModulesFolderPath());
        }
        catch (Exception error) {
            log(error, false);
        }

        // register class files
        try {
            new ClassFolderModuleLoader(modules).load(Settings.get().getModulesFolderPath());
        }
        catch (Exception error) {
            log(error, false);
        }

        // register groovy script files
        try {
            new GroovyModuleLoader(modules).load(Settings.get().getModulesFolderPath());
        }
        catch (Exception error) {
            log(error, false);
        }

        // load modules
        modules.loadModules();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        // enable modules
        modules.enableModules();
    }

    @Override
    public void onDisable() {
        // disable modules
        modules.disableModules();
    }

    public static DuckTape get() {
        return inst;
    }

}
