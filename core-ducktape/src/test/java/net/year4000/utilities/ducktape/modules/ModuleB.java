package net.year4000.utilities.ducktape.modules;

import net.year4000.utilities.ducktape.module.Load;
import net.year4000.utilities.ducktape.module.Module;

@Module(id = "b")
public class ModuleB {
    public ModuleB() {
        System.out.println("ModuleB Constructor");
    }

    @Load
    public void loasdas() {
        System.out.println("ModuleB loader");
    }
}
