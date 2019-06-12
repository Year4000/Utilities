/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.year4000.utilities.minecraft.MessageUtil;

import java.util.concurrent.TimeUnit;

public class AccountBadgeManager extends AbstractBadgeManager<String> {
    private static final API api = new API();
    private static final LoadingCache<String, Badges> BADGES = CacheBuilder.<String, Badges>newBuilder()
        .weakKeys()
        .expireAfterWrite(30, TimeUnit.MINUTES)
        .build(new CacheLoader<String, Badges>() {
            @Override
            public Badges load(String account) throws Exception {
                try {
                    return Badges.valueOf(api.getAccount(account).getRank().toUpperCase());
                }
                // SocketTimeoutException within runtime Exception
                catch (RuntimeException error) {
                    return Badges.MISSING;
                }
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
