/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router.pipline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.year4000.utilities.JsonBuilder;
import net.year4000.utilities.net.router.RoutedPath;
import net.year4000.utilities.net.router.Router;
import net.year4000.utilities.net.router.http.Message;
import net.year4000.utilities.tuple.Triad;
import net.year4000.utilities.value.Value;

import java.util.List;

@ChannelHandler.Sharable
public class RouterDecoder extends MessageToMessageDecoder<HttpRequest> {
    public static final String NAME = "router_decoder";
    public static final RouterDecoder INSTANCE = new RouterDecoder();

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, HttpRequest request, List<Object> out) throws Exception {
        Router router = ctx.channel().attr(Router.ATTRIBUTE_KEY).get();
        Message message = new Message(request);
        ctx.channel().attr(Message.ATTRIBUTE_KEY).set(message);
        Triad<Class<?>, String, ChannelHandler> triad = router.decoder(message);
        ctx.pipeline().addAfter(NAME, "content_decoder", triad.c.get());
        Value<RoutedPath<Object>> path = router.findPath(message.endPoint(), String.valueOf(request.method()), (Class<Object>) triad.a.get());
        // Route found display the contents
        if (path.isPresent()) {
            out.add(path.get().handle(message.getRequest(), message.getResponse(), message.arguments()));
        } else {
            message.setResponse(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
            out.add(JsonBuilder.newObject()
                .addProperty("code", 404)
                .addProperty("msg", "Route not found")
                .toJsonObject());
        }
    }
}
