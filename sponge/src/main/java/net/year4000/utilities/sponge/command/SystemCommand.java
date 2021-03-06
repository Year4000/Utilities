/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.command;

import static net.year4000.utilities.sponge.Messages.CMD_CPU_TOTAL;
import static net.year4000.utilities.sponge.Messages.CMD_MEMORY_FREE;
import static net.year4000.utilities.sponge.Messages.CMD_MEMORY_MAX;
import static net.year4000.utilities.sponge.Messages.CMD_MEMORY_TOTAL;
import static net.year4000.utilities.sponge.Messages.CMD_SYSTEM;
import static net.year4000.utilities.sponge.Messages.CMD_UPTIME;
import static org.spongepowered.api.text.format.TextColors.GOLD;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.YELLOW;

import com.google.inject.Injector;
import net.year4000.utilities.TimeUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

public final class SystemCommand implements CommandExecutor {
    private static final String[] ALIAS = new String[] {"system", "mem"};
    private static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
        .description(Text.of("Grab details about this system."))
        .permission("utilities.command.system")
        .executor(new SystemCommand())
        .build();

    /** Register this command with the manager */
    public static void register(Object object, Injector injector) {
        injector.injectMembers(COMMAND_SPEC.getExecutor());
        Sponge.getCommandManager().register(object, COMMAND_SPEC, ALIAS);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Runtime runtime = Runtime.getRuntime();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        src.sendMessage(CMD_SYSTEM.get(src));
        src.sendMessage(CMD_UPTIME.get(src, "&6" + new TimeUtil(uptime, TimeUnit.MILLISECONDS).prettyOutput("&8:&6")));
        src.sendMessage(CMD_CPU_TOTAL.get(src, Text.of(GOLD, runtime.availableProcessors())));
        src.sendMessage(CMD_MEMORY_FREE.get(src, formatMemory(runtime.freeMemory())));
        src.sendMessage(CMD_MEMORY_MAX.get(src, formatMemory(runtime.maxMemory())));
        src.sendMessage(CMD_MEMORY_TOTAL.get(src, formatMemory(runtime.totalMemory())));
        return CommandResult.success();
    }

    /** Calculate the bytes to a human friendly format */
    private Text formatMemory(long bytes) {
        int unit = 1024;

        if (bytes < unit) {
            return Text.of(YELLOW, bytes, GOLD, " b");
        }

        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = " KMGTPE".charAt(exp);
        int whole = (int) (bytes / Math.pow(unit, exp));
        int part = (int) (bytes % Math.pow(unit, exp)) % 10;

        return Text.of(YELLOW, whole, GRAY, ".", YELLOW, part, " ", GOLD, pre + "B");
    }
}
