/*
 * Copyright 2020 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentType {
    /** The content type that will be used when sending data to the server */
    String value();
}
