/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.cache;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.net.HttpConnection;
import net.year4000.utilities.net.JsonHttpFetcher;

/**
 * Make it simple for a class to constructor the object from a JSON web page.
 */
public abstract class JsonConfigCache {
    private LoadingCache<Class<? extends JsonConfigCache>, JsonConfigCache> cache;

    /** Get the class object */
    public static <T extends JsonConfigCache> T getInstance(JsonConfigCache self) {
        final EndPoint url = Preconditions.checkNotNull(self.getClass().getAnnotation(EndPoint.class));

        if (self.cache == null) {
            self.cache = CacheBuilder.<Class<? extends JsonConfigCache>, JsonConfigCache>newBuilder()
                .expireAfterWrite(
                    url.expire(),
                    url.unit()
                )
                .build(new CacheLoader<Class<? extends JsonConfigCache>, JsonConfigCache>() {
                    @Override
                    public JsonConfigCache load(Class<? extends JsonConfigCache> clazz) throws Exception {
                        HttpConnection connection = new HttpConnection(url.value());

                        try {
                            return JsonHttpFetcher.builder().build().get(connection, clazz);
                        }
                        catch (Exception e) {
                            System.err.println(e.toString());
                            throw new RuntimeException(e);
                        }
                    }
                });
        }

        // Have to cast or wont compile right
        return (T) self.cache.getUnchecked(url.config());
    }

    @Override
    public String toString() {
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
    }
}
