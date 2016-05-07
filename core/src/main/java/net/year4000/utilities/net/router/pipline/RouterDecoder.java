package net.year4000.utilities.net.router.pipline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;
import net.year4000.utilities.net.router.RoutedPath;
import net.year4000.utilities.net.router.Router;
import net.year4000.utilities.net.router.http.Message;
import net.year4000.utilities.tuple.Triad;
import net.year4000.utilities.value.Value;

import java.util.List;

public class RouterDecoder extends MessageToMessageDecoder<HttpRequest> {
    public static final String NAME = "router_decoder";

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, HttpRequest request, List<Object> out) throws Exception {
        System.err.println(NAME);
        Router router = ctx.channel().attr(Router.ATTRIBUTE_KEY).get();
        Message message = new Message(request);
        ctx.channel().attr(Message.ATTRIBUTE_KEY).set(message);
        Triad<Class<?>, String, ChannelHandler> triad = ContentDecoders.decoder(message);
        ctx.pipeline().addAfter(NAME, ContentDecoders.NAME, triad.c.get());
        System.out.println(request);
        Value<RoutedPath<Object>> path = router.findPath(
                message.endPoint(),
                String.valueOf(request.getMethod()),
                (Class<Object>) triad.a.get());

        // Route found display the contents
        if (path.isPresent()) {
            out.add(path.get().handle(message.getRequest(), message.getResponse(), message.arguments()));
            //out.add(value.get().handle(null,null));
        }

        // Not found display the error
        throw new RuntimeException("Error");
    }
}
