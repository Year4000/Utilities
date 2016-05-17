/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.year4000.utilities.value.TypeValue;

import java.io.IOException;

@FunctionalInterface
public interface Handle<T> {
    /** Handle the content type from the client */
    T handle(HttpRequest request, HttpResponse response, TypeValue... args) throws RouteHandleException, IOException;
}
