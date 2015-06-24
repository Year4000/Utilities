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

package net.year4000.utilities.bungee.locale;

import net.year4000.utilities.bungee.BungeeLocale;
import net.year4000.utilities.bungee.Utilities;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public final class MessageLocale extends BungeeLocale {
    public MessageLocale(CommandSender sender) {
        super(sender instanceof ProxiedPlayer ? (ProxiedPlayer) sender : null);

        try {
            localeManager = URLMessageManager.get();

            if (localeManager.getLocales().size() == 0) {
                throw new Exception("URLMessageManager has 0 locales loaded.");
            }
        } catch (Exception e) {
            Utilities.log(e, true);
            localeManager = ClassMessageManager.get();
        }
    }
}
