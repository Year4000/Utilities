package net.year4000.utilities.router;

import com.google.common.collect.ImmutableSortedMap;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Reflections;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;

public class RoutingManager implements Router {
    private final ImmutableSortedMap<String, RoutedPath<?>> paths;

    private RoutingManager(ImmutableSortedMap<String, RoutedPath<?>> paths) {
        this.paths = Conditions.nonNull(paths, paths);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Value<RoutedPath<T>> findPath(String prefix) {
        return Value.of((RoutedPath<T>) paths.get(prefix));
    }

    /** The builder to create the simple router */
    static class Builder implements Router.Builder {
        private ImmutableSortedMap.Builder<String, RoutedPath<?>> paths = ImmutableSortedMap.naturalOrder();

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
            paths.put(prefix, new RoutedPath<>(prefix, method, contentType, handle));
            return this;
        }

        @Override
        public Router build() {
            return new RoutingManager(paths.build());
        }
    }
}
