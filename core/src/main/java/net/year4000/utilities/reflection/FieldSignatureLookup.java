package net.year4000.utilities.reflection;

import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Field;

/** Look of fields know for the class */
class FieldSignatureLookup extends AbstractSignatureLookup<Field> {
    FieldSignatureLookup(String signature, Class<?> clazz) {
        super(signature, clazz, For.FIELD);
    }

    /** Get all the fields for the class */
    private Field[] fields() {
        try {
            return from.getDeclaredFields();
        } catch (SecurityException exception) {
            return new Field[0];
        }
    }

    /** Find the fields, there is no order */
    @Override
    public ImmutableSet<Field> find() {
        ImmutableSet.Builder<Field> possibles = new ImmutableSet.Builder<>();
        for (Field field : fields()) {
            if (field.getType() == returnType) {
                possibles.add(field);
            }
        }
        return possibles.build();
    }
}
