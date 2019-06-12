/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router.pipline;

import com.google.common.net.HttpHeaders;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.net.router.Router;
import net.year4000.utilities.net.router.http.Message;

@ChannelHandler.Sharable
public class ExceptionHandler extends ChannelDuplexHandler {
    public static final String NAME = "exception_handler";
    public static final ExceptionHandler INSTANCE = new ExceptionHandler();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Router router = ctx.channel().attr(Router.ATTRIBUTE_KEY).get();
        Message message = ctx.channel().attr(Message.ATTRIBUTE_KEY).get();
        String error = ErrorReporter.builder(cause)
            .add("Router: ", router)
            .add("Content Types: ", router.contentTypes(message.endPoint()).toArray())
            .add("Message: ", message)
            .build().toString();
        System.err.println(error);
        message.getRequest().headers().set(HttpHeaders.CONTENT_TYPE, "text/plain");
        message.setResponse(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR));
        ctx.write(message.makeResponse(Unpooled.copiedBuffer(error.getBytes()))).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
