/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape.module;

import com.google.inject.ScopeAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Recognise that the class files are modules that can do things */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ScopeAnnotation
public @interface Module {
    /** The id of the module plugin */
    String id();

    /** The public facing name of the module plugin, default will convert the id to Upper SnakeCase */
    String name() default "#ID#";

    /** The version of the module */
    String version() default "0.0.1";

    /** The description of the module plugin */
    String description() default "";

    /** Adds additional injector modules */
    Class<? extends com.google.inject.Module>[] injectors() default {};
}
