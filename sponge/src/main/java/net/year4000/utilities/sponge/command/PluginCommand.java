/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.command;

import com.google.common.collect.Lists;
import net.year4000.utilities.Mappers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.util.List;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.*;
import static net.year4000.utilities.sponge.Messages.*;


public final class PluginCommand implements CommandExecutor {
    private static final String[] ALIAS = {"plugins", "pl"};
    private static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
        .description(Text.of("View the running plugins on this server."))
        .permission("utilities.command.plugins")
        .executor(new PluginCommand())
        .build();

    /** Register this command with the manager */
    public static void register(Object object) {
        Sponge.getCommandManager().register(object, COMMAND_SPEC, ALIAS);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<PluginContainer> collection = plugins();
        int size = collection.size();
        Text prefix = Text.of(YELLOW, CMD_PLUGINS.get(src));
        Text count = Text.of(GRAY, " (", YELLOW, collection.size(), GRAY, ")", DARK_GRAY, ": ");
        List<Text> plugins = Lists.newArrayList(prefix, count);

        for (int i = 0; i < size; i++) {
            plugins.add(toText(collection.get(i), src));

            if (i < size - 1) {
                plugins.add(Text.of(YELLOW, ", "));
            }
        }

        src.sendMessage(Text.join(plugins));
        return CommandResult.success();
    }

    /** Convert the SimpleContainer to a pretty text */
    public Text toText(PluginContainer plugin, CommandSource src) {
        List<Text> texts = Lists.newArrayList();
        String prefix = plugin.getInstance()
            .map(Mappers.object())
            .orElseGet(Object::new)
            .getClass()
            .getName();
        Text display = Text.of(GREEN, plugin.getName());
        Text version = Text.of(AQUA, CMD_VERSION.get(src), DARK_GRAY, ": ", GREEN, plugin.getVersion().orElse("unknown"));
        String[] packages = prefix.split("\\.", prefix.split("\\.").length - 1);

        for (String parts : packages) {
            texts.add(Text.of(GRAY, parts.charAt(0), DARK_GRAY, "."));
        }

        texts.add(Text.of(GREEN, prefix.substring(prefix.lastIndexOf(".") + 1)));
        return Text.join(texts).toBuilder()
            .onHover(TextActions.showText(Text.join(display, Text.NEW_LINE, version)))
            .build();
    }

    /** Get the collection of active plugins */
    public List<PluginContainer> plugins() {
        return Sponge.getPluginManager().getPlugins().stream()
            .filter(plugin -> plugin.getInstance().isPresent())
            .collect(Collectors.toList());
    }
}
