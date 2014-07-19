package net.year4000.ducktape.bungee;

import com.ewized.utilities.bungee.util.BungeeLocale;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Message extends BungeeLocale {
    public Message(ProxiedPlayer player) {
        super(player);
        localeManager = MessageManager.get();
    }

    public Message(CommandSender sender) {
        this(sender instanceof ProxiedPlayer ? (ProxiedPlayer) sender : null);
    }
}
