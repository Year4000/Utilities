/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.utils;

/** A error that states utility classes should never be constructed */
public class UtilityConstructError extends Error {

    /** Provide simple error message */
    public UtilityConstructError() {
        super("This utility class can not be constructed");
    }

    /** Simple helper method to raise the error */
    public static void raise() throws Error {
        throw new UtilityConstructError();
    }
}
