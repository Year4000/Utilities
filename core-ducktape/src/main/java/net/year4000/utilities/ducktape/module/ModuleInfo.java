/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module;

import net.year4000.utilities.value.Value;

public interface ModuleInfo extends Loader, Enabler {

    /** Get the module handle of the module */
    Value<ModuleHandle> getHandle();

    /** Get the id of this module */
    String getId();

    /** Get the current phase of the module */
    Phase getPhase();

    /** The current phase of this module */
    enum Phase {
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
