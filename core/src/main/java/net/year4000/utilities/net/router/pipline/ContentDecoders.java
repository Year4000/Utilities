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
    public static final String NAME = "content_decoders";
    public static final String DEFAULT = "text/plain";

    /** Maps the classes to the string mime type */
    private static BiMap<Class<?>, String> mimes = ImmutableBiMap.of(
            String.class, DEFAULT,
            JsonObject.class, "text/json"
    );
    /** Maps the string mime type to the encoder class */
    private static BiMap<String, Class<? extends MessageToMessageDecoder<?>>> decoders = ImmutableBiMap.of(
            DEFAULT, StringDecoder.class,
            "text/json", JsonObjectDecoder.class
    );

    /** Get the best decoder for the message type */
    public static Triad<Class<?>, String, Class<? extends ChannelHandler>> decoder(Message message) {
        return new Triad<>(String.class, "text/json", StringDecoder.class);
    }

    /** The decoder that decodes string to byte buf */
    public static class StringDecoder extends MessageToMessageDecoder<String> {
        @Override
        protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
            System.err.println(NAME);
            out.add(Unpooled.wrappedBuffer(msg.getBytes()));
        }
    }

    /** The decoder that decodes string to byte buf */
    public static class JsonObjectDecoder extends MessageToMessageDecoder<JsonObject> {
        @Override
        protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
            out.add(Unpooled.wrappedBuffer(msg.toString().getBytes()));
        }
    }
}
