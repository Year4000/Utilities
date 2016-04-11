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

import net.year4000.utilities.Conditions;

import java.util.Map;

public class PlayerCountJson {
    private Count network;
    private Map<String, Count> groups;
    private Map<String, Count> servers;

    public PlayerCountJson(Count network, Map<String, Count> groups, Map<String, Count> servers) {
        this.network = Conditions.nonNull(network, "network");
        this.groups = Conditions.nonNull(groups, "groups");
        this.servers = Conditions.nonNull(servers, "servers");
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

    @Override
    public String toString() {
        return Conditions.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Conditions.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Conditions.hashCode(this);
    }

    public static class Count {
        private int online;
        private int max;

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

        @Override
        public String toString() {
            return Conditions.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Conditions.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Conditions.hashCode(this);
        }
    }
}
