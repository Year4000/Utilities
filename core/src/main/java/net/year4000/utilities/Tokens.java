/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities;

/**
 * Tokens generated from gradle.
 */
public class Tokens {
    /** The version of this gradle project */
    public static final String VERSION = "${version}";

    /** The git hash of this gradle project */
    public static final String GIT_HASH = "${git_hash}";

    /** The full version including git hash of this gradle project */
    public static final String FULL_VERSION = "${version}-${git_hash}";
}