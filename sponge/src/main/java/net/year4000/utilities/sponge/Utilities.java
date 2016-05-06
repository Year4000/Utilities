/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.Tokens;
import net.year4000.utilities.sponge.command.PluginCommand;
import net.year4000.utilities.sponge.command.SystemCommand;
import net.year4000.utilities.sponge.ducktape.SpongeModuleManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.function.BiConsumer;

@Plugin(
    id = "utilities",
    name = "Utilities",
    version = Tokens.FULL_VERSION,
    description = "A collection of API's to make coding faster and simpler.",
    url = "https://www.year4000.net/",
    authors = {"ewized"}
)
public final class Utilities extends AbstractSpongePlugin {
    private final SpongeModuleManager moduleManager = new SpongeModuleManager();

    static {
        ErrorReporter.setUncaughtExceptionHandler();
    }

    /** Get the instance of Utilities */
    public static Utilities get() {
        return instance(Utilities.class);
    }

    @Listener
    public void onConstruct(GameConstructionEvent event) {
        moduleManager.injectModules();
    }

    @Listener
    public void onUtilitiesInit(GameInitializationEvent event) {
        Messages.Factory.inst.get(); // Trigger a download from server now so it can cache it for later
        PluginCommand.register(this);
        // FlyCommand.register(this); todo disable, should be in drip
        SystemCommand.register(this);
    }

    /** Internal testing method */
    private void test(String cmd, final BiConsumer<CommandSource, CommandContext> consumer) {
        Sponge.getCommandManager().register(this, CommandSpec.builder().executor((src, args) -> {
            consumer.accept(src, args);
            return CommandResult.success();
        }).build(), cmd);
    }

    public static void log(Object object, Object... args) {
        log(get(), object, args);
    }

    public static void debug(Object object, Object... args) {
        debug(get(), object, args);
    }
}
