/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import com.google.common.reflect.ClassPath;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ducktape.ModuleInitException;
import net.year4000.utilities.ducktape.module.ModuleInfo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** This module loader will search the classpath for classes that are annotated with an annotation. */
public class ClassPathModuleLoader implements ModuleLoader {
    private final ClassLoader classLoader;
    private final Class<? extends Annotation>[] annotations;
    private final String packageName;

    /** Load classes from the classpath from the packageName that are annotated with any of the annotations */
    public ClassPathModuleLoader(ClassLoader classLoader, String packageName, Class<? extends Annotation>... annotations) {
        this.classLoader = Conditions.nonNull(classLoader, "class load must exist");
        this.packageName = packageName;
        this.annotations = Conditions.nonNull(annotations, "must give a annotation to find classes");
    }

    /** Loads all classes and get the classes that are annotated with any of the annotations */
    public ClassPathModuleLoader(ClassLoader classLoader, Class<? extends Annotation>... annotations) {
        this(classLoader, null, annotations);
    }

    /** Loads all classes and get the classes that are annotated with any of the annotations */
    public ClassPathModuleLoader(String packageName, Class<? extends Annotation>... annotations) {
        this(ClassLoader.getSystemClassLoader(), packageName, annotations);
    }

    /** Loads all classes and get the classes that are annotated with any of the annotations */
    public ClassPathModuleLoader(Class<? extends Annotation>... annotations) {
        this(ClassLoader.getSystemClassLoader(), null, annotations);
    }

    @Override
    public Collection<Class<?>> load() throws ModuleInitException {
        try {
            ClassPath classPath = ClassPath.from(this.classLoader);
            Stream<ClassPath.ClassInfo> classInfoStream = this.packageName == null
                ? classPath.getTopLevelClasses().stream()
                : classPath.getTopLevelClassesRecursive(this.packageName).stream();
            return classInfoStream
                .map(classInfo -> {
                    try {
                        return classInfo.load();
                    } catch (Throwable error) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(clazz -> {
                    for (Class<? extends Annotation> annotation : this.annotations) {
                        if (clazz.isAnnotationPresent(annotation)) {
                            return true;
                        }
                    }
                    return false;
                })
                .collect(Collectors.toSet());
        } catch (IOException error) {
            throw new ModuleInitException(ModuleInfo.Phase.LOADING, error);
        }
    }
}
