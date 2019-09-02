/*
 * Copyright 2016 Year4000. All Rights Reserved.
 */

package net.year4000.utilities.reflection.lookups;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

import java.lang.reflect.Field;
import java.util.Comparator;

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

    /** Sort by field name as that is the only valid choice */
    @Override
    public ImmutableSortedSet<Field> findSorted() {
        return ImmutableSortedSet.copyOf(Comparator.comparing(Field::getName), find());
    }
}
