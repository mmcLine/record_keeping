package com.mmc.work.database.api;

import org.springframework.stereotype.Component;

/**
 * @Author: mmc
 * @Date: 2018/9/8 23:27
 * @Version 1.0
 *
 * 对表的一些操作的方法 */
@Component
public interface TableApi {


    //根据类获取表名，考虑注解定义了表名的情况
    public String getTable(Class clazz);

    //根据表名获取类名
    public String getClassName(String table);


    //判断类是否有某属性
    public boolean hasPropertis(Class clazz,String fldName);

    //转字段名为数据库字段名
    public String getSqlCode(String code);

}
