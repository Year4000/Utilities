/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape.settings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectSettings {
    /** This value will be given too the settings provider to know where or how to save and load the settings */
    String value() default "";
}
