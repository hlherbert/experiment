package com.hl.experiment.exam.doc;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ExampleUtil {
    private static Map<Type, Object> examples = new ConcurrentHashMap<>();

    private static Uninstance UNINSTANCE = new Uninstance();

    /**
     * 不可实例化
     */
    private static class Uninstance {
        public String toString() {
            return "UNINSTANCE";
        }
    }


    static {
        examples.put(String.class, "a");
        examples.put(Integer.class, 1);
        examples.put(Double.class, 1.1);
        examples.put(Float.class, 1.1f);
        examples.put(Date.class, new Date());
    }

    public static Object getExampleByTypeName(String typeName) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return UNINSTANCE;
        }
        return getExample(clazz);
    }

    public static Object getExample(Type type) {
        Object ex = examples.get(type);
        if (ex == null) {
            try {
                Class<?> clazz = Class.forName(type.getTypeName());
                ex = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object fileEx = getExample(field.getType());
                    field.set(ex, fileEx);
                }
            } catch (IllegalAccessException e) {
                ex = UNINSTANCE;
            } catch (InstantiationException e) {
                ex = UNINSTANCE;
            } catch (ClassNotFoundException e) {
                ex = UNINSTANCE;
            }

            examples.put(type, ex);
        }

        return ex;
    }
}
