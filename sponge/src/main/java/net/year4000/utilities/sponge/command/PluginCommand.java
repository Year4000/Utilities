/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.command;

import com.google.common.collect.Lists;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.stream.Collectors;

import static org.spongepowered.api.text.format.TextColors.*;

public final class PluginCommand implements CommandExecutor {
    public static final String[] ALIAS = {"plugins", "pl"};
    public static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
        .description(Text.of("View the running plugins on this server."))
        .executor(new PluginCommand())
        .build();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<SimpleContainer> collection = plugins();
        int size = collection.size();
        Text prefix = Text.of(YELLOW, "Plugins ");
        Text count = Text.of(GRAY, "(", YELLOW, collection.size(), GRAY, ")", DARK_GRAY, ": ");
        List<Text> plugins = Lists.newArrayList(prefix, count);

        for (int i = 0; i < size; i++) {
            plugins.add(toText(collection.get(i)));

            if (i < size - 1) {
                plugins.add(Text.of(DARK_GRAY, ", "));
            }
        }

        src.sendMessage(Text.join(plugins));
        return CommandResult.success();
    }

    /** Convert the SimpleContainer to a pretty text */
    public Text toText(SimpleContainer container) {
        List<Text> texts = Lists.newArrayList();
        String prefix = String.valueOf(container.clazz.getPackage());
        String[] packages = prefix.split("\\.", prefix.split("\\.").length - 1);

        for (String parts : packages) {
            texts.add(Text.of(AQUA, parts.charAt(0), GRAY, "."));
        }

        texts.add(Text.of(GREEN, container.name));
        return Text.join(texts);
    }

    /** Get the collection of active plugins */
    public List<SimpleContainer> plugins() {
        return Sponge.getPluginManager().getPlugins().stream()
            .filter(plugin -> plugin.getInstance().isPresent())
            .map(SimpleContainer::new)
            .collect(Collectors.toList());
    }

    /** A Simple Container to map the Plugin Container */
    private class SimpleContainer {
        Class<?> clazz;
        String name;

        SimpleContainer(PluginContainer container) {
            this.clazz = container.getInstance().orElseThrow(IllegalStateException::new).getClass();
            this.name = clazz.getName();
        }
    }
}
