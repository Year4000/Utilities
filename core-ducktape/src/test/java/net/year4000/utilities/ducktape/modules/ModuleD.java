/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.modules;

import com.google.inject.Inject;
import net.year4000.utilities.ducktape.module.Module;

@Module(id = "d")
public class ModuleD {
    @Inject private ModuleB moduleB;
    @Inject private ModuleC moduleC;

    public ModuleD() {
        System.out.println("ModuleD Constructor");
    }
}
