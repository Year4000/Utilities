package net.year4000.utilities.ducktape.loaders;

import org.junit.Test;

import java.util.Collection;

public class V8ModuleLoaderTest {

    @Test
    public void v8Test() {
        V8ModuleLoader moduleLoader = new V8ModuleLoader();
        Collection<Class<?>> classes = moduleLoader.load();
        System.out.println(classes);
    }
}
