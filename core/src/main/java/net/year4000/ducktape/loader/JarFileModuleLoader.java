package net.year4000.ducktape.loader;

import net.year4000.ducktape.module.ModuleManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.google.common.base.Preconditions.checkNotNull;

public class JarFileModuleLoader extends AbstractModuleLoader<JarFileModuleLoader> {
    public JarFileModuleLoader(ModuleManager manager) {
        super(manager);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JarFileModuleLoader add(Class<?> clazz) {
        classes.add(clazz);
        manager.add(clazz);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JarFileModuleLoader remove(Class<?> clazz) {
        classes.remove(clazz);
        manager.remove(clazz);
        return this;
    }

    /** Load each class that is in side of the jar */
    @SuppressWarnings("ConstantConditions")
    public void load(File jarDir) {
        for (final File file : checkNotNull(jarDir.listFiles())) {
            if (!file.getName().endsWith(".jar")) continue;

            JarFile jarFile;
            ClassLoader loader;

            try {
                jarFile = new JarFile(file);
                loader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {
                    public ClassLoader run() {
                        try {
                            ClassLoader classLoader = getClass().getClassLoader();
                            return new URLClassLoader(new URL[]{file.toURI().toURL()}, classLoader.getParent() == null ? classLoader : classLoader.getParent());
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (IOException e) {
                continue;
            }

            // And then the files in the jar
            for (Enumeration<JarEntry> en = jarFile.entries(); en.hasMoreElements(); ) {
                JarEntry next = en.nextElement();
                // Make sure it's a class
                if (!next.getName().endsWith(".class")) continue;

                Class<?> clazz = null;
                try {
                    clazz = Class.forName(formatPath(next.getName()), true, loader);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (!ModuleManager.isModuleClass(clazz)) continue;

                add(clazz);
            }
        }
    }
}
