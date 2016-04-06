package net.year4000.utilities;

import net.year4000.utilities.value.Value;

/** An enum of the current OS of the running JVM */
public enum OS {
    LINUX,
    OSX,
    WINDOWS,
    ;

    /** Detect the current OS of the running JVM */
    public static OS detect() throws IllegalStateException {
        String name = Value.of(System.getProperty("os.name")).getOrElse("").toLowerCase();

        if (name.contains("linux")) return LINUX;
        else if (name.contains("mac") || name.contains("osx")) return OSX;
        else if (name.contains("windows")) return WINDOWS;

        throw new IllegalStateException("Could not detect os, report this issue.");
    }

    /** Is the JVM running under Linux */
    public boolean isLinux() {
        return this == LINUX;
    }

    /** Is the JVM running under OSX */
    public boolean isMacintosh() {
        return this == OSX;
    }

    /** Is the JVM running under windows */
    public boolean isWindows() {
        return this == WINDOWS;
    }
}