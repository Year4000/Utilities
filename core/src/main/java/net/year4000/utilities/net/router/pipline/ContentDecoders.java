package net.year4000.utilities.net.router.pipline;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.year4000.utilities.net.router.http.Message;
import net.year4000.utilities.tuple.Triad;

import java.util.List;

public class ContentDecoders {
    /** The name of the handler */
    public static final String NAME = "content_decoders";
    /** The decoder that decodes string to byte buf */
    public static final StaticDecoder<String> STRING_DECODER = new StaticDecoder<String>() {
        @Override
        protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.err.println(NAME);
            out.add(Unpooled.wrappedBuffer(msg.getBytes()));
        }
    };
    /** The decoder that decodes string to byte buf */
    public static final StaticDecoder<JsonObject> JSON_OBJECT_DECODER = new StaticDecoder<JsonObject>() {
        @Override
        protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
            System.err.println(NAME);
            out.add(Unpooled.wrappedBuffer(msg.toString().getBytes()));
        }
    };
    /** Maps the classes to the string mime type */
    private final static BiMap<Class<?>, String> mimes = ImmutableBiMap.of(
        String.class, "text/plain",
        JsonObject.class, "text/json"
    );
    /** Maps the string mime type to the encoder class */
    private final static BiMap<String, StaticDecoder<?>> decoders = ImmutableBiMap.of(
        "text/plain", STRING_DECODER,
        "text/json", JSON_OBJECT_DECODER
    );

    /** Get the best decoder for the message type */
    public static Triad<Class<?>, String, ChannelHandler> decoder(Message message) {
        return new Triad<>(String.class, "text/plain", STRING_DECODER);
    }

    /** Allow creating static decoders */
    @ChannelHandler.Sharable
    private static abstract class StaticDecoder<T> extends MessageToMessageDecoder<T> {}
}
