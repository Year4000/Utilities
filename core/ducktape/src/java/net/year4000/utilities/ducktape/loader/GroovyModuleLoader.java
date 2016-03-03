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

import groovy.lang.GroovyClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;

import static com.google.common.base.Preconditions.checkNotNull;

public class GroovyModuleLoader extends AbstractModuleLoader<GroovyModuleLoader> {
    /*public GroovyModuleLoader(ModuleManager manager) {
        super(manager);
    }*/

    @SuppressWarnings("unchecked")
    @Override
    public GroovyModuleLoader add(Class<?> clazz) {
        classes.add(clazz);
        //manager.add(clazz);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public GroovyModuleLoader remove(Class<?> clazz) {
        classes.remove(clazz);
        //manager.remove(clazz);
        return this;
    }

    /** Load any groovy script that ends with .groovy */
    public void load(File jarDir) {
        for (final File file : checkNotNull(jarDir.listFiles())) {
            if (!file.getName().endsWith(".groovy")) continue;

            try {
                ClassLoader parent = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                    public ClassLoader run() {
                        try {
                            return new URLClassLoader(new URL[]{file.toURI().toURL()}, getClass().getClassLoader());
                        }
                        catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                GroovyClassLoader loader = new GroovyClassLoader(parent);
                loader.addURL(getClass().getResource("/"));
                loader.addURL(file.toURI().toURL());
                Class clazz = loader.parseClass(file);

                //if (!ModuleManager.isModuleClass(clazz)) continue;

                add(clazz);
            }
            catch (Exception error) {
                error.printStackTrace();
            }
        }
    }
}
