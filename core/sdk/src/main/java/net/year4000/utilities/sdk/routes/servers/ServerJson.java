/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sdk.routes.servers;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.Utils;
import net.year4000.utilities.net.Pinger;

public class ServerJson {
    private String name;
    private Group group;
    private Pinger.StatusResponse status;

    public ServerJson(String name, Group group, Pinger.StatusResponse status) {
        this.name = Conditions.nonNullOrEmpty(name, "name");
        this.group = Conditions.nonNull(group, "group");
        this.status = Conditions.nonNull(status, "status");
    }

    /** Is this server hidden */
    public boolean isHidden() {
        return name.startsWith(".") || getGroup().isHidden();
    }

    public String getName() {
        return this.name;
    }

    public Group getGroup() {
        return this.group;
    }

    public Pinger.StatusResponse getStatus() {
        return this.status;
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

    public static class Group {
        private String name;
        private String display;

        public Group(String name, String display) {
            this.name = Conditions.nonNullOrEmpty(name, "name");
            this.display = Conditions.nonNullOrEmpty(display, "display");
        }

        /** Is this server hidden */
        public boolean isHidden() {
            return name.startsWith(".");
        }

        public String getName() {
            return this.name;
        }

        public String getDisplay() {
            return this.display;
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
