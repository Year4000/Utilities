package net.year4000.ducktape;

import net.year4000.ducktape.core.module.AbstractModule;

import java.io.File;

public class TesterModule extends AbstractModule {
    @Override
    public File getDataFolder() {
        throw new RuntimeException("Can't get data folder.");
    }

    @Override
    public void registerCommand(Class<?> clazz) {
        throw new RuntimeException("Can't register command.");
    }
}
