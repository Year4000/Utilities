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

package net.year4000.ducktape;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.year4000.utilities.config.Comment;
import net.year4000.utilities.config.Config;

@Data
@EqualsAndHashCode(callSuper = false)
public class GlobalSettings extends Config {
    public GlobalSettings() {
        CONFIG_HEADER = new String[] {"DuckTape Settings"};
    }

    @Comment("The URL to get the locale files from.")
    private String url = "https://git.year4000.net/year4000/locales/raw/master/net/year4000/ducktape/locales/";

    @Comment("The path were the modules are stored.")
    private String modulesPath = "modules";

    @Comment("The path were the modules data folder is stored.")
    private String modulesData = "data";
}
