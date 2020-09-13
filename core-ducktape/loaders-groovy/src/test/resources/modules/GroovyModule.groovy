/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
import net.year4000.utilities.ducktape.module.Load
import net.year4000.utilities.ducktape.module.Module

@Module(id = "groovy")
class GroovyModule {

    @Load
    def load() {
        println 'Groovy Module load()'
        GroovyUtility.helloWorld()
    }
}
