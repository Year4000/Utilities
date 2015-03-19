package net.year4000.utilities;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.year4000.utilities.sdk.API;

import java.util.concurrent.TimeUnit;

public class AccountBadgeManager extends AbstractBadgeManager<String> {
    private static final API api = new API();
    private static final LoadingCache<String, Badges> BADGES = CacheBuilder.<String, Badges>newBuilder()
        .weakKeys()
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Badges>() {
            @Override
            public Badges load(String account) throws Exception {
                return Badges.valueOf(api.getAccount(account).getRank().toUpperCase());
            }
        });

    /** Find the badge the player should have */
    public Badges findBadge(String player) {
        return BADGES.getUnchecked(player);
    }

    /** Get the badge in bracket form */
    public String getBadge(String player) {
        Badges badge = findBadge(player);
        return MessageUtil.replaceColors("&f[" + badge + "&f]");
    }
}
