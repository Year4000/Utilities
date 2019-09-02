package net.year4000.utilities.reflection;

import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.*;
import java.lang.reflect.Field;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

// Created by:
// https://gist.githubusercontent.com/Andrei-Pozolotin/dc8b448dc590183f5459
public class BlackMagicTest {

    // Black magic solution for: "Java 8 access private member with lambda?"
    // http://stackoverflow.com/questions/28184065/java-8-access-private-member-with-lambda
    static class Target {

        private int id;

        Target(final int id) {
            this.id = id;
        }
    }

    public class PrivateTargetLambda {
        ToIntFunction getterLambda(final MethodHandles.Lookup caller, final MethodHandle getterHandle) throws Throwable {

            final Class<?> functionKlaz = ToIntFunction.class;
            final String functionName = "applyAsInt";
            final Class<?> functionReturn = int.class;
            final Class<?>[] functionParams = new Class<?>[] { Object.class };

            //

            final MethodType factoryMethodType = MethodType
                .methodType(functionKlaz);
            final MethodType functionMethodType = MethodType.methodType(
                functionReturn, functionParams);

            final CallSite getterFactory = LambdaMetafactory.metafactory( //
                caller, // Represents a lookup context.
                functionName, // The name of the method to implement.
                factoryMethodType, // Signature of the factory method.
                functionMethodType, // Signature of function implementation.
                getterHandle, // Function method implementation.
                getterHandle.type() // Function method type signature.
            );

            final MethodHandle getterInvoker = getterFactory.getTarget();

            final ToIntFunction getterLambda = (ToIntFunction) getterInvoker.invokeExact();

            return getterLambda;
        }

        ObjIntConsumer setterLambda(final MethodHandles.Lookup caller, final MethodHandle setterHandle) throws Throwable {

            final Class<?> functionKlaz = ObjIntConsumer.class;
            final String functionName = "accept";
            final Class<?> functionReturn = void.class;
            final Class<?>[] functionParams = new Class<?>[] { Object.class,
                int.class };

            final MethodType factoryMethodType = MethodType
                .methodType(functionKlaz);
            final MethodType functionMethodType = MethodType.methodType(
                functionReturn, functionParams);

            final CallSite setterFactory = LambdaMetafactory.metafactory( //
                caller, // Represents a lookup context.
                functionName, // The name of the method to implement.
                factoryMethodType, // Signature of the factory method.
                functionMethodType, // Signature of function implementation.
                setterHandle, // Function method implementation.
                setterHandle.type() // Function method type signature.
            );

            final MethodHandle setterInvoker = setterFactory.getTarget();

            final ObjIntConsumer setterLambda = (ObjIntConsumer) setterInvoker
                .invokeExact();

            return setterLambda;
        }
    }

    @Test
    public void main() throws Throwable {

        // Define black magic.
        final MethodHandles.Lookup original = MethodHandles.lookup();
        final Field internal = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        internal.setAccessible(true);
        final MethodHandles.Lookup trusted = (MethodHandles.Lookup) internal.get(original);

        // Invoke black magic.
        final MethodHandles.Lookup caller = trusted.in(Target.class);

        //final Method getterMethod = Target.class.getDeclaredMethod("id");
        //final Method setterMethod = Target.class.getDeclaredMethod("id", int.class);

        final MethodHandle getterHandle = caller.unreflectGetter(Target.class.getDeclaredField("id"));
        final MethodHandle setterHandle = caller.unreflectSetter(Target.class.getDeclaredField("id"));

        //PrivateTargetLambda l = new PrivateTargetLambda();
        //final ToIntFunction getterLambda = l.getterLambda(caller, getterHandle);
        //final ObjIntConsumer setterLambda = l.setterLambda(caller, setterHandle);


        final int set1 = 123;

        final Target target = new Target(set1);

        //final int get1 = getterLambda.applyAsInt(target);
        int get1 = (int) getterHandle.invoke(target);

        Assert.assertEquals(set1, get1);

        final int set2 = 456;

        setterHandle.invoke(target, set2);

        final int get2 = (int) getterHandle.invoke(target);
        Assert.assertEquals(set2, get2);
    }

}
