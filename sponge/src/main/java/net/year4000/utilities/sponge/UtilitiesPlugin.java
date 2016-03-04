/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.Tokens;
import net.year4000.utilities.ducktape.ModuleManager;
import net.year4000.utilities.sponge.command.FlyCommand;
import net.year4000.utilities.sponge.command.PluginCommand;
import net.year4000.utilities.sponge.command.SystemCommand;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.File;

@Plugin(id = "utilities", name = "Utilities", version = Tokens.FULL_VERSION)
public final class UtilitiesPlugin extends AbstractSpongePlugin {

    /** Get the instance of Utilities */
    public static UtilitiesPlugin get() {
        return instance();
    }

    @Listener
    public void onUtilitiesInit(GameInitializationEvent event) {
        Messages.Factory.inst.get(); // Trigger a download from server now so it can cache it for later
        PluginCommand.register(this);
        FlyCommand.register(this);
        SystemCommand.register(this);

        new ModuleManager().loadAll(new File("~/y4k-git/minecraft/mods/").toPath(), collection -> {
            collection.forEach(clazz -> {
                logger.info(clazz.toString());
            });
        });
    }
}
