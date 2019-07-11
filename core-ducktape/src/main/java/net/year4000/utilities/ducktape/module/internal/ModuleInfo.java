/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module;

import net.year4000.utilities.Conditions;

public final class ModuleInfo implements Loader, Enabler {
    private Phase phase = Phase.CONSTRUCTING;
    private Class<?> moduleClass;
    private Module annotation;

    public ModuleInfo(Class<?> moduleClass) {
        this.moduleClass = Conditions.nonNull(moduleClass, "module class must not be empty");
        Conditions.condition(moduleClass.isAnnotationPresent(Module.class), "the module class must have the @Module annotation");
        this.annotation = moduleClass.getAnnotation(Module.class);
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
    public String getId() {
        return this.annotation.id().toLowerCase();
    }

    /** Called when the module class is being constructed */
    public void constructing() {
        System.out.println("Constructing: " + this);
    }

    /** Called when the module is being loaded*/
    @Override
    public void load() {
        System.out.println("Loading: " + this);
        this.phase = Phase.LOADED;
    }

    /** Called when the module is being enabled */
    @Override
    public void enable() {
        System.out.println("Enabling: " + this);
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

    /** The current phase of this module */
    public enum Phase {
        /** This is a special phase when the classes are being loaded into the system to be constructed by Guice */
        LOADING,
        /** The default phase state as this is created when the mode is constructing */
        CONSTRUCTING,
        /** This is when the module has been loaded, settings have been populated and module specific stuff should be loaded here */
        LOADED,
        /** This is when the module has been enable, at this point all loading has been done and can do what they need */
        ENABLED
        ;

        /** Return true when the phase is constructing */
        public boolean isConstructing() {
            return this == CONSTRUCTING;
        }

        /** Return true when the phase is loaded */
        public boolean isLoaded() {
            return this == LOADED;
        }

        /** Return true with the phase is enabled */
        public boolean isEnabled() {
            return this == ENABLED;
        }
    }
}
