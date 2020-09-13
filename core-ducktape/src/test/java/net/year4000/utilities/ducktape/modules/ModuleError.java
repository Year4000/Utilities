/*
 * Copyright 2020 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.modules;

import net.year4000.utilities.ducktape.module.Load;
import net.year4000.utilities.ducktape.module.Module;

@Module(id = "error")
public class ModuleError {

    @Load
    public void load() {
        throw new RuntimeException("error");
    }
}
