/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.lookups;

import com.google.common.collect.Lists;
import net.year4000.utilities.Conditions;
import net.year4000.utilities.value.Value;

import java.util.List;

/** Tries and finds the field for method based on the signatures value */
public abstract class AbstractSignatureLookup<T> implements SignatureLookup<T> {
    /** Should it look up fields for methods */
    public enum For {FIELD, METHOD, CONSTRUCTOR}

    // Used to parse the signature

    /** Letters in the alphabet */
    private static final int LETTERS_IN_ALPHABET = 26;

    /** Map of the class types represented by their letter */
    private static final Class<?>[] TYPES = new Class<?>[LETTERS_IN_ALPHABET];
    static  {
        TYPES[id('Z')] = boolean.class;
        TYPES[id('B')] = byte.class;
        TYPES[id('C')] = char.class;
        TYPES[id('D')] = double.class;
        TYPES[id('F')] = float.class;
        TYPES[id('I')] = int.class;
        TYPES[id('J')] = long.class;
        TYPES[id('S')] = short.class;
        TYPES[id('V')] = void.class;
    }

    // Constructor fields
    protected final For type;
    protected final Class<?> from;

    // Caching / Unit Test
    final Class<?> returnType;
    final Class<?>[] argsType;

    /** Set up the lookup from the signature of the string */
    AbstractSignatureLookup(String signature, Class<?> from, For type) {
        Conditions.nonNullOrEmpty(signature, "signature");
        this.from = Conditions.nonNull(from, "from");
        this.type = Conditions.nonNull(type, "type");
        // Grab the return type and args
        this.returnType = type(signature).getOrElse(null);
        this.argsType = (type != For.FIELD) ? methodArgs(signature).getOrElse(null) : null;
    }

    /** Creates a unique map of the classes and the chars */
    private static int id(char letter) {
        // (char + OFFSET) % LETTERS_IN_ALPHABET
        return ((letter + 7) % LETTERS_IN_ALPHABET);
    }

    /** Convert the signature to the class it represents */
    private Class<?> toClass(String str) throws ClassNotFoundException {
        Conditions.nonNullOrEmpty(str, "str");
        int arrays = str.lastIndexOf("[") + 1;
        // Classes without arrays needs to be remapped
        if (arrays == 0) {
            char c = str.charAt(arrays);
            if (TYPES[id(c)] != null) {
                return TYPES[id(c)];
            } else if (c == 'L') {
                return Class.forName(str.substring(1, str.lastIndexOf(";")).replace("/", "."));
            }
        }
        // Tries to handle everything else
        // Primitives and Object arrays work in here
        // only the objects need to convert / to .
        return Class.forName(str.replace("/", "."));
    }

    /** Get the return type of the method if it is one, or the field type, if class not found return empty value */
    private Value<Class<?>> type(String str) {
        Conditions.checkState(type, For.METHOD, For.FIELD, For.CONSTRUCTOR);
        Conditions.nonNullOrEmpty(str, "str");
        int index = str.lastIndexOf(")") + 1;
        try {
            return Value.of((index > 0) ? toClass(str.substring(index)) : toClass(str));
        } catch (ClassNotFoundException exception) {
            return Value.empty();
        }
    }

    /** Get the class types of the method arguments, if class not found return empty value */
    private Value<Class<?>[]> methodArgs(String str) {
        Conditions.checkState(type, For.METHOD, For.CONSTRUCTOR);
        Conditions.nonNullOrEmpty(str, "str");
        str = str.substring(1, str.lastIndexOf(")"));
        List<Class<?>> args = Lists.newArrayList();
        try {
            for (int i = 0 ; i < str.length(); i++) {
                int a = i;
                if (str.charAt(i) == '[') { // Process arrays
                    while (str.charAt(a) == '[') {
                        a++;
                    }
                }
                if (str.charAt(a) == 'L') { // Grab the class from the Object type
                    int b = str.indexOf(";", a + 1);
                    args.add(toClass(str.substring(i, b + 1)));
                    i = b;
                } else { // Handle the primitives
                    args.add(toClass(str.substring(i, a + 1)));
                    i = a;
                }
            }
            return Value.of(args.toArray(new Class<?>[]{}));
        } catch (ClassNotFoundException exception) {
            return Value.empty();
        }
    }
}
