/*
 * Copyright 2015 Year4000.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
