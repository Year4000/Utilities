/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection;

/** A interface to cache the method and avoid reflection look up calls */
@FunctionalInterface
interface MethodHandler {
    /** Handle the method args */
    Object handle(Object instance, Object[] args) throws Throwable;
}
