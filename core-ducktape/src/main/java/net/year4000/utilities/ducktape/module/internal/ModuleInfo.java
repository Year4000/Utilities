/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module.internal;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.annotations.Nullable;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.ModuleHandle;
import net.year4000.utilities.value.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** The module info class is for the implementation of it's interface */
public final class ModuleInfo implements net.year4000.utilities.ducktape.module.ModuleInfo {
    private final Logger logger = LogManager.getLogger("utilities/ducktape");
    private Phase phase = Phase.CONSTRUCTING;
    private Class<?> moduleClass;
    private Module annotation;
    private ModuleHandle handle;

    public ModuleInfo(Class<?> moduleClass) {
        this.moduleClass = Conditions.nonNull(moduleClass, "module class must not be empty");
        Conditions.condition(moduleClass.isAnnotationPresent(Module.class), "the module class must have the @Module annotation");
        this.annotation = moduleClass.getAnnotation(Module.class);
    }

    /** This will set the handle of the module info */
    public void setHandle(@Nullable ModuleHandle handle) {
        this.handle = handle;
    }

    /** Returns the handle for the module this will be null until the module has been loaded */
    public Value<ModuleHandle> getHandle() {
        return Value.of(handle);
    }

    /** Get the current phase the module is on */
    public Phase getPhase() {
        return this.phase;
    }

    /** Get the module class */
    public Class<?> getModuleClass() {
        return this.moduleClass;
    }

    /** Get the id of the module */
    @Override
    public String getId() {
        return this.annotation.id().toLowerCase();
    }

    /** Called when the module class is being constructed */
    public void constructing() {
        logger.info("Constructing: {}", this);
    }

    /** Called when the module is being loaded*/
    @Override
    public void load() {
        logger.info("Loading: {}", this);
        this.phase = Phase.LOADED;
    }

    /** Called when the module is being enabled */
    @Override
    public void enable() {
        logger.info("Enabling: {}", this);
        this.phase = Phase.ENABLED;
    }

    @Override
    public String toString() {
        return String.format("ModuleInfo{id=%s, class=%s}", this.getId(), this.moduleClass);
    }

    @Override
    public int hashCode() {
        return this.getId().hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ModuleInfo && this.getId().equals(((ModuleInfo) other).getId());
    }
}
