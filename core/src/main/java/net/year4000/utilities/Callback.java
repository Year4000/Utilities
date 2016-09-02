/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

import java.util.Optional;

@FunctionalInterface
public interface Callback<T> {
    /** The class back method that is exposed to the user */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void callback(Optional<T> data, Optional<Throwable> error);

    /** Provide a wrapper for the callback values */
    default void callback(T data, Throwable error) {
        callback(Optional.ofNullable(data), Optional.ofNullable(error));
    }

    /** Only wrap the data and use empty for the error */
    default void callback(T data) {
        callback(Optional.of(data), Optional.empty());
    }

    /** Only wrap the error and use empty for the data */
    default void callback(Throwable error) {
        callback(Optional.empty(), Optional.of(error));
    }
}
