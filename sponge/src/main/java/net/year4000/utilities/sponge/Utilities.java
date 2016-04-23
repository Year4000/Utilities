/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.Tokens;
import net.year4000.utilities.sponge.command.ChunksCommand;
import net.year4000.utilities.sponge.command.PluginCommand;
import net.year4000.utilities.sponge.command.SystemCommand;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(
    id = "net.year4000.utilities",
    name = "Utilities",
    version = Tokens.FULL_VERSION,
    description = "A collection of API's to make coding faster and simpler.",
    url = "https://www.year4000.net/",
    authors = {"ewized"}
)
public final class Utilities extends AbstractSpongePlugin {

    /** Get the instance of Utilities */
    public static Utilities get() {
        return instance();
    }

    @Listener
    public void onUtilitiesInit(GameInitializationEvent event) {
        Messages.Factory.inst.get(); // Trigger a download from server now so it can cache it for later
        PluginCommand.register(this);
        // FlyCommand.register(this); todo disable, should be in drip
        SystemCommand.register(this);
        ChunksCommand.register(this);
    }
}
