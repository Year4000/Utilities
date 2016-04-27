package net.year4000.utilities.router;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

public class RoutedPath<T> {
    private final String method;
    private final String prefix;
    private final Handle<T> handle;
    private final Class<T> contentType;

    RoutedPath(String prefix, String method, Class<T> contentType, Handle<T> handle) {
        this.method = Conditions.nonNullOrEmpty(method, "method");
        this.prefix = Conditions.nonNullOrEmpty(prefix, "prefix");
        this.handle = Conditions.nonNull(handle, "handle");
        this.contentType = Conditions.nonNull(contentType, "contentType");
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
        return Utils.toString(this, "method", "prefix", "contentTpe");
    }
}
