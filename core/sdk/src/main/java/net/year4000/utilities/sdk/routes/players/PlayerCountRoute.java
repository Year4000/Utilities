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

package net.year4000.utilities.sdk.routes.players;

import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import net.year4000.utilities.sdk.routes.Route;

public class PlayerCountRoute extends Route<PlayerCountJson> {
    private PlayerCountRoute() {
    }

    /** Get the player count of the network */
    public PlayerCountJson.Count getNetworkPlayerCount() {
        return response.getNetwork();
    }

    /** Get the player count of the network */
    public ImmutableMap<String, PlayerCountJson.Count> getGroupsPlayerCount() {
        return new ImmutableMap.Builder<String, PlayerCountJson.Count>().putAll(response.getGroups()).build();
    }

    /** Get the player count of the specific server */
    public PlayerCountJson.Count getGroupPlayerCount(@NonNull String group) {
        return response.getGroups().get(group);
    }

    /** Get the player count of the network */
    public ImmutableMap<String, PlayerCountJson.Count> getServersPlayerCount() {
        return new ImmutableMap.Builder<String, PlayerCountJson.Count>().putAll(response.getServers()).build();
    }

    /** Get the player count of the specific server */
    public PlayerCountJson.Count getServerPlayerCount(@NonNull String server) {
        return response.getServers().get(server);
    }
}
