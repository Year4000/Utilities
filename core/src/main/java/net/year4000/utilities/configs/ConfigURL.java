package net.year4000.utilities.configs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ConfigURL {
    String value();
    Class config();
    int expire() default 1;
    TimeUnit unit() default TimeUnit.HOURS;
}
