/*
 * Copyright 2020 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.ducktape;

import com.google.common.collect.*;
import com.google.inject.*;
import com.google.inject.Module;
import net.year4000.utilities.Builder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.annotations.Nullable;
import net.year4000.utilities.ducktape.loaders.ModuleLoader;
import net.year4000.utilities.ducktape.module.Enabler;
import net.year4000.utilities.ducktape.module.internal.ModuleInfo;
import net.year4000.utilities.ducktape.module.ModuleWrapper;
import net.year4000.utilities.value.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class DucktapeManager extends AbstractModule implements Ducktape {
    private final Logger logger = LogManager.getLogger("utilities/ducktape");
    /** The set of current loaded modules */
    protected Set<Class<? extends ModuleLoader>> loaded = new LinkedHashSet<>();
    /** A map of all loaders stored by their class */
    protected ClassToInstanceMap<ModuleLoader> loaders;
    /** The guice injector that will init the modules */
    protected Injector injector;
    /**
     * The modules that are loaded, at first the order is when they are constructed but while they are loading
     * it resorts them based on load order, so when loading is done it will enable them properly.
     */
    protected final Map<ModuleInfo, ModuleWrapper> modules = new LinkedHashMap<>();

    protected DucktapeManager(Injector injector, Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap) {
        this.injector = Conditions.nonNull(injector, "extra injector must not be null");
        this.loaders = ImmutableClassToInstanceMap.<ModuleLoader>builder()
            .putAll(loaderMap)
            .build();
    }

    /** Create a snapshot of the modules that are currently in the system when this method is called */
    @Override
    public ImmutableList<net.year4000.utilities.ducktape.module.ModuleInfo> getModules() {
        ImmutableList.Builder<net.year4000.utilities.ducktape.module.ModuleInfo> moduleBuilder = ImmutableList.builder();
        this.modules.forEach(((moduleInfo, moduleWrapper) -> moduleBuilder.add(moduleInfo)));
        return moduleBuilder.build();
    }

    @Override
    public void load() throws ModuleInitException {
        logger.info("Loading module classes from the loaders");
        Set<Class<?>> classes = loadAll();
        logger.info("Setting up the injector");
        List<Module> modules = Lists.newArrayList(this, new ModulesInitModule(classes), new DucktapeModule(this.modules));
        this.injector = this.injector.createChildInjector(modules);
    }

    @Override
    public void enable() throws ModuleInitException {
        logger.info("Enabling modules: " + this.modules);
        this.modules.values().forEach(Enabler::enable);
    }

    @Override
    protected void configure() {
        bind(Ducktape.class).toInstance(this);
    }

    /** Load all classes from the selected path */
    protected Set<Class<?>> loadAll() throws ModuleInitException {
        Set<Class<?>> classes = new HashSet<>();
        this.loaders.forEach((key, value) -> {
            if (loaded.contains(key)) {
                throw new ModuleInitException(ModuleInfo.Phase.LOADING, new IllegalStateException("Can not load the same loader twice."));
            }
            classes.addAll(value.load());
            loaded.add(key);
        });
        return classes;
    }

    public static DucktapeManagerBuilder builder() {
        return new DucktapeManagerBuilder();
    }

    /** This will build the ducktape manager environment with the correct module loaders and guice injector */
    public static class DucktapeManagerBuilder implements Builder<Ducktape> {
        protected final Set<ModuleLoader> loaders = new HashSet<>();
        protected Value<Injector> injectorValue = Value.empty();

        /** Add a module loader system */
        public DucktapeManagerBuilder addLoader(ModuleLoader moduleLoader) {
            this.loaders.add(moduleLoader);
            return this;
        }

        /** Set the injector for ducktape to use */
        public DucktapeManagerBuilder setInjector(Injector injector) {
            this.injectorValue = Value.of(injector);
            return this;
        }

        /** Internal reuse able method that will map reduce the module loader map */
        protected Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMapReduce() {
            return this.loaders.stream().map(loader -> ImmutableMap.<Class<? extends ModuleLoader>, ModuleLoader>of(loader.getClass(), loader))
                .reduce((left, right) -> ImmutableMap.<Class<? extends ModuleLoader>, ModuleLoader>builder().putAll(left).putAll(right).build()).get();
        }

        @Override
        public Ducktape build() {
            Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap = loaderMapReduce();
            return new DucktapeManager(this.injectorValue.getOrElse(Guice.createInjector()), loaderMap);
        }
    }
}
