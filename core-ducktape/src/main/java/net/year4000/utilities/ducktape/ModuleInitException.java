/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.ducktape.module.ModuleInfo;

/** This exception is thrown when the system has failed to initialization */
public class ModuleInitException extends RuntimeException {
    private final ModuleInfo.Phase phase;

    public ModuleInitException(ModuleInfo.Phase phase, Throwable cause) {
        this(phase, cause.getMessage(), cause);
    }

    public ModuleInitException(ModuleInfo.Phase phase, String message, Throwable cause) {
        super(message, cause);
        this.phase = Conditions.nonNull(phase, "The phase when system was init must be provided");
    }

    /** The phase the system was at when an exception was thrown */
    public ModuleInfo.Phase getPhase() {
        return this.phase;
    }
}
