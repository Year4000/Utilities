package net.year4000.ducktape.core.module;

import com.ewized.utilities.core.util.LogUtil;
import com.google.common.eventbus.EventBus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.year4000.ducktape.api.events.ModuleDisableEvent;
import net.year4000.ducktape.api.events.ModuleEnableEvent;
import net.year4000.ducktape.api.events.ModuleLoadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class ModuleManager<T extends AbstractModule> {
    /** Raw Class Files */
    protected final List<Class<? extends T>> classes = new ArrayList<>();

    /** Enabled Modules */
    @Getter
    protected final Map<ModuleInfo, T> loadedClasses = new ConcurrentHashMap<>();

    /** Guava's EventBus */
    protected final EventBus eventBus = new EventBus();

    /** LogUtil */
    protected final LogUtil log;

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

    /** Load each module */
    public void loadModules() {
        classes.forEach(clazz -> {
            try {
                //log.log(clazz.toString());
                InitModule init = initModule(clazz);

                T module = (T) init.getModule();
                ModuleInfo info = init.getInfo();
                //log.log(info.toString());

                module.load();

                loadedClasses.put(info, module);

                eventBus.post(new ModuleLoadEvent(info, module));

                log.log(info.name() + " version " + info.version() + " loaded.");
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    /** Enable each module */
    public void enableModules() {
        loadedClasses.forEach((info, module) -> {
            module.enable();

            eventBus.post(new ModuleEnableEvent(info, module));

            log.log(info.name() + " version " + info.version() + " enabled.");
        });
    }

    /** Disable each module */
    public void disableModules() {
        loadedClasses.forEach((info, module) -> {
            module.disable();

            eventBus.post(new ModuleDisableEvent(info, module));

            log.log(info.name() + " version " + info.version() + " disabled.");
        });
    }

    public static boolean isModuleClass(Class<?> clazz) {
        return clazz != null && AbstractModule.class.isAssignableFrom(clazz);
    }

    /** Init the instance */
    @SuppressWarnings("unchecked")
    protected InitModule initModule(Class<? extends T> clazz) throws Throwable {
        if (!isModuleClass(clazz)) {
            throw new IllegalArgumentException(clazz.getName() + ": Not A Module Class");
        }

        return new InitModule(clazz.getAnnotation(ModuleInfo.class), clazz.newInstance());
    }

    @Data
    @AllArgsConstructor
    protected class InitModule {
        ModuleInfo info;
        T module;
    }
}
