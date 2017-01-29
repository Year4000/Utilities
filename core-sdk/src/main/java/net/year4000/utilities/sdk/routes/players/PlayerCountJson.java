/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk.routes.players;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;

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
        return Utils.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Utils.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Utils.hashCode(this);
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
            return Utils.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return Utils.equals(this, other);
        }

        @Override
        public int hashCode() {
            return Utils.hashCode(this);
        }
    }
}
