package net.year4000.utilities.router;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EndPoint {
    /** The method that will be used to route requests */
    String method();

    /** The class object that will be used as the return type for the handle */
    Class<?> contentType();
}

