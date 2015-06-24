package net.year4000.utilities;

public interface Callback<T> {
    /** A simple call back interface */
    void callback(T data, Throwable error);

    /** A simple call back interface */
    default void callback(T data) {
        callback(data, null);
    }
}
