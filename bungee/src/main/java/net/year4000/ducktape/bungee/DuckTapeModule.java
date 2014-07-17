package net.year4000.ducktape.bungee;

import com.ewized.utilities.bungee.util.MessageUtil;
import com.google.common.base.Joiner;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.year4000.ducktape.bungee.module.BungeeModule;
import net.year4000.ducktape.core.module.ModuleInfo;

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

        sender.sendMessage(MessageUtil.makeMessage(text + (modules().size() == 0 ? plugins : joiner.join(plugins, modules))));
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