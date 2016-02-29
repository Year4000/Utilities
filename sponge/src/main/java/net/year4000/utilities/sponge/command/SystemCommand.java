package net.year4000.utilities.sponge.command;

import net.year4000.utilities.TimeUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

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
    public static void register(Object object) {
        Sponge.getCommandManager().register(object, COMMAND_SPEC, ALIAS);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Runtime runtime = Runtime.getRuntime();
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        src.sendMessage(Text.of(
            TextColors.YELLOW,
            "Uptime",
            TextColors.GRAY,
            ": ",
            TextColors.GOLD,
            new TimeUtil(uptime, TimeUnit.MILLISECONDS).prettyOutput()
        ));
        //src.sendMessage(Text.of(Messages.CMD_SYSTEM_MEMORY.get(src, runtime.availableProcessors())));
        src.sendMessage(Text.of(
            TextColors.YELLOW,
            "System Processors",
            TextColors.GRAY,
            ": ",
            TextColors.GOLD,
            runtime.availableProcessors()
            ));
        src.sendMessage(Text.of(
            TextColors.YELLOW,
            "Free Memory",
            TextColors.GRAY,
            ": ",
            TextColors.GOLD,
            runtime.freeMemory()
        ));
        src.sendMessage(Text.of(
            TextColors.YELLOW,
            "Total Memory",
            TextColors.GRAY,
            ": ",
            TextColors.GOLD,
            runtime.totalMemory()
        ));
        src.sendMessage(Text.of(
            TextColors.YELLOW,
            "Max Memory",
            TextColors.GRAY,
            ": ",
            TextColors.GOLD,
            formatMemory(runtime.maxMemory())
        ));

        return CommandResult.success();
    }

    /** Calculate the bytes to a human friendly format */
    private Text formatMemory(long bytes) {
        return Text.of(bytes);
    }
}
