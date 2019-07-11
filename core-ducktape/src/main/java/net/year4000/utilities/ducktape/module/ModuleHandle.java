/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module;

/** This is the base interface that the module will inherit */
public interface ModuleHandle {

    /** This will return the class of the loaded module */
    Class<?> $class();

    /** When this is invoked it will return the instance that the proxy has */
    Object $this();
}
