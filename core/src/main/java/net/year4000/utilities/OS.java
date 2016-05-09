/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

/** An enum of the current OS of the running JVM */
public enum OS {
    LINUX,
    OSX,
    WINDOWS,
    ;

    /** Detect the current OS of the running JVM */
    public static OS detect() throws IllegalStateException {
        String name = System.getProperty("os.name", "unknown").toLowerCase();

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
