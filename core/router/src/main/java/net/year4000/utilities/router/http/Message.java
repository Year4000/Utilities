package net.year4000.utilities.router.http;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.AttributeKey;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.value.TypeValue;

public class Message {
    public static final AttributeKey<Message> ATTRIBUTE_KEY = AttributeKey.newInstance("message");
    private HttpRequest request;
    private HttpResponse response;

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
    }

    /** Get the request of the message */
    public HttpRequest getRequest() {
        return request;
    }

    /** Set the responce of the message */
    public void setResponse(HttpResponse response) {
        this.response = Conditions.nonNull(response, "response");
    }

    /** Get the response of the message */
    public HttpResponse getResponse() {
        return response;
    }

    /** Get the endpoint of the route */
    public String endPoint() {
        return "";
    }

    /** Get the arguments of the url */
    public TypeValue[] arguments() {
        return null;
    }

    /** Create the full response from the content of the byte buffer */
    public FullHttpResponse makeResponse(ByteBuf buffer) {
        Conditions.nonNull(buffer, "buffer").retain();
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(this.response.protocolVersion(), this.response.status(), buffer);
        response.headers().addInt(HttpHeaderNames.CONTENT_LENGTH, buffer.readableBytes());
        return response;
    }
}
