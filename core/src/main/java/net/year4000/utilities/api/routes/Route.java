package net.year4000.utilities.api.routes;

import com.google.gson.Gson;

public abstract class Route<T> {
    private static final Gson GSON = new Gson();
    protected T response;

    /** Generate the route based on its correct type */
    public static <R extends Route, T> R generate(Class<R> route, T response) {
        try {
            R reply = route.newInstance();
            reply.response = response;
            return reply;
        }
        catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return GSON.toJson(response);
    }
}
