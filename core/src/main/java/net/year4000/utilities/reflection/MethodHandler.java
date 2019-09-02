/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.reflection;

/** A interface to cache the method and avoid reflection look up calls */
@FunctionalInterface
public interface MethodHandler {
    /** Handle the method args */
    Object handle(Object... args) throws Throwable;
}
