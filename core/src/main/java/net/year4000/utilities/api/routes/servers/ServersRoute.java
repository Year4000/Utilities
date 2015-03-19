package net.year4000.utilities.api.routes.servers;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import net.year4000.utilities.api.routes.Route;

import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServersRoute extends Route<Map<String, ServerJson>> {
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
