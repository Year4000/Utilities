/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.sponge;

import net.year4000.utilities.Tokens;
import net.year4000.utilities.sponge.command.ChunksCommand;
import net.year4000.utilities.sponge.command.FlyCommand;
import net.year4000.utilities.sponge.command.PluginCommand;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "utilities", name = "Utilities", version = Tokens.FULL_VERSION)
public final class UtilitiesPlugin extends AbstractSpongePlugin {

    /** Get the instance of Utilities */
    public static UtilitiesPlugin get() {
        return instance();
    }

    @Listener
    public void onUtilitiesInit(GameInitializationEvent event) {
        PluginCommand.register(this);
        FlyCommand.register(this);
        ChunksCommand.register(this);
    }
}
