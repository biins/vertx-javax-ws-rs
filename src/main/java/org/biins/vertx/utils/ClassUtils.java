package org.biins.vertx.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Martin Janys
 */
public class ClassUtils {

    public static List<Field> getFields(Class<?> type) {
        if (type.getSuperclass() != null) {
            List<Field> list = new ArrayList<>();
            list.addAll(getFields(type.getSuperclass()));
            list.addAll(getInstanceFields(type.getDeclaredFields()));
            return list;
        }
        else if (Object.class.equals(type)) {
            return Collections.emptyList();
        }
        else {
            return Arrays.asList(type.getDeclaredFields());
        }
    }

    private static Collection<? extends Field> getInstanceFields(Field[] fields) {
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers()) && !(field.getGenericType() instanceof TypeVariable)) {
                fieldList.add(field);
            }
        }
        return fieldList;
    }

    public static <T> T newInstance(Class<T> type) {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            if (type.getEnclosingClass() != null && constructor == null) {
                // inner, no static class
                throw new IllegalArgumentException("Inner class is not supported");
                // Object enclosing = newInstance(type.getEnclosingClass());
                // Constructor<T> constructor = type.getConstructor(enclosing.getClass());
                // return constructor.newInstance(enclosing);

            }
            else {
                if (!constructor.isAccessible()) {
                    constructor.setAccessible(true);
                }
                return constructor.newInstance();
            }
        }
        catch (ReflectiveOperationException e) {
            throw  new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> type, Constructor<?> constructor, Object ... parameters) {
        try {
            return (T) constructor.newInstance(parameters);
        }
        catch (ReflectiveOperationException e) {
            return null;
        }
    }

    public static void setProperty(Object o, Field field, Object fieldValue) {
        try {
            if (Modifier.isFinal(field.getModifiers())) {
                return;
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }

            field.set(o, fieldValue);
        }
        catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotation) {
        return cls.getAnnotation(annotation);
    }

    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotation) {
        return method.getAnnotation(annotation);
    }

    public static Method[] getMethods(Class<?> api) {
        return api.getMethods();
    }

    public static boolean isPresent(String cls) {
        try {
            Class.forName(cls);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Class<?> getClass(String cls) {
        try {
            return Class.forName(cls);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
