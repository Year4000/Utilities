package net.year4000.utilities;

import com.google.common.base.Joiner;
import net.year4000.utilities.reflection.Reflections;
import net.year4000.utilities.utils.UtilityConstructError;
import net.year4000.utilities.value.Value;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Contains basic util methods that do not belong in a set category */
public final class Utils {
    private Utils() {
        UtilityConstructError.raise();
    }

    /** Does the string match what a uuid should look like */
    public static boolean isUUID(String uuid) {
        return uuid != null && !uuid.isEmpty() && uuid.split("-").length == 5 && uuid.length() <= 36;
    }

    /** Convert a string uuid to UUID object*/
    public static UUID toUUID(String uuid) {
        Conditions.nonNullOrEmpty(uuid, "uuid");
        if (isUUID(uuid)) {
            return UUID.fromString(uuid);
        } else {
            return UUID.fromString(uuid.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
        }
    }

    /** Validate the ip */
    public static boolean isIpAddress(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        // IP6
        if (ip.indexOf(':') > 0 && ip.split(":").length == 8) {
            return true;
        }
        // IP4
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        for (String part : parts) {
            Value<Integer> ipPart = Value.parseInteger(part);
            if (ipPart.isEmpty()) {
                return false;
            }
            if (ipPart.get() < 0 && ipPart.get() > 256) {
                return false;
            }
        }

        return true;
    }

    /** Get the server name */
    public static String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (Exception e) {
            return "unknown";
        }
    }

    /** Sleep the current thread */
    public static void quickSleep() {
        try {
            Thread.sleep(Byte.MAX_VALUE);
        } catch (InterruptedException error) {
            throw new RuntimeException(error);
        }
    }

    /** Grab the field state value and name, used for toString */
    private static Value<String> fieldState(Field field, Object instance) {
        Value<Object> inst = Reflections.getter(instance, field.getName());
        if (inst.isPresent()) {
            return Value.of(field.getName() + ": " + inst.get());
        }
        return Value.of(field.getName() + ": null");
    }

    /** Use reflection to print the to string method */
    public static String toString(Object instance, String... vars) {
        String prefix = instance.getClass().getSimpleName();
        Field[] fields = instance.getClass().getDeclaredFields();
        List<String> variables = new ArrayList<>(fields.length);
        if (vars.length == 0) {
            for (Field field : fields) {
                fieldState(field, instance).ifPresent(variables::add);
            }
        } else {
            for (String name : vars) {
                try {
                    fieldState(instance.getClass().getDeclaredField(name), instance).ifPresent(variables::add);
                } catch (NoSuchFieldException error) {
                    error.printStackTrace();
                    variables.add(name + ": error");
                }
            }
        }
        return prefix + "(" + instance.hashCode()  +") {" + Joiner.on(", ").join(variables) + "}";
    }

    /** Check if the objects hash codes matches, thus the objects should be equal */
    public static boolean equals(Object a, Object b) {
        return Objects.hashCode(a) == Objects.hashCode(b);
    }

    /** Generate a hash code for the list of objects */
    public static int hashCode(Object instance, Object... objects) {
        int hash = Objects.hashCode(instance.getClass().getName());
        if (objects.length == 0) {
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                hash ^= objects.length;
                hash += Objects.hashCode(Reflections.getter(instance, field.getName()));
            }
        } else {
            for (Object object : objects) {
                hash ^= objects.length;
                hash += Objects.hashCode(object);
            }
        }
        return hash;
    }
}
