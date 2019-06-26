/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module.internal;

/** This is the base interface that the module will inherit */
public interface ModuleHandle {

    /** When this is invoked it will return the instance that the proxy has */
    Object $this();
}
