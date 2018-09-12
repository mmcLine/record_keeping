package com.mmc.work.database.api;

import java.util.List;

/**
 * @Author: mmc
 * @Date: 2018/9/8 22:43
 * @Version 1.0
 */
public interface QueryApi<T> extends DataBaseApi  {

    //普通的表单查询,带分页的
    List<T> listData(Class clazz, Integer pageNo, String where, String order);

    //下拉的数据查询
    List listCombobox(Class clazz);

    //根据pkey查询某个表的一个属性
    Object findFldByPkey(Class clazz, String fld, Integer pkey);

    //根据pkey查询出一个对象
    T findByPkey(Class clazz, Integer pkey);

    //根据where条件的参数和值去查询记录
    List<T> listByParams(Class clazz,String[] paramNames,Object... objects);

    //根据where条件的参数和值去查询某一个属性的记录
    List findOneFldByParams(Class clazz,String fld,String[] paramNames,Object... objects);

    //根据where条件的参数和值去查询多个个属性的记录
    List findManyFldByParams(Class clazz,String[] flds,String[] paramNames,Object... objects);

    //自定义sql语句查询
    List query(String sql,Object... objects);

}
