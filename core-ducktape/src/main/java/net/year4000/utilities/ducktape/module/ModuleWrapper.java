/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape.module;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.ducktape.module.internal.ModuleInfo;
import net.year4000.utilities.ducktape.module.internal.ModuleInvocationHandler;

public class ModuleWrapper implements Loader, Enabler {
    private ModuleInfo moduleInfo;
    private Object proxyInstance;

    /** Create the module wrapper that creates the module instance and prep it for other things */
    public ModuleWrapper(ModuleInfo moduleInfo, Object instance) {
        this.moduleInfo = Conditions.nonNull(moduleInfo, "moduleInfo must exists");
        this.proxyInstance = ModuleInvocationHandler.createProxy(moduleInfo.getModuleClass(), Conditions.nonNull(instance, "instance must exists"));
        this.moduleInfo.setHandle((ModuleHandle) this.proxyInstance);
    }

    /** Load the module and call the method with @Load */
    @Override
    public void load() {
        this.moduleInfo.load();
        if (this.proxyInstance instanceof Loader) {
            ((Loader) this.proxyInstance).load();
        }
    }

    /** Enable the module and call the method with @Enable */
    @Override
    public void enable() {
        this.moduleInfo.enable();
        if (this.proxyInstance instanceof Enabler) {
            ((Enabler) this.proxyInstance).enable();
        }
    }

    @Override
    public String toString() {
        return String.format("ModuleWrapper{info=%s, proxy=%s}", this.moduleInfo, this.proxyInstance);
    }
}
