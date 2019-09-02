package net.year4000.utilities.reflection;

import org.junit.Test;

import java.lang.invoke.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InvokeTest {
    private static final MethodHandles.Lookup lookup = Reflections.<MethodHandles.Lookup>getter(MethodHandles.Lookup.class, MethodHandles.publicLookup(), "IMPL_LOOKUP").getOrThrow();

    public static class Tester {
        public int value = 42;
        public String foo = "bar";

        public String bar() {
            return "foo";
        }

        public String echo(String echo) {
            return echo;
        }
    }

    interface ProxyTester {

        String bar();

        String echo(String echo);
    }

    public static class ProxyInvocationHandler implements InvocationHandler {
        private final Tester tester = new Tester(); // instance

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("method: " + method);
            MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
            MethodHandle methodHandle = lookup.findVirtual(tester.getClass(), method.getName(), methodType);


            //methodHandle.bindTo(tester);
//            CallSite callSite = LambdaMetafactory.metafactory(
//                lookup,
//                method.getName(),
//                MethodType.methodType(ProxyTester.class),
//                methodType,
//                methodHandle,
//                methodType
//            );
//            return callSite.dynamicInvoker().invoke();

            //MethodHandle explicitCastArguments = MethodHandles.explicitCastArguments(methodHandle.bindTo(tester), methodType);
            //return explicitCastArguments.invoke(args);
            return methodHandle.bindTo(tester).invokeWithArguments(args);
        }
    }

    public static class ProxyReflectionInvocationHandler implements InvocationHandler {
        private final Tester tester = new Tester(); // instance

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("method: " + method);
            return Reflections.invoke(Tester.class, tester, method.getName(), args).get();
        }
    }

    public static ProxyTester createProxy() {
        return (ProxyTester) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[] {ProxyTester.class}, new ProxyInvocationHandler());
    }


    public static ProxyTester createReflectionProxy() {
        return (ProxyTester) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class<?>[] {ProxyTester.class}, new ProxyReflectionInvocationHandler());
    }

    @Test
    public void methodHandleTest() throws Throwable {
        ProxyTester proxyTester = createProxy();
        String bar = proxyTester.bar();
        System.out.println("bar -> " + bar);
    }

    @Test
    public void methodHandleArgsTest() throws Throwable {
        ProxyTester proxyTester = createProxy();
        String echo = proxyTester.echo("echo");
        System.out.println("echo -> " + echo);
    }

    @Test
    public void reflectionTest() throws Throwable {
        ProxyTester proxyTester = createReflectionProxy();
        String bar = proxyTester.bar();
        System.out.println("bar -> " + bar);
    }

    @Test
    public void reflectionArgsTest() throws Throwable {
        ProxyTester proxyTester = createReflectionProxy();
        String echo = proxyTester.echo("echo");
        System.out.println("echo -> " + echo);
    }
}
