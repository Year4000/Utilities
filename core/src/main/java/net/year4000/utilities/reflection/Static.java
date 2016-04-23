package net.year4000.utilities.reflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** This is to note when a method is proxying a static field or method */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Static {}
