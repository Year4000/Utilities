/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This will require proxy interface to have the proxy instance implements the following classes.
 * The proxied annotation takes in a new context in here, instead of being used for to map to a
 * class, it will map to an interface that the proxied class must implements.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Implements {
    /** A series of interfaces that are not known at compile time but are available at run time */
    Proxied[] value();
}
