package com.hl.experiment.exam.doc;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExampleUtil {
    private static Map<Type, String> examples = new ConcurrentHashMap<>();

    static {
        examples.put(String.class, "a");
        examples.put(Integer.class, "1");
        examples.put(Double.class, "1.1");
        examples.put(Float.class, "1.1");
        examples.put(Date.class, "2019-01-01T15:31:00");
    }
    public static String getExample(Type type) {
        String ex = examples.get(type);
        if (ex == null) {
            Class<?> clazz = type.getClass();
            Field[] fields = clazz.getFields();
        }
        return ex;
    }
}
