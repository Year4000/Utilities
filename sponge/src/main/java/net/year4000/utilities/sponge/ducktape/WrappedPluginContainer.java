/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.sponge.ducktape;

import static com.google.common.base.Preconditions.checkNotNull;

import net.year4000.utilities.ducktape.module.ModuleInfo;
import org.spongepowered.api.plugin.PluginContainer;

import java.util.Optional;

public class WrappedPluginContainer implements PluginContainer {
    private final ModuleInfo moduleInfo;

    public WrappedPluginContainer(Class<?> clazz) {
        this.moduleInfo = new net.year4000.utilities.ducktape.module.internal.ModuleInfo(checkNotNull(clazz, "clazz"));
    }

    @Override
    public String getId() {
        return this.moduleInfo.getId();
    }

    @Override
    public String getName() {
        return this.moduleInfo.getId();
    }

    @Override
    public Optional<String> getVersion() {
        return Optional.empty();
    }

    @Override
    public Optional<Object> getInstance() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return this.moduleInfo.toString();
    }
}
