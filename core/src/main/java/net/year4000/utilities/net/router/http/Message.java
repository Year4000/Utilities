/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router.http;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.value.TypeValue;
import net.year4000.utilities.value.Value;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class Message {
    public static final AttributeKey<Message> ATTRIBUTE_KEY = AttributeKey.valueOf("message");
    private HttpRequest request;
    private HttpResponse response;
    private String[] args;
    private Map<String, TypeValue> query = Maps.newHashMap();

    public Message(HttpRequest request) {
        this(request, new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK));
    }

    public Message(HttpRequest request, HttpResponse response) {
        setRequest(request);
        setResponse(response);
    }

    /** Set the request of message */
    public void setRequest(HttpRequest request) {
        this.request = Conditions.nonNull(request, "request");
        if (request.uri().indexOf("?") > 0) {
            String[] parts = request.uri().split("\\?");
            this.args = parts[0].split("/");
            if (parts.length > 1) { // if there are query parts after ?
                for (String part : parts[1].split("&")) { // seperate the key and value from the query
                    if (part.contains("=")) {
                        String[] keyValue = part.split("=");
                        query.put(keyValue[0], new TypeValue(keyValue[1]));
                    } else {
                        query.put(part, new TypeValue(Value.empty()));
                    }
                }
            }
        } else {
            this.args = request.uri().split("/");
        }
    }

    /** Get the request of the message */
    public HttpRequest getRequest() {
        return request;
    }

    /** Set the response of the message */
    public void setResponse(HttpResponse response) {
        this.response = Conditions.nonNull(response, "response");
    }

    /** Get the response of the message */
    public HttpResponse getResponse() {
        return response;
    }

    /** Try and get the mime type */
    public Value<String> mime() {
        return Value.of(getRequest().headers().get(HttpHeaders.CONTENT_TYPE));
    }

    /** Get the endpoint of the route */
    public String endPoint() {
        return (args.length < 2) ? "" : args[1];
    }

    /** Get the arguments of the url */
    public TypeValue[] arguments() {
        return (args.length < 2) ? new TypeValue[0] : Stream.of(Arrays.copyOfRange(args, 1, args.length)).map(TypeValue::new).toArray(TypeValue[]::new);
    }

    /** Create the full response from the content of the byte buffer */
    public FullHttpResponse makeResponse(ByteBuf buffer) {
        Conditions.nonNull(buffer, "buffer").retain();
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(this.response.protocolVersion(), this.response.status(), buffer);
        response.headers()
            .setAll(this.response.headers()) // copy from response
            .add(HttpHeaders.CONTENT_TYPE, mime().get())
            .add(HttpHeaders.CONTENT_LENGTH, buffer.readableBytes());
        return response;
    }

    /** Does the query contain the key value */
    public boolean containsQuery(String key) {
        Conditions.nonNullOrEmpty(key, "key");
        return query.get(key) != null;
    }

    /** Return the TypeValue of the query key */
    public TypeValue query(String key) {
        Conditions.nonNullOrEmpty(key, "key");
        return Conditions.nonNull(query.get(key), "query.get(key)");
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
        return Utils.toString(this);
    }
}
