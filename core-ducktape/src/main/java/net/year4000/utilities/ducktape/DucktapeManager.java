/*
 * Copyright 2019 Year4000. All Rights Reserved.
 */
package net.year4000.utilities.ducktape;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import net.year4000.utilities.Builder;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.ducktape.loaders.ModuleLoader;
import net.year4000.utilities.ducktape.module.Enabler;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.ModuleInfo;
import net.year4000.utilities.ducktape.module.ModuleWrapper;
import net.year4000.utilities.value.Value;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DucktapeManager extends AbstractModule implements Ducktape {
    /** The set of current loaded modules */
    private Set<Class<? extends ModuleLoader>> loaded = new LinkedHashSet<>();

    /** A map of all loaders stored by their class */
    private ClassToInstanceMap<ModuleLoader> loaders;

    /** The guice injector that will init the modules */
    private Injector injector;

    private Path modsPath = Paths.get("mods/");

    /**
     * The modules that are loaded, at first the order is when they are constructed but while they are loading
     * it resorts them based on load order, so when loading is done it will enable them properly.
     */
    private final Map<ModuleInfo, ModuleWrapper> modules = new LinkedHashMap<>();


    private DucktapeManager(Injector injector, Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap) {
        this.injector = Conditions.nonNull(injector, "extra injector must not be null");
        this.loaders = ImmutableClassToInstanceMap.<ModuleLoader>builder()
            .putAll(loaderMap)
            .build();
    }

    /** Create a snapshot of the modules that are currently in the system when this method is called */
    @Override
    public ImmutableList<ModuleInfo> getModules() {
        ImmutableList.Builder<ModuleInfo> moduleBuilder = ImmutableList.builder();
        this.modules.forEach(((moduleInfo, moduleWrapper) -> moduleBuilder.add(moduleInfo)));
        return moduleBuilder.build();
    }

    @Override
    public void init() throws ModuleInitException {
        System.out.println("Loading module classes from the loaders");
        Set<Class<?>> classes = loadAll(this.modsPath);
        System.out.println("Setting up the injector");
        // todo think? use child injector or our own injector, is we use a child injector sponge plugins will work, if not modules only have our bindings
        this.injector = this.injector.createChildInjector(this, new ModulesInitModule(classes), new DucktapeModule(this.modules), new SettingsModule());
        //this.injector = Guice.createInjector(this, new ModulesInitModule(classes), new DucktapeModule(this.modules), new SettingsModule());
        System.out.println("Enabling modules: " + this.modules);
        this.modules.values().forEach(Enabler::enable);
    }

    @Override
    protected void configure() {
        bind(Ducktape.class).toInstance(this);
        bind(Path.class).annotatedWith(Names.named("mods")).toInstance(this.modsPath);
    }

    /** Load all classes from the selected path */
    protected Set<Class<?>> loadAll(Path path) throws ModuleInitException {
        System.out.println("path: " + path);
        Set<Class<?>> classes = new HashSet<>();
        this.loaders.forEach((key, value) -> {
            try {
                if (loaded.contains(key)) {
                    throw new ModuleInitException(ModuleInfo.Phase.LOADING, new IllegalStateException("Can not load the same loader twice."));
                }
                classes.addAll(value.load(path));
                loaded.add(key);
            } catch (IOException error) {
                throw new ModuleInitException(ModuleInfo.Phase.LOADING, error);
            }
        });
        return classes;
    }

    public static DucktapeManagerBuilder builder() {
        return new DucktapeManagerBuilder();
    }

    /** This will build the ducktape manager environment with the correct module loaders and guice injector */
    public static class DucktapeManagerBuilder implements Builder<DucktapeManager> {
        private final Set<ModuleLoader> loaders = new HashSet<>();
        private Value<Injector> injectorValue = Value.empty();

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

        @Override
        public DucktapeManager build() {
            Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap = loaders.stream().map(loader -> ImmutableMap.<Class<? extends ModuleLoader>, ModuleLoader>of(loader.getClass(), loader))
                .reduce((left, right) -> ImmutableMap.<Class<? extends ModuleLoader>, ModuleLoader>builder().putAll(left).putAll(right).build()).get();

            return new DucktapeManager(injectorValue.getOrElse(Guice.createInjector()), loaderMap);
        }
    }
}
