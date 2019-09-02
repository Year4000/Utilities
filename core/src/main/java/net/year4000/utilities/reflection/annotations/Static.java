/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This is to note when a method is proxying a static field or method */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Static {}
