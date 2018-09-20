package com.mmc.utils;

import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Author: mmc
 * @Date: 2018/9/13 21:58
 * @Version 1.0
 */
public class ClassUtil {

    /**
     * 获取所有的fld，包括父类
     * @param clazz
     * @return
     */
    public static Field[] getAllFields(Class clazz){
        Class superClass=clazz;
        Field[] array=null;
        while (superClass!=null){
            Field[] declaredFields = superClass.getDeclaredFields();
            if(array==null){
                array=declaredFields;
            }else {
                int oldLength=array.length;
                array = Arrays.copyOf(array, oldLength + declaredFields.length);
                System.arraycopy(declaredFields,0,array,oldLength,declaredFields.length);
            }
            superClass=superClass.getSuperclass();
        }
        return array;
    }

    public static Method getSetMethod(Class clazz,Field field,Class paramClass){
        field.setAccessible(true);
        String name = field.getName();
        String setName="set"+name.substring(0,1).toUpperCase()+name.substring(1);
        return ClassUtils.getMethod(clazz, setName,paramClass);
    }


}
