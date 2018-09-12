package com.mmc.work.database.impl;

import com.mmc.work.database.api.TableApi;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.Table;
import java.beans.PropertyDescriptor;

/**
 * @Author: mmc
 * @Date: 2018/9/8 23:28
 * @Version 1.0
 */
@Component
public class TableImpl implements TableApi {

    @Override
    public String getTable(Class clazz) {
        String className = clazz.getName();
        //判断是否指定了表名
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation != null && StringUtils.hasLength(annotation.name())) {
            return annotation.name();
        } else {
            return getTableByName(className.substring(className.lastIndexOf(".")+1));
        }
    }

    @Override
    public String getClassName(String table) {
        char[] chars = table.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '_') {
                sb.append(Character.toUpperCase(chars[++i]));
            } else {
                sb.append(chars[i]);
            }
        }
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    @Override
    public boolean hasPropertis(Class clazz, String fldName) {
        try{
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, fldName);
            return propertyDescriptor!=null;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String getSqlCode(String code) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append("_" + Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 类名转换为表名
     * 不能直接使用，因为没考虑@Table注解自定义了表名的情况
     * @param className
     * @return
     */
    private String getTableByName(String className) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < className.length(); i++) {
            char c = className.charAt(i);
            if (i == 0) {
                char first = Character.toLowerCase(c);
                sb.append(first);
            } else if (Character.isUpperCase(c)) {
                sb.append("_" + Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
