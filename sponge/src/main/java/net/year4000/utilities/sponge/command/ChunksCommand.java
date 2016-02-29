package net.year4000.utilities.sponge.command;

import com.google.common.collect.Lists;
import net.year4000.utilities.sponge.Messages;
import net.year4000.utilities.sponge.UtilitiesPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandPermissionException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.World;

import java.util.List;
import java.util.concurrent.TimeUnit;

/** View the status of the chunks of each world */
public final class ChunksCommand implements CommandExecutor {
    private static final String[] ALIAS = new String[] {"chunks", "memory"};
    private static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
        .child(ChunksUnloadCommand.COMMAND_SPEC, ChunksUnloadCommand.ALIAS)
        .permission("utilities.command.chunks")
        .executor(new ChunksCommand())
        .build();

    /** Register this command with the manager */
    public static void register(Object object) {
        Sponge.getCommandManager().register(object, COMMAND_SPEC, ALIAS);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<Chunk> pendingUnload = Lists.newLinkedList();
        Sponge.getServer().getWorlds().stream()
            .map(World::getLoadedChunks)
            .forEach(iterator -> pendingUnload.addAll(Lists.newLinkedList(iterator)));

        src.sendMessage(Text.of("Chunk Size: ", pendingUnload.size()));
        return CommandResult.success();
    }

    /** Format the text line for the world */
    private Text formatWorld(World world) {
        return Text.EMPTY;
    }

    /** Command to unload all chunks on the server inorder to save memory */
    public static class ChunksUnloadCommand implements CommandExecutor {
        private static final String[] ALIAS = new String[] {"unload"};
        private static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
            .description(Text.of("Unload all the chunks on the server."))
            .permission("utilities.command.chunks.unload")
            .executor(new ChunksUnloadCommand())
            .build();

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            if (Sponge.getScheduler().getTasksByName("utilities-chunk-unload").size() > 0) {
                throw new CommandPermissionException(Text.of(Messages.ERROR, "Command already running..."));
            }

            Task broadcast = Sponge.getScheduler().createTaskBuilder()
                .async()
                .interval(1, TimeUnit.SECONDS)
                .name("utilities-chunk-unload")
                .execute(() -> {
                    Sponge.getServer().getOnlinePlayers().forEach(player -> {
                        player.sendMessage(ChatTypes.ACTION_BAR, Text.of(Messages.NOTICE, "Unloading server chunks, use caution when building."));
                    });
                })
                .submit(UtilitiesPlugin.get());

            Sponge.getScheduler().createTaskBuilder()
                .async()
                .execute(() -> {
                    // Generate the list of chunks
                    List<Chunk> pendingUnload = Lists.newLinkedList();
                    Sponge.getServer().getWorlds().stream()
                        .map(World::getLoadedChunks)
                        .forEach(iterator -> pendingUnload.addAll(Lists.newLinkedList(iterator)));

                    // Unload the chunks
                    try {
                        Thread.sleep(10000);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //pendingUnload.stream().forEach(Chunk::unloadChunk);

                    // Send completion notification
                    broadcast.cancel();
                    Sponge.getServer().getOnlinePlayers().forEach(player -> {
                        player.sendMessage(ChatTypes.ACTION_BAR, Text.of(Messages.SUCCESS, TextColors.GOLD, TextStyles.BOLD, "It's OK, you may resume your work."));
                    });
                })
                .submit(UtilitiesPlugin.get());

            return CommandResult.success();
        }
    }
}
