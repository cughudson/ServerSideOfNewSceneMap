package com.example.demo.system.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericsUtil {
    public static Class getClassType(Class<?> thisClass,int index){
        Type superClass = thisClass.getGenericSuperclass();
        Type[] types = ((ParameterizedType) superClass).getActualTypeArguments();
        Type type=types[index];
        Class<Object> classType;
        if (type instanceof ParameterizedType) {
            classType = (Class<Object>) ((ParameterizedType) type).getRawType();
        } else {
            classType = (Class<Object>) type;
        }
        return classType;
    }
}
