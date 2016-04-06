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

import net.year4000.utilities.net.Pinger;

public class ServerJson {
    private String name;
    private Group group;
    private Pinger.StatusResponse status;

    @java.beans.ConstructorProperties({"name", "group", "status"})
    public ServerJson(String name, Group group, Pinger.StatusResponse status) {
        this.name = name;
        this.group = group;
        this.status = status;
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

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof ServerJson)) return false;
        final ServerJson other = (ServerJson) o;
        final Object this$name = this.name;
        final Object other$name = other.name;
        if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
        final Object this$group = this.group;
        final Object other$group = other.group;
        if (this$group == null ? other$group != null : !this$group.equals(other$group)) return false;
        final Object this$status = this.status;
        final Object other$status = other.status;
        if (this$status == null ? other$status != null : !this$status.equals(other$status)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.name;
        result = result * PRIME + ($name == null ? 0 : $name.hashCode());
        final Object $group = this.group;
        result = result * PRIME + ($group == null ? 0 : $group.hashCode());
        final Object $status = this.status;
        result = result * PRIME + ($status == null ? 0 : $status.hashCode());
        return result;
    }

    public String toString() {
        return "net.year4000.utilities.sdk.routes.servers.ServerJson(name=" + this.name + ", group=" + this.group + ", status=" + this.status + ")";
    }

    public static class Group {
        private String name;
        private String display;

        @java.beans.ConstructorProperties({"name", "display"})
        public Group(String name, String display) {
            this.name = name;
            this.display = display;
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

        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof Group)) return false;
            final Group other = (Group) o;
            final Object this$name = this.name;
            final Object other$name = other.name;
            if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
            final Object this$display = this.display;
            final Object other$display = other.display;
            if (this$display == null ? other$display != null : !this$display.equals(other$display)) return false;
            return true;
        }

        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final Object $name = this.name;
            result = result * PRIME + ($name == null ? 0 : $name.hashCode());
            final Object $display = this.display;
            result = result * PRIME + ($display == null ? 0 : $display.hashCode());
            return result;
        }

        public String toString() {
            return "net.year4000.utilities.sdk.routes.servers.ServerJson.Group(name=" + this.name + ", display=" + this.display + ")";
        }
    }
}
