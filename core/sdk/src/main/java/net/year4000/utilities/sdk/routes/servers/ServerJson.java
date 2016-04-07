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

import net.year4000.utilities.ObjectHelper;
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

    @Override
    public String toString() {
        return ObjectHelper.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return ObjectHelper.equals(this, other);
    }

    @Override
    public int hashCode() {
        return ObjectHelper.hashCode(this);
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

        @Override
        public String toString() {
            return ObjectHelper.toString(this);
        }

        @Override
        public boolean equals(Object other) {
            return ObjectHelper.equals(this, other);
        }

        @Override
        public int hashCode() {
            return ObjectHelper.hashCode(this);
        }
    }
}
