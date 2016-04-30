package net.year4000.utilities.router;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSortedMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.Utils;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.util.SortedSet;

public class RoutingManager implements Router {
    private final ImmutableSortedMap<Path, RoutedPath<?>> paths;
    private final ImmutableMultimap<String, Class<?>> contentTypes;

    private RoutingManager(ImmutableSortedMap<Path, RoutedPath<?>> paths, ImmutableMultimap<String, Class<?>> contentTypes) {
        this.paths = Conditions.nonNull(paths, paths);
        this.contentTypes = Conditions.nonNull(contentTypes, "contentTypes");
    }

    /** Get the sorted keys of the routing manger used for unit test */
    SortedSet<Path> keys() {
        return paths.keySet();
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
        private ImmutableSortedMap.Builder<Path, RoutedPath<?>> paths = ImmutableSortedMap.naturalOrder();
        private ImmutableMultimap.Builder<String, Class<?>> contentTypes = ImmutableMultimap.builder();

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
        public <T> Router.Builder path(String endPoint, String method, Class<T> contentType, Handle<T> handle) {
            Path path = new Path(endPoint, method, contentType);
            paths.put(path, new RoutedPath<>(path, handle));
            contentTypes.put(endPoint, contentType);
            return this;
        }

        @Override
        public Router build() {
            return new RoutingManager(paths.build(), contentTypes.build());
        }
    }

    /** An immutable path that will be used as the key for the sorted hash map */
    static class Path implements Comparable<Path> {
        final String endPoint;
        final String method;
        final Class<?> contentType;

        Path(String endPoint, String method, Class<?> contentType) {
            this.endPoint = Conditions.nonNullOrEmpty(endPoint, "endPoint");
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
