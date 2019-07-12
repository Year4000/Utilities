/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.sponge;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.Tokens;
import net.year4000.utilities.ducktape.loaders.GroovyModuleLoader;
import net.year4000.utilities.sponge.command.FlyCommand;
import net.year4000.utilities.sponge.command.PluginCommand;
import net.year4000.utilities.sponge.command.SystemCommand;
import net.year4000.utilities.sponge.ducktape.SpongeDucktapeManager;
import net.year4000.utilities.sponge.hologram.Holograms;
import net.year4000.utilities.sponge.protocol.Packets;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import java.nio.file.Paths;

@Plugin(
    id = "utilities",
    name = "Utilities",
    version = Tokens.FULL_VERSION,
    description = "A collection of API's to make coding faster and simpler.",
    url = "https://www.year4000.net/",
    authors = {"ewized"}
)
public final class Utilities extends AbstractSpongePlugin {
    private SpongeDucktapeManager moduleManager;

    // Services
    private Packets packets;
    private Holograms holograms;

    // Error Reporter
    static {
        ErrorReporter.setUncaughtExceptionHandler();
    }

    /** The DI injector that was used to inject this plugin */
    @Inject private Injector injector;

    /** Get the instance of Utilities */
    public static Utilities get() {
        return instance(Utilities.class);
    }

    /** Get the module manager */
    public SpongeDucktapeManager getModuleManager() {
        return moduleManager;
    }

    @Listener
    public void onConstruct(GameConstructionEvent event) {
        this.moduleManager = (SpongeDucktapeManager) SpongeDucktapeManager.builder()
            .setInjector(this.injector)
            .addLoader(new GroovyModuleLoader(Paths.get(SpongeDucktapeManager.MOD_PATH)))
            .build();
        this.moduleManager.load();
    }

    @Listener
    public void onUtilitiesPreInit(GamePreInitializationEvent event) {
        packets = setProvider(Packets.class, Packets.manager());
        holograms = setProvider(Holograms.class, Holograms.manager());
        moduleManager.enable();
        moduleManager.registerListeners();
    }

    @Listener
    public void onUtilitiesInit(GameInitializationEvent event) {
        Messages.Factory.inst.get(); // Trigger a download from server now so it can cache it for later
        PluginCommand.register(this, injector);
        FlyCommand.register(this, injector);
        SystemCommand.register(this, injector);
    }

    /** Internal setting of service provider */
    private <T> T setProvider(Class<T> clazz, T instance) {
        Sponge.getServiceManager().setProvider(this, clazz, instance);
        return instance;
    }

    public static void log(Object object, Object... args) {
        log(get(), object, args);
    }

    public static void debug(Object object, Object... args) {
        debug(get(), object, args);
    }
}
