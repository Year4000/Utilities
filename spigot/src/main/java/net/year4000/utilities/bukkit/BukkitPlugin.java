/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.bukkit;

import net.year4000.utilities.LogUtil;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is a wrapper for Bukkit plugins, this will allow for
 * quick and creation of common tasks.
 */
@SuppressWarnings("unused")
public class BukkitPlugin extends JavaPlugin {
    private static BukkitPlugin inst;
//    private final BukkitCommandsManager commands = new BukkitCommandsManager();
    public LogUtil log = new LogUtil(getLogger());
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

    private static BukkitPlugin getInst() {
        return BukkitPlugin.inst;
    }

    /** Set the new debug status of this plugin */
    public void setDebug(boolean debug) {
        log.setDebug(debug);
    }

//    /** Register a command for this plugin */
//    public void registerCommand(Class<?> commandClass) {
//        new CommandsManagerRegistration(this, commands).register(commandClass);
//    }
//
//    @SuppressWarnings("unchecked")
//    public boolean onCommand(CommandSender sender, Command cmd, String commandName, String[] args) {
//        List<String> msg = new ArrayList<>();
//        Optional<Locale> locale = Optional.empty();
//
//        if (sender instanceof Player) {
//            Player player = (Player) sender;
//            locale = Optional.of(new Locale(player.spigot().getLocale()));
//        }
//
//        try {
//            commands.execute(cmd.getName(), args, sender, sender);
//        }
//        catch (CommandPermissionsException e) {
//            msg.add(CMD_ERROR_PERMISSION.get(locale));
//        }
//        catch (MissingNestedCommandException e) {
//            msg.add(CMD_ERROR_USAGE.get(locale, e.getUsage()));
//        }
//        catch (CommandUsageException e) {
//            msg.add(ChatColor.RED + e.getMessage());
//            msg.add(CMD_ERROR_USAGE.get(locale, e.getUsage()));
//        }
//        catch (WrappedCommandException e) {
//            if (e.getCause() instanceof NumberFormatException) {
//                msg.add(CMD_ERROR_NUMBER.get(locale));
//            }
//            else {
//                msg.add(CMD_ERROR.get(locale));
//                e.printStackTrace();
//            }
//        }
//        catch (CommandException e) {
//            msg.add(ChatColor.RED + e.getMessage());
//        }
//        finally {
//            Iterator<String> line = msg.listIterator();
//
//            if (line.hasNext()) {
//                sender.sendMessage(MessageUtil.message(" &7[&e!&7] &e") + line.next());
//
//                while (line.hasNext()) {
//                    sender.sendMessage(line.next());
//                }
//            }
//        }
//
//        return true;
//    }
//
//    public BukkitCommandsManager getCommands() {
//        return this.commands;
//    }

    public LogUtil getLog() {
        return this.log;
    }

    public boolean isDebug() {
        return this.debug;
    }
}
