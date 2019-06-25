/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import com.google.common.collect.ImmutableList;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.ModuleInfo;
import net.year4000.utilities.value.Value;

/** This is the interface that will handle the basic Ducktape module loading systems */
public interface Ducktape {
    /** Create the system using the default Ducktape manager */
    static DucktapeManager.DucktapeManagerBuilder builder() {
        return DucktapeManager.builder();
    }

    /** This will init the Ducktape systems */
    void init() throws ModuleInitException;

    /** Get the module from the class, avoid this call and prefer the id method over this one */
    default Value<ModuleInfo> getModule(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Module.class)) {
            return getModule(clazz.getAnnotation(Module.class).id().toLowerCase());
        }
        return Value.empty();
    }

    /** Try and get the module info from the given id */
    default Value<ModuleInfo> getModule(String id) {
        id = Conditions.nonNullOrEmpty(id.toLowerCase(), "the id of the module must not be null");
        for (ModuleInfo module : getModules()) {
            if (id.equals(module.getId())) {
                return Value.of(module);
            }
        }
        return Value.empty();
    }

    /** Get all modules that are currently in the system, the order is they constructed and load order */
    ImmutableList<ModuleInfo> getModules();
}
