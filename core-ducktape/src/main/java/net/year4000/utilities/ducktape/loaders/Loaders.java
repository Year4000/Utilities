/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.utils.UtilityConstructError;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class Loaders {
    private Loaders() {
        UtilityConstructError.raise();
    }

    /** Format the path */
    static String formatPath(String path) {
        return path.length() < 6 ? path : path.substring(0, path.length() - 6).replaceAll("/", ".");
    }

    /** Create the class loader from the path */
    static ClassLoader classLoaderFromPath(Path directory) throws IOException {
        URL[] urls = new URL[] {directory.toUri().toURL()};
        return AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
            try (URLClassLoader classLoader = new URLClassLoader(urls)) {
                return classLoader;
            } catch (IOException error) {
                throw ErrorReporter.builder(error).buildAndReport();
            }
        });
    }
}
