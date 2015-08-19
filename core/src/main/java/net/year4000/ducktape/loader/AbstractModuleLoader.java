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

import lombok.Getter;
import net.year4000.ducktape.module.ModuleManager;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractModuleLoader<T extends AbstractModuleLoader> {
    /** The module manager to load the classes into */
    protected final ModuleManager manager;

    /** The classes that were added in this loader's instance */
    @Getter
    protected final Set<Class<?>> classes = new HashSet<>();

    public AbstractModuleLoader(ModuleManager manager) {
        this.manager = manager;
    }

    /** Add the class to ModuleManager */
    public abstract T add(Class<?> clazz);

    /** Remove the class from ModuleManager */
    public abstract T remove(Class<?> clazz);

    /** Format the path */
    public String formatPath(String path) {
        if (path.length() < 6) return path;
        return path.substring(0, path.length() - 6).replaceAll("/", ".");
    }
}
