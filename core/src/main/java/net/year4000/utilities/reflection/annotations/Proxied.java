package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Proxied {
    /** The class this object represents a reflection of */
    String value();

    /** When fetching the class by the name should the class be inited */
    boolean init() default false;
}
