package net.year4000.utilities.reflection.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Getter {
    /** Invoke the proxy getter, when a value is not supplied use the method name */
    String value() default "";

    /** When value is an empty string try to use the signature, only the first value will be used */
    Signature[] signature() default {};
}
