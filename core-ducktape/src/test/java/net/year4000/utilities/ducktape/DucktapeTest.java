package net.year4000.utilities.ducktape;

import com.google.common.collect.Lists;
import net.year4000.utilities.ducktape.modules.ModuleD;
import net.year4000.utilities.ducktape.modules.ModuleA;
import net.year4000.utilities.ducktape.modules.ModuleB;
import net.year4000.utilities.ducktape.modules.ModuleC;
import org.junit.Test;

public class DucktapeTest {
    @Test
    public void loadModules() {
        Ducktape ducktape = Ducktape.builder().addLoader(path -> {
            return Lists.newArrayList(ModuleA.class, ModuleB.class, ModuleC.class, ModuleD.class);
            //return Lists.newArrayList(ModuleB.class, ModuleC.class, ModuleD.class, ModuleA.class);
        }).build();

        ducktape.loadAll();
    }
}
