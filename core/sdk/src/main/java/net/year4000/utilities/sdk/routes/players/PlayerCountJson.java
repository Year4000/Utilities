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

import java.util.Map;

public class PlayerCountJson {
    private Count network;
    private Map<String, Count> groups;
    private Map<String, Count> servers;

    @java.beans.ConstructorProperties({"network", "groups", "servers"})
    public PlayerCountJson(Count network, Map<String, Count> groups, Map<String, Count> servers) {
        this.network = network;
        this.groups = groups;
        this.servers = servers;
    }

    public Count getNetwork() {
        return this.network;
    }

    public Map<String, Count> getGroups() {
        return this.groups;
    }

    public Map<String, Count> getServers() {
        return this.servers;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof PlayerCountJson)) return false;
        final PlayerCountJson other = (PlayerCountJson) o;
        final Object this$network = this.network;
        final Object other$network = other.network;
        if (this$network == null ? other$network != null : !this$network.equals(other$network)) return false;
        final Object this$groups = this.groups;
        final Object other$groups = other.groups;
        if (this$groups == null ? other$groups != null : !this$groups.equals(other$groups)) return false;
        final Object this$servers = this.servers;
        final Object other$servers = other.servers;
        if (this$servers == null ? other$servers != null : !this$servers.equals(other$servers)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $network = this.network;
        result = result * PRIME + ($network == null ? 0 : $network.hashCode());
        final Object $groups = this.groups;
        result = result * PRIME + ($groups == null ? 0 : $groups.hashCode());
        final Object $servers = this.servers;
        result = result * PRIME + ($servers == null ? 0 : $servers.hashCode());
        return result;
    }

    public String toString() {
        return "net.year4000.utilities.sdk.routes.players.PlayerCountJson(network=" + this.network + ", groups=" + this.groups + ", servers=" + this.servers + ")";
    }

    public static class Count {
        private int online;
        private int max;

        @java.beans.ConstructorProperties({"online", "max"})
        public Count(int online, int max) {
            this.online = online;
            this.max = max;
        }

        public int getOnline() {
            return this.online;
        }

        public int getMax() {
            return this.max;
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Count)) return false;
            final Count other = (Count) o;
            if (this.online != other.online) return false;
            if (this.max != other.max) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            result = result * PRIME + this.online;
            result = result * PRIME + this.max;
            return result;
        }

        public String toString() {
            return "net.year4000.utilities.sdk.routes.players.PlayerCountJson.Count(online=" + this.online + ", max=" + this.max + ")";
        }
    }
}
