package com.mmc.work.database.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: mmc
 * @Date: 2018/9/8 22:43
 * @Version 1.0
 *
 * find开头的方法为查询单个
 * list开头的方法为查询集合
 *
 */
public interface QueryApi<T> extends DataBaseApi  {

    enum SQL_ORDER{
        DESC,
        ASC;
    }

    //根据pkey查询某个表的一个属性
    Object findFldByPkey(Class clazz, String fld, Integer pkey);


    //根据where条件的参数和值去查询一个属性
    Optional findFldByParams(Class<T> clazz, String fld, String[] paramNames, Object... objects);

    //根据pkey查询出一个对象
    T findByPkey(Class clazz, Integer pkey);

    //根据pkey集合查询某一个对象集合
    List<T> listByPkeyList(Class<T> clazz,Iterable<Integer> ids);

    //根据where条件的参数和值去查询对象
    T findByParams(Class<T> clazz,String[] paramNames,Object... objects);

    //根据where条件的参数和值去查询记录
    List<T> listByParams(Class<T> clazz,String[] paramNames,Object... objects);

    //根据where条件的参数和值去查询记录
    List<T> listByWhere(Class<T> clazz,String where,Object... objects);

    //根据where条件的参数和值去查询某一个属性的记录
    List<Map<String,Object>> listOneFldByParams(Class clazz, String fld, String[] paramNames, Object... objects);

    //根据where条件的参数和值去查询多个个属性的记录
    List<Map<String,Object>> listManyFldByParams(Class clazz,String[] flds,String[] paramNames,Object... objects);

    //自定义where语句，自行权限判断
    List<Map<String,Object>> query(Class clazz,String sql,Object...objects);

}
