/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.loaders;

import net.year4000.utilities.ducktape.ModuleInitException;

import java.util.Collection;

/**
 * This is a functional interface that will load
 * a module from the select file path.
 */
@FunctionalInterface
public interface ModuleLoader {

    /** Load classes from where ever and create the collection of classes for the modules */
    Collection<Class<?>> load() throws ModuleInitException;
}
