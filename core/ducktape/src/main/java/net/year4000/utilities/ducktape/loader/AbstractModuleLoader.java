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

package net.year4000.utilities.ducktape.loader;

import net.year4000.utilities.ducktape.ModuleLoader;
import net.year4000.utilities.ducktape.ModuleManager;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractModuleLoader<T extends AbstractModuleLoader> implements ModuleLoader {
    /** The module manager to load the classes into */
    protected final ModuleManager manager;

    /** The classes that were added in this loader's instance */
    protected final Set<Class<?>> classes = new HashSet<>();

    public AbstractModuleLoader(ModuleManager manager) {
        this.manager = manager;
    }

    /** Add the class to ModuleManager */
    @SuppressWarnings("unchecked")
    public T add(Class<?> clazz) {
        classes.add(clazz);
        return (T) this;
    }

    /** Remove the class from ModuleManager */
    @SuppressWarnings("unchecked")
    public T remove(Class<?> clazz) {
        classes.remove(clazz);
        return (T) this;
    }

    /** Format the path */
    public String formatPath(String path) {
        if (path.length() < 6) return path;
        return path.substring(0, path.length() - 6).replaceAll("/", ".");
    }
}
