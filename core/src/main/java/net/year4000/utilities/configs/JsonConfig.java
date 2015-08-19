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
 */
@EqualsAndHashCode
public abstract class JsonConfig {
    private LoadingCache<Class<? extends JsonConfig>, JsonConfig> cache;

    /** Get the class object */
    public static <T extends JsonConfig> T getInstance(JsonConfig self) {
        final ConfigURL url = Preconditions.checkNotNull(self.getClass().getAnnotation(ConfigURL.class));

        if (self.cache == null) {
            self.cache = CacheBuilder.<Class<? extends JsonConfig>, JsonConfig>newBuilder()
                .expireAfterWrite(
                    url.expire(),
                    url.unit()
                )
                .build(new CacheLoader<Class<? extends JsonConfig>, JsonConfig>() {
                    @Override
                    public JsonConfig load(Class<? extends JsonConfig> clazz) throws Exception {
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
        return (T) self.cache.getUnchecked(url.config());
    }
}
