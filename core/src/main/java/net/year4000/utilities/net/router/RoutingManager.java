/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.net.router;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.gson.JsonObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.net.router.http.Message;
import net.year4000.utilities.net.router.pipline.StaticDecoder;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.tuple.Triad;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.util.List;
import java.util.SortedSet;

public class RoutingManager implements Router {
    private final ImmutableSortedMap<Path, RoutedPath<?>> paths;
    private final ImmutableMultimap<String, Class<?>> contentTypes;
    /** Maps the classes to the string mime type */
    private final BiMap<Class<?>, String> mimes;
    /** Maps the string mime type to the encoder class */
    private final BiMap<String, StaticDecoder<?>> decoders;

    private RoutingManager(ImmutableSortedMap<Path, RoutedPath<?>> paths, ImmutableMultimap<String, Class<?>> contentTypes, ImmutableList<Decoder<?>> decoders) {
        this.paths = Conditions.nonNull(paths, "paths");
        this.contentTypes = Conditions.nonNull(contentTypes, "contentTypes");
        Conditions.nonNull(decoders, "decoders");
        ImmutableBiMap.Builder<Class<?>, String> mimesBuilder = ImmutableBiMap.builder();
        ImmutableBiMap.Builder<String, StaticDecoder<?>> decodersBuilder = ImmutableBiMap.builder();
        decoders.forEach(decoder -> {
            mimesBuilder.put(decoder.clazz, decoder.mime);
            decodersBuilder.put(decoder.mime, decoder.decoder);
        });
        this.mimes = mimesBuilder.build();
        this.decoders = decodersBuilder.build();
    }

    /** Get the sorted keys of the routing manger used for unit test */
    SortedSet<Path> keys() {
        return paths.keySet();
    }

    @Override
    public Triad<Class<?>, String, ChannelHandler> decoder(Message message) {
        String defaultMime = mimes.get(contentTypes(message.endPoint()).iterator().next());
        String mime = message.mime().getOrElse(defaultMime);
        return new Triad<>(mimes.inverse().get(mime), mime, decoders.get(mime));
    }

    @Override
    public ImmutableCollection<Class<?>> contentTypes(String endPoint) {
        ImmutableCollection<Class<?>> types = contentTypes.get(endPoint);
        return (types == null) ? ImmutableList.of() : types;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Value<RoutedPath<T>> findPath(String endPoint, String method, Class<T> contentType) {
        return Value.of((RoutedPath<T>) paths.get(new Path(endPoint, method, contentType)));
    }

    /** The builder to create the simple router */
    static class Builder implements Router.Builder {
        private final ImmutableSortedMap.Builder<Path, RoutedPath<?>> paths = ImmutableSortedMap.naturalOrder();
        private final ImmutableMultimap.Builder<String, Class<?>> contentTypes = ImmutableMultimap.builder();
        private final ImmutableList.Builder<Decoder<?>> decoders = ImmutableList.builder();

        @Override
        @SuppressWarnings("unchecked")
        public <T> Router.Builder path(Class<T> clazz) {
            Conditions.nonNull(clazz, "clazz");
            Route route = Conditions.nonNull(clazz.getAnnotation(Route.class), "@Route");
            String path = Conditions.nonNull(route.value(), "route.value()");  // endpoint can be empty for root
            Object instance = Reflections.instance(clazz).getOrThrow("error");
            for (Field field : clazz.getDeclaredFields()) {
                EndPoint endPoint = field.getAnnotation(EndPoint.class);
                if (endPoint == null || field.getType() != Handle.class) {
                    continue; // Must include both endpoint and handle
                }
                Handle<T> handle = Reflections.<Handle<T>>getter(instance, field).get();
                if (handle != null) {
                    path(path, endPoint.method(), (Class<T>) endPoint.contentType(), handle);
                }
            }
            return this;
        }

        @Override
        public <T> Router.Builder path(String endPoint, String method, Class<T> contentType, Handle<T> handle) {
            Path path = new Path(endPoint, method, contentType);
            paths.put(path, new RoutedPath<>(path, handle));
            contentTypes.put(endPoint, contentType);
            return this;
        }

        @Override
        public Router.Builder defaultDecoders() {
            // Decoder for strings
            decoder("text/plain", String.class, new StaticDecoder<String>() {
                @Override
                protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
                    out.add(Unpooled.wrappedBuffer(msg.getBytes()));
                }
            });
            // Decoder for json objects
            decoder("application/json", JsonObject.class, new StaticDecoder<JsonObject>() {
                @Override
                protected void decode(ChannelHandlerContext ctx, JsonObject msg, List<Object> out) throws Exception {
                    out.add(Unpooled.wrappedBuffer(msg.toString().getBytes()));
                }
            });
            return this;
        }

        @Override
        public <T> Router.Builder decoder(String mime, Class<T> clazz, StaticDecoder<T> decoder) {
            decoders.add(new Decoder<>(mime, clazz, decoder));
            return this;
        }

        @Override
        public Router build() {
            return new RoutingManager(paths.build(), contentTypes.build(), decoders.build());
        }
    }

    /** An immutable path that will be used as the key for the sorted hash map */
    static class Path implements Comparable<Path> {
        final String endPoint;
        final String method;
        final Class<?> contentType;

        Path(String endPoint, String method, Class<?> contentType) {
            this.endPoint = Conditions.nonNull(endPoint, "endPoint"); // endpoint can be empty for root
            this.method = Conditions.nonNullOrEmpty(method, "method");
            this.contentType = Conditions.nonNull(contentType, "contentType");
        }

        @Override
        public int compareTo(Path other) {
            Conditions.nonNull(other, "other");
            return hashCode() - other.hashCode();
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    /** Used to pass the decoder info thought the builder and the routing manager */
    private static class Decoder<T> {
        final String mime;
        final Class<T> clazz;
        final StaticDecoder<T> decoder;

        Decoder(String mime, Class<T> clazz, StaticDecoder<T> decoder) {
            this.mime = Conditions.nonNullOrEmpty(mime, "mime");
            this.clazz = Conditions.nonNull(clazz, "class");
            this.decoder = Conditions.nonNull(decoder, "decoder");
        }

        @Override
        public String toString() {
            return Utils.toString(this);
        }
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }
}
