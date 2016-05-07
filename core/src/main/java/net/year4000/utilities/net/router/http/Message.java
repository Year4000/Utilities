package net.year4000.utilities.net.router.http;

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
import java.util.stream.Stream;

public class Message {
    public static final AttributeKey<Message> ATTRIBUTE_KEY = AttributeKey.valueOf("message");
    private HttpRequest request;
    private HttpResponse response;
    private String[] args;

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
        this.args = request.getUri().split("/");
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
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(this.response.getProtocolVersion(), this.response.getStatus(), buffer);
        response.headers().add(HttpHeaders.CONTENT_LENGTH, buffer.readableBytes());
        return response;
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
