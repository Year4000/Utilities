package net.year4000.utilities.ducktape;

/** This is the interface that will handle the basic Ducktape module loading systems */
public interface Ducktape {
    static DucktapeManager.DucktapeManagerBuilder builder() {
        return DucktapeManager.builder();
    }

    /** When called this will load all modules */
    void loadAll();
}
