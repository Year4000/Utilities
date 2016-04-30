package net.year4000.utilities.router;

import com.google.common.collect.ImmutableCollection;
import io.netty.util.AttributeKey;
import net.year4000.utilities.value.Value;

/** Routers must be immutable this is so the design must be fast */
public interface Router {
    AttributeKey<Router> ATTRIBUTE_KEY = AttributeKey.newInstance("router");

    /** Get the known contentTypes for the selected prefix */
    ImmutableCollection<Class<?>> contentTypes(String endPoint);

    /** Tries to find the routed path from the prefix */
    <T> Value<RoutedPath<T>> findPath(String endPoint, String method, Class<T> contentType);

    /** Create the router builder */
    static Builder builder() {
        return new RoutingManager.Builder();
    }

    /** The builder that the router must include */
    interface Builder extends net.year4000.utilities.Builder<Router> {
        /** Add a class that contains a @Route annotation that will search for @EndPoints */
        <T> Builder path(Class<T> clazz);

        /** Embed static handles into the router */
        <T> Builder path(String prefix, String method, Class<T> contentType, Handle<T> handle);
    }
}
