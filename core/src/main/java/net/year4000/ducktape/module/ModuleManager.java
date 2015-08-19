package net.year4000.ducktape.module;

import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.api.events.ModuleLoadEvent;
import net.year4000.utilities.LogUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class ModuleManager<T extends AbstractModule> {
    /** Raw Class Files */
    protected final Set<Class<? extends T>> classes = new HashSet<>();

    /** Enabled Modules */
    @Getter
    protected final Map<ModuleInfo, T> loadedClasses = new ConcurrentHashMap<>();

    /** LogUtil */
    protected final LogUtil log;

    /** Guava's EventBus */
    @Getter
    protected final EventBus eventBus;

    /** Init LogUtil with its own logger */
    public ModuleManager() {
        this(new LogUtil());
    }

    /** Init LogUtil with specific logger */
    public ModuleManager(Logger logger) {
        this(new LogUtil(logger));
    }

    /** Init LogUtil with specific LogUtil */
    public ModuleManager(LogUtil log) {
        this.log = log;
        this.eventBus = new EventBus(log.getLogger().getName());
    }

    /** Register an event */
    public void registerEvent(Object event) {
        eventBus.register(event);
    }

    /** Add a class to the module */
    public synchronized void add(Class<? extends T> clazz) {
        log.debug("Adding: " + clazz.getName());
        classes.add(clazz);
    }

    /** Remove a class to the module */
    public synchronized void remove(Class<? extends T> clazz) {
        log.debug("Removing: " + clazz.getName());
        classes.remove(clazz);
    }

    /** Load each module from a set of classes */
    public Set<T> loadModules(Set<Class<?>> classes) {
        return classes.stream().map(this::loadModule).collect(Collectors.toSet());
    }

    /** Load each module */
    public void loadModules() {
        classes.forEach(this::loadModule);
    }

    /** Load a module's class */
    public synchronized T loadModule(Class<?> clazz) {
        T module;

        try {
            //log.log(clazz.toString());
            InitModule init = initModule(clazz);

            module = init.getModule();
            ModuleInfo info = init.getInfo();
            //log.log(info.toString());

            eventBus.post(new ModuleLoadEvent(info, module));

            module.load();

            loadedClasses.put(info, module);

            if (info.version().equals("internal")) {
                log.log("Module " + info.name() + " loaded.");
            }
            else {
                log.log("Module " + info.name() + " version " + info.version() + " loaded.");
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return module;
    }

    /** Enable each module */
    public void enableModules() {
        loadedClasses.forEach((info, module) -> enableModule(info.name()));
    }

    public synchronized void enableModule(String name) {
        T module = getModule(name);
        ModuleInfo info = getModuleInfo(name);

        try {
            module.setEnabled(true);

            eventBus.post(new ModuleEnableEvent(info, module));

            module.enable();

            if (info.version().equals("internal")) {
                log.log("Module " + info.name() + " enabled.");
            }
            else {
                log.log("Module " + info.name() + " version " + info.version() + " enabled.");
            }
        } catch (Exception e) {
            log.log("There was an exception while enabling: " + info.name());
            module.setEnabled(false);
        }
    }

    /** Disable each module */
    public void disableModules() {
        loadedClasses.forEach((info, module) -> disableModule(info.name()));
    }

    /** Disable one module by its name */
    public synchronized void disableModule(String name) {
        T module = getModule(name);
        ModuleInfo info = getModuleInfo(name);

        try {
            module.setEnabled(false);

            eventBus.post(new ModuleDisableEvent(info, module));

            module.disable();

            if (info.version().equals("internal")) {
                log.log("Module " + info.name() + " disabled.");
            }
            else {
                log.log("Module " + info.name() + " version " + info.version() + " disabled.");
            }
        } catch (Exception e) {
            log.log("There was an exception while disabling: " + info.name());
        }
    }

    /** Get module info by its name */
    public ModuleInfo getModuleInfo(String name) {
        for (ModuleInfo info : loadedClasses.keySet()) {
            if (info.name().equalsIgnoreCase(name)) {
                return info;
            }
        }

        return null;
    }

    /** Get a module by its name */
    public T getModule(String name) {
        for (ModuleInfo info : loadedClasses.keySet()) {
            if (info.name().equalsIgnoreCase(name)) {
                return loadedClasses.get(info);
            }
        }

        return null;
    }

    public static boolean isModuleClass(Class<?> clazz) {
        return clazz != null && AbstractModule.class.isAssignableFrom(clazz);
    }

    /** Init the instance */
    @SuppressWarnings("unchecked")
    protected InitModule initModule(Class<?> clazz) throws Throwable {
        if (!isModuleClass(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + ": Not A Module Class");
        }

        return new InitModule(clazz.getAnnotation(ModuleInfo.class), (T) clazz.newInstance());
    }

    @Data
    @AllArgsConstructor
    protected class InitModule {
        ModuleInfo info;
        T module;
    }
}
