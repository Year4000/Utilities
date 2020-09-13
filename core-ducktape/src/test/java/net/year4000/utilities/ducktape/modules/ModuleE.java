/*
 * Copyright 2020 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.modules;

import net.year4000.utilities.ducktape.module.Enable;
import net.year4000.utilities.ducktape.module.Load;
import net.year4000.utilities.ducktape.module.Module;

@Module(id = "e")
public class ModuleE {
    @Load
    public void load() {
        System.out.println("ModuleE loader");
    }

    @Enable
    public void enable() {
        System.out.println("ModuleE enabler");
    }
}
