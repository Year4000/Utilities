package net.year4000.ducktape.bukkit;

import net.year4000.utilities.bukkit.BukkitLocale;
import net.year4000.utilities.locale.LocaleUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message extends BukkitLocale implements LocaleUtil {
    public Message(Player player) {
        super(player);
        localeManager = MessageManager.get();
    }

    public Message(CommandSender sender) {
        this(sender instanceof Player ? (Player) sender : null);
    }
}
