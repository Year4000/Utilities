package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Setter {
    /** Invoke the proxy setter, when a value is not supplied use the method name */
    String value() default "";

    /** Read it by the signature, if multiple are present it will try to use value */
    String signature() default "";
}
