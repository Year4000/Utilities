/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

@FunctionalInterface
public interface Builder<T> {
    /** A method that tells builders that they are a builder and must include {@link #build()} */
    T build();
}
