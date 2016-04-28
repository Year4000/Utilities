package net.year4000.utilities.router;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.value.TypeValue;

public class RoutedPath<T> {
    private final RoutingManager.Path path;
    private final Handle<T> handle;

    RoutedPath(RoutingManager.Path path, Handle<T> handle) {
        this.path = Conditions.nonNull(path, "path");
        this.handle = Conditions.nonNull(handle, "handle");
    }

    /** Handle the handle and catch any exceptions that occur and wrap it with RoutHandleException */
    public T handle(HttpRequest request, HttpResponse response, TypeValue... args) {
        Conditions.nonNull(request, "request");
        Conditions.nonNull(response, "response");
        return _handle(request, response, args);
    }

    /** Used for unit tests,  ignores the request and response */
    T _handle(HttpRequest request, HttpResponse response, TypeValue... args) {
        try {
            return handle.handle(request, response, args);
        } catch (RouteHandleException error) {
            throw error;
        } catch (Throwable throwable) {
            throw new RouteHandleException(throwable);
        }
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public String toString() {
        return Utils.toString(this, "path");
    }
}
