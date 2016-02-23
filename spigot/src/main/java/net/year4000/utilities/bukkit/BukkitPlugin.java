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

package net.year4000.utilities.bukkit;

import com.sk89q.bukkit.util.BukkitCommandsManager;
import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import lombok.AccessLevel;
import lombok.Getter;
import net.year4000.utilities.LogUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import static net.year4000.utilities.bukkit.Messages.*;

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

    /** Logs a message to the console */
    public static void log(String message, Object... args) {
        getInst().log.log(message, args);
    }

    // Command Stuff //

    /** Logs a debug message to the console */
    public static void debug(String message, Object... args) {
        getInst().log.debug(message, args);
    }

    /** Print out the stack trace */
    public static void debug(Exception e, boolean simple) {
        getInst().log.debug(e, simple);
    }

    // Logger Stuff //

    /** Print out the stack trace */
    public static void log(Exception e, boolean simple) {
        getInst().log.log(e, simple);
    }

    /** Set the new debug status of this plugin */
    public void setDebug(boolean debug) {
        log.setDebug(debug);
    }

    /** Register a command for this plugin */
    public void registerCommand(Class<?> commandClass) {
        new CommandsManagerRegistration(this, commands).register(commandClass);
    }

    @SuppressWarnings("unchecked")
    public boolean onCommand(CommandSender sender, Command cmd, String commandName, String[] args) {
        List<String> msg = new ArrayList<>();
        Optional<Locale> locale = Optional.empty();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            locale = Optional.of(new Locale(player.spigot().getLocale()));
        }

        try {
            commands.execute(cmd.getName(), args, sender, sender);
        }
        catch (CommandPermissionsException e) {
            msg.add(CMD_ERROR_PERMISSION.get(locale));
        }
        catch (MissingNestedCommandException e) {
            msg.add(CMD_ERROR_USAGE.get(locale, e.getUsage()));
        }
        catch (CommandUsageException e) {
            msg.add(ChatColor.RED + e.getMessage());
            msg.add(CMD_ERROR_USAGE.get(locale, e.getUsage()));
        }
        catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                msg.add(CMD_ERROR_NUMBER.get(locale));
            }
            else {
                msg.add(CMD_ERROR.get(locale));
                e.printStackTrace();
            }
        }
        catch (CommandException e) {
            msg.add(ChatColor.RED + e.getMessage());
        }
        finally {
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
}
