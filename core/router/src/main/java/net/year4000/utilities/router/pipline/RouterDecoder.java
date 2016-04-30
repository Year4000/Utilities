package net.year4000.utilities.router.pipline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.router.RoutedPath;
import net.year4000.utilities.router.Router;
import net.year4000.utilities.router.http.Message;
import net.year4000.utilities.tuple.Pair;
import net.year4000.utilities.value.Value;

import java.util.List;

public class RouterDecoder extends MessageToMessageDecoder<HttpRequest> {
    public static final String NAME = "router_decoder";

    @Override
    @SuppressWarnings("unchecked")
    protected void decode(ChannelHandlerContext ctx, HttpRequest request, List<Object> out) throws Exception {
        Router router = ctx.attr(Router.ATTRIBUTE_KEY).get();
        Message message = new Message(request);
        ctx.attr(Message.ATTRIBUTE_KEY).set(message);
        Pair<String, Class<? extends ChannelHandler>> pair = ContentDecoders.decoder(message);
        ChannelHandler decoder = Reflections.instance(pair.b.get()).get();
        ctx.pipeline().addAfter(NAME, ContentDecoders.NAME, decoder);
        Value<RoutedPath<Object>> path = router.findPath(message.endPoint(), request.method().toString(), null);

        // Route found display the contents
        if (path.isPresent()) {
            out.add(path.get().handle(message.getRequest(), message.getResponse(), message.arguments()));
            //out.add(value.get().handle(null,null));
        }

        // Not found display the error
    }
}
