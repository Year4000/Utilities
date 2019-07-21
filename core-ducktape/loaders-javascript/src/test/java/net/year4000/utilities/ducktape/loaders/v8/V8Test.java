package net.year4000.utilities.ducktape.loaders.v8;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

public class V8Test {

    @Before
    public void loadLib() {
        Path path = Paths.get("build/libs/ducktape/shared/libducktape.dylib");
        System.load(path.toAbsolutePath().toString());
    }

    @Test
    public void test() {
        V8 v8 = new V8();
        v8.test();
    }
}
