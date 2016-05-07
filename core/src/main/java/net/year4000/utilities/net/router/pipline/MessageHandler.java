package net.year4000.utilities.net.router.pipline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.year4000.utilities.net.router.http.Message;

@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<ByteBuf> {
    public static final String NAME = "message_handler";
    public static final MessageHandler INSTANCE = new MessageHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        Message message = ctx.channel().attr(Message.ATTRIBUTE_KEY).get();
        ctx.write(message.makeResponse(msg)).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
}
