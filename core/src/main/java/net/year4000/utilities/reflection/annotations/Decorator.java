/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Decorator {
    /** The method name of a declaring proxied class to be observed */
    String value() default "";

    /** Read it by the signature, this one is special as interfaces can not have multiple methods with the same signature */
    String signature() default "";
}
