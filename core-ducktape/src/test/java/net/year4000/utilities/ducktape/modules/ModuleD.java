package net.year4000.utilities.ducktape.modules;

import com.google.inject.Inject;
import net.year4000.utilities.ducktape.module.Module;

@Module(id = "d")
public class ModuleD {
    private @Inject ModuleB moduleB;
    private @Inject ModuleC moduleC;

    public ModuleD() {
        System.out.println("ModuleD Constructor");
    }
}
