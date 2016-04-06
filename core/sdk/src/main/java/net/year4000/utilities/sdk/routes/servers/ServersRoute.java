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

package net.year4000.utilities.sdk.routes.servers;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.NonNull;
import net.year4000.utilities.sdk.routes.Route;

import java.util.Map;

public class ServersRoute extends Route<Map<String, ServerJson>> {
    private ServersRoute() {
    }

    /** Get a immutable version of the map */
    public ImmutableMap<String, ServerJson> getServersMap() {
        return new ImmutableMap.Builder<String, ServerJson>().putAll(response).build();
    }

    /** Get a collection of all the ServerJson objects returned by this route */
    public ImmutableCollection<ServerJson> getServersCollection() {
        return new ImmutableList.Builder<ServerJson>().addAll(response.values()).build();
    }

    /** Get a server object by its name */
    public ServerJson getServer(@NonNull String server) {
        return response.get(server);
    }
}
