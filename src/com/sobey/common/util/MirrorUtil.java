package com.sobey.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by Yanggang.
 * Date: 11-1-9
 * Time: 上午12:55
 * To change this template use File | Settings | File Templates.
 */
public class MirrorUtil<T> {
    public static <T> Class<T> getTypeParam(Class<?> klass, int index) {
        Type[] types = getTypeParams(klass);
        if (index >= 0 && index < types.length) {
            Type t = types[index];
            if (t instanceof Class<?>) {
                return (Class<T>) t;
            } else if (t instanceof ParameterizedType) {
                t = ((ParameterizedType) t).getRawType();
                return (Class<T>) t;
            }
            throw new RuntimeException(String.format("Type '%s' is not a Class", t.toString()));
        }
        throw new RuntimeException(String.format("Class type param out of range %d/%d", index, types.length));
    }

    public static Type[] getTypeParams(Class<?> klass) {
        if (klass == null || "java.lang.Object".equals(klass.getName()))
            return null;
        Type superclass = klass.getGenericSuperclass();
        if (null != superclass && superclass instanceof ParameterizedType)
            return ((ParameterizedType) superclass).getActualTypeArguments();

        Type[] interfaces = klass.getGenericInterfaces();
        for (Type inf : interfaces) {
            if (inf instanceof ParameterizedType) {
                return ((ParameterizedType) inf).getActualTypeArguments();
            }
        }
        return getTypeParams(klass.getSuperclass());
    }

    public static <T> T getObject(Class<?> klass, Map<String, Integer> map) {
        Object object = null;
        try {
            object = klass.newInstance();
            Field[] fields = klass.getDeclaredFields();
            if (map == null) {
                return (T) object;
            }
            for (Field f : fields) {
                f.setAccessible(true);
                for (Map.Entry<String, Integer> m : map.entrySet()) {
                    if (f.getName().equals(m.getKey())) {
                        f.set(object, map.get(m.getKey()));
                    }
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Can not newInstance for '%s' ", klass.toString()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

        return (T) object;
    }

    public static <T> T setValue(T t, Map<String, Integer> map) {
        Class klass = t.getClass();
        try {
            Field[] fields = klass.getDeclaredFields();
            if (map == null) {
                return t;
            }
            for (Field f : fields) {
                f.setAccessible(true);
                for (Map.Entry<String, Integer> m : map.entrySet()) {
                    if (f.getName().equals(m.getKey())) {
                        f.set(t, map.get(m.getKey()));
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e.getMessage());
        }

        return t;
    }

    public static Object getValue(Class<?> klass, Object o, String field) {
        try {
            Field[] fields = klass.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                if (f.getName().endsWith(field)) {
                    return f.get(o);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        throw new RuntimeException(String.format(" field not find in Class '%s'", klass.getSimpleName()));
    }
}
