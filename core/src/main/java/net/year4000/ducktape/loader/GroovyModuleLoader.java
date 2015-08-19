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

package net.year4000.ducktape.loader;

import groovy.lang.GroovyClassLoader;
import net.year4000.ducktape.module.ModuleManager;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class GroovyModuleLoader extends AbstractModuleLoader<GroovyModuleLoader> {
    public GroovyModuleLoader(ModuleManager manager) {
        super(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public GroovyModuleLoader add(Class<?> clazz) {
        classes.add(clazz);
        manager.add(clazz);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GroovyModuleLoader remove(Class<?> clazz) {
        classes.remove(clazz);
        manager.remove(clazz);
        return this;
    }

    /** Load any groovy script that ends with .groovy */
    public void load(File jarDir) {
        for (final File file : checkNotNull(jarDir.listFiles())) {
            if (!file.getName().endsWith(".groovy")) continue;

            try {
                GroovyClassLoader loader = new GroovyClassLoader();
                Class clazz = loader.parseClass(file);
                add(clazz);
            }
            catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}
