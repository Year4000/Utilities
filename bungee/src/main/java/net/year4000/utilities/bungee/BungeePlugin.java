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

package net.year4000.utilities.bungee;

import com.sk89q.bungee.util.BungeeCommandsManager;
import com.sk89q.bungee.util.CommandExecutor;
import com.sk89q.bungee.util.CommandRegistration;
import com.sk89q.minecraft.util.commands.*;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.year4000.utilities.LogUtil;
import net.year4000.utilities.Conditions;

import java.util.*;

import static net.year4000.utilities.bungee.Messages.*;

/**
 * This is a wrapper for BungeeCord plugins, this will allow for
 * quick and creation of common tasks.
 */
@SuppressWarnings("unused")
public class BungeePlugin extends Plugin implements CommandExecutor<CommandSender> {
    private static BungeePlugin inst;
    private final BungeeCommandsManager commands = new BungeeCommandsManager();
    public LogUtil log = new LogUtil(ProxyServer.getInstance().getLogger());
    public boolean debug = log.isDebug();

    /** Load this instance */
    public BungeePlugin() {
        inst = this;
    }

    /** Logs a message to the console */
    public static void log(String message, Object... args) {
        getInst().log.log(message, args);
    }

    /** Logs a debug message to the console */
    public static void debug(String message, Object... args) {
        getInst().log.debug(message, args);
    }

    // Command Stuff //

    /** Print out the stack trace */
    public static void debug(Exception e, boolean simple) {
        getInst().log.debug(e, simple);
    }

    /** Print out the stack trace */
    public static void log(Exception e, boolean simple) {
        getInst().log.log(e, simple);
    }

    private static BungeePlugin getInst() {
        return BungeePlugin.inst;
    }

    // Logger Stuff //

    /** Re-register the LogUtil after plugin been enabled */
    @Override
    public void onEnable() {
        log.setLogger(getLogger());
    }

    /** Set the new debug status of this plugin */
    public void setDebug(boolean debug) {
        log.setDebug(debug);
    }

    /** Register a command for this plugin */
    public void registerCommand(Class<?> commandClass) {
        new CommandRegistration(this, getProxy().getPluginManager(), commands, this).register(commandClass);
    }

    @Override
    public void onCommand(CommandSender sender, String commandName, String[] args) {
        List<BaseComponent[]> msg = new ArrayList<>();

        Optional<Locale> locale = Optional.empty();

        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            locale = Optional.of(player.getLocale());
        }

        try {
            commands.execute(commandName, args, sender, sender);
        }
        catch (CommandPermissionsException e) {
            msg.add(MessageUtil.message(CMD_ERROR_PERMISSION.get(locale)));
        }
        catch (MissingNestedCommandException e) {
            msg.add(MessageUtil.message(CMD_ERROR_USAGE.get(locale, e.getUsage())));
        }
        catch (CommandUsageException e) {
            msg.add(MessageUtil.message(ChatColor.RED + e.getMessage()));
            msg.add(MessageUtil.message(CMD_ERROR_USAGE.get(locale, e.getUsage())));
        }
        catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                msg.add(MessageUtil.message(CMD_ERROR_NUMBER.get(locale)));
            }
            else {
                msg.add(MessageUtil.message(CMD_ERROR.get(locale)));
                e.printStackTrace();
            }
        }
        catch (CommandException e) {
            msg.add(MessageUtil.message(ChatColor.RED + e.getMessage()));
        }
        finally {
            Iterator<BaseComponent[]> line = msg.listIterator();

            if (line.hasNext()) {
                sender.sendMessage(MessageUtil.merge(" &7[&e!&7] ", line.next()));

                while (line.hasNext()) {
                    sender.sendMessage(line.next());
                }
            }
        }
    }

    public BungeeCommandsManager getCommands() {
        return this.commands;
    }

    public LogUtil getLog() {
        return this.log;
    }

    public boolean isDebug() {
        return this.debug;
    }

    public void setLog(LogUtil log) {
        this.log = log;
    }

    @Override
    public String toString() {
        return Conditions.toString(this);
    }

    @Override
    public boolean equals(Object other) {
        return Conditions.equals(this, other);
    }

    @Override
    public int hashCode() {
        return Conditions.hashCode(this);
    }
}
