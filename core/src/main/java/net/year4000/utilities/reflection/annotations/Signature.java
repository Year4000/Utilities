package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Used to grab the correct field or method based */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface Signature {
    /** The signature of the field or method */
    String value() default "";

    /** The index of the matching signature defaults to the first in the list, also supports negatives to go backwards */
    int index() default 0;
}
