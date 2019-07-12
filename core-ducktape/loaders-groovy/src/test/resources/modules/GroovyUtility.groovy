/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
import net.year4000.utilities.utils.UtilityConstructError

final class GroovyUtility {
    private GroovyUtility() {
        UtilityConstructError.raise()
    }

    static def helloWorld() {
        println 'Hello World!'
    }
}
