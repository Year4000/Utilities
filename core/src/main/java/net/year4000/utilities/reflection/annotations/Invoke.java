/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Invoke {
    /** Invoke the proxy method, when a value is not supplied use the method name */
    String value() default "";

    /** Read it by the signature, if multiple are present it will try to use value */
    String signature() default "";

    /** The index to grab from the SignatureLookup */
    int index() default 0;

    /** Should this proxy use the method handles implementation or just use reflection by default */
    boolean methodHandle() default false;
}
