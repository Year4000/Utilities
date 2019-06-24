/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.Sets;
import net.year4000.utilities.ducktape.loaders.GroovyModuleLoader;
import net.year4000.utilities.ducktape.loaders.ModuleLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

/** @Deprecated see DucktapeManager for future module stuff */
@Deprecated
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
