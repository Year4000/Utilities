/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
import net.year4000.utilities.ducktape.module.Enable
import net.year4000.utilities.ducktape.module.Module

@Module(id = "another-module")
class AnotherGroovyModule {
    @Enable
    def enable() {
        println 'Groovy Module load()'
        GroovyUtility.helloWorld()
    }
}
