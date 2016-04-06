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

import lombok.Value;
import net.year4000.utilities.net.Pinger;

@Value
public class ServerJson {
    private String name;
    private Group group;
    private Pinger.StatusResponse status;

    /** Is this server hidden */
    public boolean isHidden() {
        return name.startsWith(".") || getGroup().isHidden();
    }

    @Value
    public static class Group {
        private String name;
        private String display;

        /** Is this server hidden */
        public boolean isHidden() {
            return name.startsWith(".");
        }
    }
}
