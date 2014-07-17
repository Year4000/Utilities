package net.year4000.ducktape.bukkit;

import com.google.common.base.Joiner;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import net.year4000.ducktape.bukkit.module.BukkitModule;
import net.year4000.ducktape.core.module.ModuleInfo;
import org.bukkit.command.CommandSender;

@ModuleInfo(
    name = "DuckTape",
    version = "${pom.version}-${git.commit.id.abbrev}",
    description = "DuckTape Core Module handles everything internal.",
    authors = {"Year4000"}
)
public final class DuckTapeModule extends BukkitModule {

    public void enabled() {
        registerCommand(DuckTapeModule.class);
    }

    @Command(
        aliases = {"plugins", "comp", "components", "modules"},
        desc = "Show all loaded modules and plugins"
    )
    public static void modules(CommandContext args, CommandSender sender) throws CommandException {
        sender.sendMessage(Joiner.on(", ").join(DuckTape.get().getModules().getLoadedClasses().keySet()));
    }
}
