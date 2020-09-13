/*
 * Copyright 2018 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge.command;

import static net.year4000.utilities.sponge.Messages.CMD_PLUGINS;
import static net.year4000.utilities.sponge.Messages.CMD_ID;
import static net.year4000.utilities.sponge.Messages.CMD_AUTHORS;
import static net.year4000.utilities.sponge.Messages.CMD_AUTHOR;
import static net.year4000.utilities.sponge.Messages.CMD_URL;
import static net.year4000.utilities.sponge.Messages.CMD_CLASS;
import static org.spongepowered.api.text.format.TextColors.AQUA;
import static org.spongepowered.api.text.format.TextColors.DARK_GRAY;
import static org.spongepowered.api.text.format.TextColors.GRAY;
import static org.spongepowered.api.text.format.TextColors.GREEN;
import static org.spongepowered.api.text.format.TextColors.YELLOW;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.year4000.utilities.Mappers;
import net.year4000.utilities.sponge.Utilities;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.action.TextActions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class PluginCommand implements CommandExecutor {
    private static final String[] ALIAS = {"plugins", "pl", "mods"};
    private static final CommandSpec COMMAND_SPEC = CommandSpec.builder()
        .description(Text.of("View the running mods on this server."))
        .permission("utilities.command.mods")
        .executor(new PluginCommand())
        .build();

    @Inject private PluginManager pluginManager;

    /** Register this command with the manager */
    public static void register(Object object, Injector injector) {
        injector.injectMembers(COMMAND_SPEC.getExecutor());
        Sponge.getCommandManager().register(object, COMMAND_SPEC, ALIAS);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {
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
        String prefix = null;
        if (plugin.getInstance().isPresent()) {
            prefix = plugin.getInstance()
                .map(Mappers.object())
                .get()
                .getClass()
                .getName();
        }
        boolean allInfo = src.hasPermission("utilities.command.mods.extras");
        Text base = Text.of(GREEN, plugin.getName(), Text.of(DARK_GRAY, " (", GRAY, plugin.getVersion().orElse("unknown"), DARK_GRAY, ")"));
        if (plugin.getDescription().isPresent()) {
            base = Text.join(base, Text.of(GRAY, Text.NEW_LINE, plugin.getDescription().get()));
        }
        // Show the authors
        if ((allInfo || src.hasPermission("utilities.command.mods.authors")) && plugin.getAuthors().size() > 0) {
            String authorsList = plugin.getAuthors().stream().collect(Collectors.joining(", "));
            Text authorsPrefix = plugin.getAuthors().size() > 1 ? CMD_AUTHORS.get(src) : CMD_AUTHOR.get(src);
            Text authors = Text.of(AQUA, authorsPrefix, DARK_GRAY, ": ", GREEN, authorsList);
            base = Text.join(base, Text.NEW_LINE, authors);
        }
        // Show the website url for this mod
        if ((allInfo || src.hasPermission("utilities.command.mods.url")) && plugin.getUrl().isPresent()) {
            Text className = Text.of(AQUA, CMD_URL.get(src), DARK_GRAY, ": ", GREEN, plugin.getUrl().get());
            base = Text.join(base, Text.NEW_LINE, className);
        }
        // Show the id
        if (allInfo || src.hasPermission("utilities.command.mods.id")) {
            Text id = Text.of(AQUA, CMD_ID.get(src), DARK_GRAY, ": ", GREEN, plugin.getId());
            base = Text.join(base, Text.NEW_LINE, id);
        }
        // Show the main class
        if ((allInfo || src.hasPermission("utilities.command.mods.class")) && prefix != null) {
            Text className = Text.of(AQUA, CMD_CLASS.get(src), DARK_GRAY, ": ", GREEN, prefix);
            base = Text.join(base, Text.NEW_LINE, className);
        }
        // Show the source file of the mod
        if ((allInfo || src.hasPermission("utilities.command.mods.source")) && plugin.getSource().isPresent()) {
            Text className = Text.of(DARK_GRAY, plugin.getSource().get());
            base = Text.join(base, Text.NEW_LINE, className);
        }
        // construct our version of the list
        if (prefix != null) {
            String[] packages = prefix.split("\\.", prefix.split("\\.").length - 1);
            for (String parts : packages) {
                texts.add(Text.of(GRAY, parts.charAt(0), DARK_GRAY, "."));
            }
        }
        texts.add(Text.of(GREEN, plugin.getName().replaceAll(" ", "")));
        // put everything together
        Text.Builder out = Text.join(texts).toBuilder().onHover(TextActions.showText(base));
        plugin.getUrl().ifPresent(url -> out.onShiftClick(TextActions.insertText(url)));
        return out.build();
    }

    /** Get the collection of active plugins */
    public List<PluginContainer> plugins() {
        return ImmutableList.<PluginContainer>builder()
            .addAll(new ArrayList<>(pluginManager.getPlugins()))
            .addAll(Utilities.get().getModuleManager().getWrappedModules())
            .build();
    }
}
