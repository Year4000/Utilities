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

import net.year4000.ducktape.module.ModuleManager;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClassFolderModuleLoader extends AbstractModuleLoader<ClassFolderModuleLoader> {
    public ClassFolderModuleLoader(ModuleManager manager) {
        super(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassFolderModuleLoader add(Class<?> clazz) {
        classes.add(clazz);
        manager.add(clazz);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ClassFolderModuleLoader remove(Class<?> clazz) {
        classes.remove(clazz);
        manager.remove(clazz);
        return this;
    }

    /** Load each class that is in side of a directory */
    public void load(File classDir) {
        URLClassLoader loader = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {
            public URLClassLoader run() {
                try {
                    return new URLClassLoader(new URL[]{classDir.toURI().toURL()}, getClass().getClassLoader());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        for (String clazzName : getClassNames(classDir)) {
            Class<?> clazz = null;
            try {
                clazz = loader.loadClass(clazzName);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (!ModuleManager.isModuleClass(clazz)) continue;

            add(clazz);
        }
    }

    /** Get all the class names */
    public Set<String> getClassNames(File classDir) {
        return recursiveGetClasses(classDir, "");
    }

    /** Get all the class names */
    @SuppressWarnings("ConstantConditions")
    public Set<String> recursiveGetClasses(File dir, String parentName) {
        Set<String> classNames = new HashSet<>();
        for (File file : checkNotNull(dir.listFiles())) {
            if (file.isDirectory()) {
                classNames.addAll(recursiveGetClasses(file, parentName + file.getName() + "."));
            } else if (file.getName().endsWith(".class")) {
                classNames.add(parentName + formatPath(file.getName()));
            }
        }

        return classNames;
    }
}
