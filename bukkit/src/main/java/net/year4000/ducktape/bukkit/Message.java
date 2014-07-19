package net.year4000.ducktape.bukkit;

import com.ewized.utilities.bukkit.util.BukkitLocale;
import com.ewized.utilities.core.util.locale.LocaleUtil;
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
