/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.value.TypeValue;

import java.util.Map;

public class RoutedPath<T> {
    private final RoutingManager.Path path;
    private final Handle<T> handle;

    RoutedPath(RoutingManager.Path path, Handle<T> handle) {
        this.path = Conditions.nonNull(path, "path");
        this.handle = Conditions.nonNull(handle, "handle");
    }

    /** Get the end point of the routed path */
    public String getEndPoint() {
        return path.endPoint;
    }

    /** Get the method of the routed path */
    public String getMethod() {
        return path.method;
    }

    /** Get the content type of the routed path */
    public Class<?> getContentType() {
        return path.contentType;
    }

    /** Handle the handle and catch any exceptions that occur and wrap it with RoutHandleException */
    public T handle(HttpRequest request, HttpResponse response, Map<String, TypeValue> query, TypeValue... args) {
        Conditions.nonNull(request, "request");
        Conditions.nonNull(response, "response");
        Conditions.nonNull(query, "query");
        return _handle(request, response, query, args);
    }

    /** Used for unit tests, ignores the request and response checking */
    T _handle(HttpRequest request, HttpResponse response, Map<String, TypeValue> query, TypeValue... args) {
        try {
            return handle.handle(request, response, query, args);
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
