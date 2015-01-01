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
