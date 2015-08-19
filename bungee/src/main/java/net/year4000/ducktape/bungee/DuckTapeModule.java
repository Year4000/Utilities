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

package net.year4000.ducktape.bungee;

import com.google.common.base.Joiner;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.year4000.ducktape.bungee.module.BungeeModule;
import net.year4000.ducktape.module.ModuleInfo;
import net.year4000.utilities.bungee.MessageUtil;
import net.year4000.utilities.bungee.commands.Command;
import net.year4000.utilities.bungee.commands.CommandContext;
import net.year4000.utilities.bungee.commands.CommandException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ModuleInfo(
    name = "DuckTape",
    version = "internal",
    description = "DuckTape Core Module handles everything internal.",
    authors = {"Year4000"}
)
@SuppressWarnings("unused")
public final class DuckTapeModule extends BungeeModule {
    private static final Joiner joiner = Joiner.on("&e, &7");
    private static final Joiner pack = Joiner.on("&8.&7");

    public void enable() {
        registerCommand(DuckTapeModule.class);
    }

    @Command(
        aliases = {"gplugins", "gpl", "gcomp", "gcomponents", "gmodules"},
        desc = "Show all loaded modules and plugins"
    )
    public static void modules(CommandContext args, CommandSender sender) throws CommandException {
        String text = new Message(sender).get("ducktape.modules", plugins().size(), modules().size()) + " ";

        String plugins = joiner.join(plugins().stream().map(plugin -> name(plugin, (plugin.isEnabled() ? "&a" : "&4"))).toArray());
        String modules = joiner.join(modules().stream().map(module -> name(module, (module.isEnabled() ? "&b" : "&c"))).toArray());

        sender.sendMessage(MessageUtil.message(text + (modules().size() == 0 ? plugins : joiner.join(plugins, modules))));
    }

    private static String name(SimpleAddon info, String prefix) {
        String name = info.getObject().getClass().getName();
        String[] packages = name.split("\\.", name.split("\\.").length - 1);

        String first = pack.join(Arrays.asList(packages).stream().map(p -> p.substring(0, 1)).collect(Collectors.toList()));

        return pack.join(first, prefix) + info.getName();
    }

    private static Set<SimpleAddon> plugins() {
        return ProxyServer.getInstance().getPluginManager().getPlugins().stream().map(plugin -> new SimpleAddon(
            true,
            plugin.getDescription().getName(),
            plugin.getDescription().getVersion(),
            Arrays.asList(plugin.getDescription().getAuthor()),
            plugin
        )).collect(Collectors.toSet());
    }

    private static Set<SimpleAddon> modules() {
        Set<SimpleAddon> newList = new HashSet<>();

        DuckTape.get().getModules().getLoadedClasses().keySet().forEach(info -> {
            if (!info.name().equalsIgnoreCase("DuckTape")) {
                newList.add(new SimpleAddon(
                    true,
                    info.name(),
                    info.version(),
                    Arrays.asList(info.authors()),
                    DuckTape.get().getModules().getModule(info.name())
                ));
            }
        });

        return newList;
    }

    @Data
    @Setter(AccessLevel.NONE)
    @AllArgsConstructor
    private static class SimpleAddon {
        private boolean enabled;
        private String name;
        private String version;
        private List<String> authors;
        private Object object;
    }
}