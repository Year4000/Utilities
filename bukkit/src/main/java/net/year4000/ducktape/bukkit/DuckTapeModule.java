package net.year4000.ducktape.bukkit;

import com.ewized.utilities.bukkit.util.MessageUtil;
import com.google.common.base.Joiner;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import net.year4000.ducktape.bukkit.module.BukkitModule;
import net.year4000.ducktape.core.module.ModuleInfo;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

@ModuleInfo(
    name = "DuckTape",
    version = "internal",
    description = "DuckTape Core Module handles everything internal.",
    authors = {"Year4000"}
)
public final class DuckTapeModule extends BukkitModule {
    private static final Joiner joiner = Joiner.on("&e, &7");
    private static final Joiner pack = Joiner.on("&8.&7");

    public void enable() {
        registerCommand(DuckTapeModule.class);
    }

    @Command(
        aliases = {"plugins", "pl", "comp", "components", "modules"},
        desc = "Show all loaded modules and plugins"
    )
    public static void modules(CommandContext args, CommandSender sender) throws CommandException {
        String text = new Message(sender).get("ducktape.modules", plugins().size(), modules().size()) + " ";

        String plugins = joiner.join(plugins().stream().map(plugin -> name(plugin, (plugin.isEnabled() ? "&a" : "&4"))).toArray());
        String modules = joiner.join(modules().stream().map(module -> name(module, (module.isEnabled() ? "&b" : "&c"))).toArray());

        sender.sendMessage(MessageUtil.message(text + (modules().size() == 0 ? plugins: joiner.join(plugins, modules))));
    }

    private static String name(SimpleAddon info, String prefix) {
        String name = info.getObject().getClass().getName();
        String[] packages = name.split("\\.", name.split("\\.").length - 1);

        String first = pack.join(Arrays.asList(packages).stream().map(p -> p.substring(0, 1)).collect(Collectors.toList()));

        return pack.join(first, prefix) + info.getName();
    }

    private static Set<SimpleAddon> plugins() {
        Set<SimpleAddon> newList = new HashSet<>();

        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            newList.add(new SimpleAddon(
                plugin.isEnabled(),
                plugin.getName(),
                plugin.getDescription().getVersion(),
                plugin.getDescription().getAuthors(),
                plugin
            ));
        }

        return newList;
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
