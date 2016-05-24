/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk.routes;

import com.google.gson.Gson;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Route<T> {
    private static final Gson GSON = new Gson();
    protected T response;

    /** Generate the route based on its correct type */
    public static <R extends Route, T> R generate(Class<R> route, T response) {
        try {
            // Give us access to the default constructor
            Constructor<R> constructor = route.getDeclaredConstructor();
            constructor.setAccessible(true);
            R reply = constructor.newInstance();
            reply.response = response;
            return reply;
        }
        catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /** Get the raw response from the route */
    public T getRawResponse() {
        return response;
    }

    @Override
    public String toString() {
        return GSON.toJson(response);
    }
}
