package net.year4000.utilities.router.pipline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.year4000.utilities.router.http.Message;

public class MessageHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final String NAME = "message_handler";

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        System.err.println(NAME);
        Message message = ctx.attr(Message.ATTRIBUTE_KEY).get();
        ctx.write(message.makeResponse(msg)).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
