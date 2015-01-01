package net.year4000.utilities.bukkit;

import net.year4000.utilities.LogUtil;
import net.year4000.utilities.bukkit.locale.MessageLocale;
import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is a wrapper for Bukkit plugins, this will allow for
 * quick and creation of common tasks.
 */
@SuppressWarnings("unused")
public class BukkitPlugin extends JavaPlugin {
    @Getter(AccessLevel.PRIVATE)
    private static BukkitPlugin inst;
    @Getter
    private final BukkitCommandsManager commands = new BukkitCommandsManager();
    @Getter
    public LogUtil log = new LogUtil(getLogger());
    @Getter
    public boolean debug = log.isDebug();

    /** Load this instance */
    public BukkitPlugin() {
        inst = this;
    }

    /** Set the new debug status of this plugin */
    public void setDebug(boolean debug) {
        log.setDebug(debug);
    }

    // Command Stuff //

    /** Register a command for this plugin */
    public void registerCommand(Class<?> commandClass) {
        new CommandsManagerRegistration(this, commands).register(commandClass);
    }

    @SuppressWarnings("unchecked")
    public boolean onCommand(CommandSender sender, Command cmd, String commandName, String[] args) {
        List<String> msg = new ArrayList<>();

        MessageLocale locale = new MessageLocale(sender);

        try {
            commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            msg.add(locale.get("error.cmd.permission"));
        } catch (MissingNestedCommandException e) {
            msg.add(locale.get("error.cmd.usage", e.getUsage()));
        } catch (CommandUsageException e) {
            msg.add(ChatColor.RED + e.getMessage());
            msg.add(locale.get("error.cmd.usage", e.getUsage()));
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                msg.add(locale.get("error.cmd.number"));
            }
            else {
                msg.add(locale.get("error.cmd.error"));
                e.printStackTrace();
            }
        } catch (CommandException e) {
            msg.add(ChatColor.RED + e.getMessage());
        } finally {
            Iterator<String> line = msg.listIterator();

            if (line.hasNext()) {
                sender.sendMessage(MessageUtil.message(" &7[&e!&7] &e") + line.next());

                while (line.hasNext()) {
                    sender.sendMessage(line.next());
                }
            }
        }

        return true;
    }

    // Logger Stuff //

    /** Logs a message to the console */
    public static void log(String message, Object... args) {
        getInst().log.log(message, args);
    }

    /** Logs a debug message to the console */
    public static void debug(String message, Object... args) {
        getInst().log.debug(message, args);
    }

    /** Print out the stack trace */
    public static void debug(Exception e, boolean simple) {
        getInst().log.debug(e, simple);
    }

    /** Print out the stack trace */
    public static void log(Exception e, boolean simple) {
        getInst().log.log(e, simple);
    }
}
