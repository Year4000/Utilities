/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router.pipline;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
        ErrorReporter.builder(cause)
            .add("Router: ", router)
            .add("Content Types: ", router.contentTypes(message.endPoint()).toArray())
            .add("Message: ", message)
            .buildAndReport(System.err);
        ctx.channel().close();
    }
}
