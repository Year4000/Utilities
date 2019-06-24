package net.year4000.utilities.ducktape.module;

import net.year4000.utilities.Conditions;
import net.year4000.utilities.ErrorReporter;
import net.year4000.utilities.reflection.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleWrapper implements Loader, Enabler {
    private ModuleInfo moduleInfo;
    private Object proxyInstance;

    /** Create the module wrapper that creates the module instance and prep it for other things */
    public ModuleWrapper(ModuleInfo moduleInfo, Object instance) {
        this.moduleInfo = Conditions.nonNull(moduleInfo, "moduleInfo must exists");
        this.proxyInstance = createProxy(moduleInfo.getModuleClass(), Conditions.nonNull(instance, "instance must exists"));
    }

    /** Create  the proxy instance that will invoke the proper method */
    private Object createProxy(Class<?> moduleClass, final Object moduleInstance) {
        List<Class<?>> interfaces = new ArrayList<>();
        final Map<String, Method> methodLookup = new HashMap<>();
        for (Method method : moduleClass.getMethods()) {
            if (method.isAnnotationPresent(Load.class)) {
                method.setAccessible(true);
                interfaces.add(Loader.class);
                methodLookup.put("load", method);
            }
            if (method.isAnnotationPresent(Enable.class)) {
                method.setAccessible(true);
                interfaces.add(Enabler.class);
                methodLookup.put("enable", method);
            }
        }
        return Proxy.newProxyInstance(moduleClass.getClassLoader(), interfaces.toArray(new Class<?>[]{}), (proxy, method, args) -> {
            try {
                Method proxyMethod = methodLookup.get(method.getName());
                if (proxyMethod != null) {
                    return proxyMethod.invoke(moduleInstance, args);
                }
            } catch (Exception error) {
                ErrorReporter.builder(error)
                    .add("Fail to invoke method for the reason in the stacktrace")
                    .add("Method", method)
                    .add("ModuleInfo", this.moduleInfo)
                    .buildAndReport(System.err);
            }
            return Reflections.invoke(method.getDeclaringClass(), moduleInstance, method.getName()).get();
        });
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
