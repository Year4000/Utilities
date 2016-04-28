package net.year4000.utilities.router;

import com.google.common.collect.ImmutableSortedMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.Utils;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.util.Objects;

public class RoutingManager implements Router {
    private final ImmutableSortedMap<Path, RoutedPath<?>> paths;

    private RoutingManager(ImmutableSortedMap<Path, RoutedPath<?>> paths) {
        this.paths = Conditions.nonNull(paths, paths);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Value<RoutedPath<T>> findPath(String prefix, String method, Class<?> contentType) {
        return Value.of((RoutedPath<T>) paths.get(new Path(prefix, method, contentType)));
    }

    /** The builder to create the simple router */
    static class Builder implements Router.Builder {
        private ImmutableSortedMap.Builder<Path, RoutedPath<?>> paths = ImmutableSortedMap.<Path, RoutedPath<?>>naturalOrder();

        @Override
        @SuppressWarnings("unchecked")
        public <T> Router.Builder path(Class<T> clazz) {
            Conditions.nonNull(clazz, "clazz");
            Route route = Conditions.nonNull(clazz.getAnnotation(Route.class), "@Route");
            String path = Conditions.nonNullOrEmpty(route.value(), "route.value()");
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
        public <T> Router.Builder path(String prefix, String method, Class<T> contentType, Handle<T> handle) {
            Path path = new Path(prefix, method, contentType);
            paths.put(path, new RoutedPath<>(path, handle));
            return this;
        }

        @Override
        public Router build() {
            return new RoutingManager(paths.build());
        }
    }

    /** An immutable path that will be used as the key for the sorted hash map */
    static class Path implements Comparable<Path> {
        private final String prefix;
        private final String method;
        private final Class<?> contentType;

        Path(String prefix, String method, Class<?> contentType) {
            this.prefix = Conditions.nonNullOrEmpty(prefix, "prefix");
            this.method = Conditions.nonNullOrEmpty(method, "method");
            this.contentType = Conditions.nonNull(contentType, "contentType");
        }

        @Override
        public int compareTo(Path other) {
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
