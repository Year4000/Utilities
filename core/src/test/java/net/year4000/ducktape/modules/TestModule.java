package net.year4000.ducktape.modules;

import lombok.extern.java.Log;
import net.year4000.ducktape.TesterModule;
import net.year4000.ducktape.core.module.AbstractModule;
import net.year4000.ducktape.core.module.ModuleInfo;

import java.io.File;

@ModuleInfo(
    name = "Test Module",
    version = "1.0.0",
    description = "A test module to test the module loading system.",
    authors = {"Year4000"}
)
@Log
public class TestModule extends TesterModule {
    @Override
    public void load() {
        log.info("Loaded");
    }

    @Override
    public void enable() {
        log.info("Enabled");
    }

    @Override
    public void disable() {
        log.info("Disabled");
    }
}
