package net.year4000.utilities.net.router.pipline;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class ExceptionHandler extends ChannelDuplexHandler {
    public static final String NAME = "exception_handler";

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
