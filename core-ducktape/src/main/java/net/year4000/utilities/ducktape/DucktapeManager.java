package net.year4000.utilities.ducktape;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableMap;
import com.google.inject.*;
import net.year4000.utilities.Builder;
import net.year4000.utilities.ducktape.loaders.ModuleLoader;
import net.year4000.utilities.ducktape.module.Enabler;
import net.year4000.utilities.ducktape.module.Module;
import net.year4000.utilities.ducktape.module.ModuleInfo;
import net.year4000.utilities.ducktape.module.ModuleWrapper;
import net.year4000.utilities.value.Value;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class DucktapeManager implements Ducktape {
    /** The set of current loaded modules */
    private Set<Class<? extends ModuleLoader>> loaded = new LinkedHashSet<>();

    /** A map of all loaders stored by their class */
    private ClassToInstanceMap<ModuleLoader> loaders;

    /** The guice injector that will init the modules */
    private Injector injector;

    /**
     * The modules that are loaded, at first the order is when they are constructed but while they are loading
     * it resorts them based on load order, so when loading is done it will enable them properly.
     */
    final Map<ModuleInfo, ModuleWrapper> modules = new LinkedHashMap<>();


    private DucktapeManager(Injector injector, Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap) {
        //this.injector = Guice.createInjector().createChildInjector(new SettingsModule(), new DucktapeModule(this));//.createChildInjector(new DucktapeModule(this)); // child injectors dont work with settings????
        this.loaders = ImmutableClassToInstanceMap.<ModuleLoader>builder()
            .putAll(loaderMap)
            .build();
    }

    @Override
    public void loadAll() {
        Injector injector = Guice.createInjector(new DucktapeModule(this));

        loadAll(null, classes -> {
            for (Class clazz : classes) {
                if (clazz.isAnnotationPresent(Module.class)) {
                    injector.getInstance(clazz);
                }
            }

        });
        System.out.println("modules: " + this.modules);
        this.modules.values().forEach(Enabler::enable);
    }

    /** Load all classes from the selected path */
    protected void loadAll(Path path, Consumer<Collection<Class<?>>> consumer) {
        loaders.forEach((key, value) -> {
            try {
                if (loaded.contains(key)) {
                    throw new IllegalStateException("Can not load the same loader twice.");
                }
                consumer.accept(value.load(path));
                loaded.add(key);
            } catch (IOException | IllegalStateException error) {
                // todo
                System.err.println(error.getMessage());
            }
        });
    }

    public static DucktapeManagerBuilder builder() {
        return new DucktapeManagerBuilder();
    }

    /** This will build the ducktape manager environment with the correct module loaders and guice injector */
    public static class DucktapeManagerBuilder implements Builder<DucktapeManager> {
        private final Set<ModuleLoader> loaders = new HashSet<>();
        private Value<Injector> injectorValue = Value.of(Guice.createInjector());

        public DucktapeManagerBuilder addLoader(ModuleLoader moduleLoader) {
            this.loaders.add(moduleLoader);
            return this;
        }

        @Override
        public DucktapeManager build() {
            Map<Class<? extends ModuleLoader>, ModuleLoader> loaderMap = loaders.stream().map(loader -> ImmutableMap.<Class<? extends ModuleLoader>, ModuleLoader>of(loader.getClass(), loader))
                .reduce((left, right) -> ImmutableMap.<Class<? extends ModuleLoader>, ModuleLoader>builder().putAll(left).putAll(right).build()).get();

            return new DucktapeManager(injectorValue.get(), loaderMap);
        }
    }
}
