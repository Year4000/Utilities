package net.year4000.utilities;

public interface Callback<T> {
    /** A simple call back interface */
    public void callback(T data, Throwable error);
}
