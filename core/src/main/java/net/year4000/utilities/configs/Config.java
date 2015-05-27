package net.year4000.utilities.configs;

import com.google.common.base.Preconditions;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.EqualsAndHashCode;
import net.year4000.utilities.sdk.HttpConnection;
import net.year4000.utilities.sdk.HttpFetcher;
import org.eclipse.jetty.io.RuntimeIOException;

/**
 * Make it simple for a class to constructor the object from a JSON web page.
 * @param <T> The class that is being used from the config.
 */
@EqualsAndHashCode
public abstract class Config<T> {
    private LoadingCache<Class<T>, T> cache;

    /** Get the class object */
    protected T getInstance(Config self) {
        final ConfigURL url = Preconditions.checkNotNull(self.getClass().getAnnotation(ConfigURL.class));

        if (cache == null) {
            cache = CacheBuilder.<Class<T>, T>newBuilder()
                .expireAfterWrite(
                    url.expire(),
                    url.unit()
                )
                .build(new CacheLoader<Class<T>, T>() {
                    @Override
                    public T load(Class<T> clazz) throws Exception {
                        HttpConnection connection = new HttpConnection(url.value());

                        try {
                            return HttpFetcher.get(connection, clazz);
                        }
                        catch (Exception e) {
                            System.err.println(e.toString());
                            throw new RuntimeIOException(e);
                        }
                    }
                });
        }

        // Have to cast or wont compile right
        return (T) cache.getUnchecked(url.config());
    }
}
