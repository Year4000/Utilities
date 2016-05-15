/*
 * Copyright 2016 Year4000.
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

package net.year4000.utilities.ducktape;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.Sets;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class ModuleManager {
    /** The set of current loaded modules */
    private Set<Class<? extends ModuleLoader>> loaded = Sets.newLinkedHashSet();

    /** A map of all loaders stored by their class, should not be final as support for adding custom loaders */
    private ClassToInstanceMap<ModuleLoader> loaders = ImmutableClassToInstanceMap.<ModuleLoader>builder()
        .put(GroovyModuleLoader.class, new GroovyModuleLoader())
        .build();

    /** Load all classes from the selected path */
    public void loadAll(Path path, Consumer<Collection<Class<?>>> consumer) {
        loaders.forEach((key, value) -> {
            try {
                if (loaded.contains(key)) {
                    throw new IllegalStateException("Can not load the same loader twice.");
                }
                consumer.accept(value.load(path));
                loaded.add(key);
            } catch (IOException | IllegalStateException error) {
                // todo
                System.err.println(error.getMessage());
            }
        });
    }

    /** Format the path */
    public static String formatPath(String path) {
        if (path.length() < 6) return path;
        return path.substring(0, path.length() - 6).replaceAll("/", ".");
    }
}
