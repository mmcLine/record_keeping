package com.mmc.work.database.impl;

import com.mmc.utils.ClassUtil;
import com.mmc.utils.DateUtil;
import com.mmc.utils.RecordException;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.TableApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @Author: mmc
 * @Date: 2018/9/8 23:04
 * @Version 1.0
 *
 */

public class QueryImpl<T> implements QueryApi<T> {



    Logger logger = LoggerFactory.getLogger(org.hibernate.query.internal.QueryImpl.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableApi tableApi;

    @Autowired
    private QueryResultApi queryResultApi;


    @Override
    public Object findFldByPkey(Class clazz, String fld, Integer pkey) {
        String extSql = MessageFormat.format("select {0} from {1} where pkey =?", fld, tableApi.getTable(clazz));
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(extSql, pkey);
        //仅会有一条数据
        while (sqlRowSet.next()) {
            return sqlRowSet.getObject(fld);
        }
        return "";
    }

    @Override
    public Optional findFldByParams(Class<T> clazz, String fld,String[] paramNames, Object... objects) {
        checkParams(paramNames,objects);
        List<Map<String, Object>> oneFldList = listOneFldByParams(clazz, fld, paramNames, objects);
        if(oneFldList!=null&&oneFldList.size()==1){
            return Optional.of(oneFldList.get(0).get(fld));
        }else {
            logger.warn("findFldByParams方法查询结果为空或者查询结果个数不为1");
            return Optional.empty();
        }
    }

    @Override
    public T findByPkey(Class clazz, Integer pkey) {
        String sql = "select * from " + tableApi.getTable(clazz) + " where pkey=?";
        logger.info("执行数据库查询：" + sql);
        Object object = jdbcTemplate.queryForObject(
                sql, new Object[]{pkey},
                new BeanPropertyRowMapper(clazz));
        return object!=null?(T)object:null;

    }

    @Override
    public List<T> listByPkeyList(Class<T> clazz,Iterable<Integer> ids) {
        Assert.notNull(ids, "The given Iterable of Id's must not be null!");

        if (!ids.iterator().hasNext()) {
            return Collections.emptyList();
        }
        List<T> resultList=new ArrayList<>();
        for (Integer id:ids){
            T t=this.findByPkey(clazz,id);
            if(t!=null){
                resultList.add(t);
            }
        }
        return resultList;
    }

    @Override
    public List<T> listByWhere(Class<T> clazz, String where, Object... objects) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        sql.append(getAuthorithWhere(clazz));
        sql.append(" AND "+where);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql.toString(), objects);
        return rowSetToList(clazz,sqlRowSet);
    }

    @Override
    public T findByParams(Class clazz, String[] paramNames, Object... objects) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        if(paramNames!=null) {
            for (String param : paramNames) {
                sql.append(param + "= ? and ");
            }
        }
        sql.append(getAuthorithWhere(clazz));
        logger.info("执行数据库查询：" + sql);
        try{
            Object object = jdbcTemplate.queryForObject(sql.toString(), objects, new BeanPropertyRowMapper(clazz));
            return (T)object;
        }catch (EmptyResultDataAccessException e){
            //查询结果为空
            return null;
        }
    }

    @Override
    public List<T> listByParams(Class clazz, String[] paramNames, Object... objects) {
        checkParams(paramNames,objects);
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        if(paramNames!=null) {
            for (String param : paramNames) {
                sql.append(param + "= ? and ");
            }
        }
        sql.append(getAuthorithWhere(clazz));
        logger.info("执行数据库查询：" + sql);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql.toString(), objects);
        return rowSetToList(clazz,sqlRowSet);
    }

    @Override
    public List<Map<String,Object>> listOneFldByParams(Class clazz, String fld, String[] paramNames, Object... objects) {
        return this.listManyFldByParams(clazz, new String[]{fld}, paramNames, objects);
    }

    @Override
    public List<Map<String,Object>> listManyFldByParams(Class clazz, String[] flds, String[] paramNames, Object... objects) {
        checkParams(paramNames,objects);
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (int i = 0; i < flds.length; i++) {
            sql.append(flds[i]);
            if (i < flds.length - 1) {
                sql.append(",");
            }
        }
        sql.append(" from " + tableApi.getTable(clazz) + " where ");
        if(paramNames!=null){
            for (String param : paramNames) {
                sql.append(param + "= ? and ");
            }
        }
        sql.append(getAuthorithWhere(clazz));
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString(),objects);
        return queryResultApi.extJoinData(mapList);
    }

    @Override
    public List<Map<String, Object>> query(Class clazz,String sql, Object... objects) {
        String lowerSql=sql.toLowerCase();
        if(lowerSql.contains("where")){
            sql=sql.replace("where","where"+getAuthorithWhere(clazz)+"and ");
            sql=sql.replace("WHERE","where"+getAuthorithWhere(clazz)+"and ");

        }else {
            if(lowerSql.contains("order by")||lowerSql.contains("limit")||lowerSql.contains("join")){
                throw new RecordException("QueryImpl","暂不支持复杂的查询");
            }else {
                sql+=" where "+getAuthorithWhere(clazz);
            }
        }
        logger.info("执行数据库查询：" + sql);
        return queryResultApi.extJoinData(jdbcTemplate.queryForList(sql,objects));
    }


    private void checkParams(String[] paramNames,Object... objects){
        if(paramNames!=null &&paramNames.length != objects.length) {
            throw new IllegalArgumentException("输入的属性名和属性值长度不同");
        }
    }



    public String getAuthorithWhere(Class clazz) {
        return " 1=1 ";
    }

    private List<T> rowSetToList(Class<T> clazz,SqlRowSet sqlRowSet) {
        Field[] allFields = ClassUtil.getAllFields(clazz);
        List<T> list = new ArrayList<>();
        while (sqlRowSet.next()) {
            try {
                T t = (T) clazz.newInstance();
                for (Field field : allFields) {
                    Method method = ClassUtil.getSetMethod(clazz, field, field.getType());
                    String fieldCode = tableApi.getSqlCode(field.getName());
                    if (field.getType() == int.class || field.getType() == Integer.class) {
                        method.invoke(t, sqlRowSet.getInt(fieldCode));
                    } else if (field.getType() == double.class || field.getType() == Double.class) {
                        method.invoke(t, sqlRowSet.getDouble(fieldCode));
                    } else if (field.getType() == long.class || field.getType() == Long.class) {
                        method.invoke(t, sqlRowSet.getLong(fieldCode));
                    } else if (field.getType() == byte.class || field.getType() == Byte.class) {
                        method.invoke(t, sqlRowSet.getByte(fieldCode));
                    } else if (field.getType() == short.class || field.getType() == Short.class) {
                        method.invoke(t, sqlRowSet.getShort(fieldCode));
                    } else if (field.getType() == float.class || field.getType() == Float.class) {
                        method.invoke(t, sqlRowSet.getFloat(fieldCode));
                    } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                        method.invoke(t, sqlRowSet.getBoolean(fieldCode));
                    } else if (field.getType() == BigDecimal.class) {
                        method.invoke(t, sqlRowSet.getBigDecimal(fieldCode));
                    } else if (field.getType() == String.class) {
                        method.invoke(t, sqlRowSet.getString(fieldCode));
                    } else if (field.getType() == LocalDate.class) {
                        java.sql.Date date = sqlRowSet.getDate(field.getName());
                        if (date != null) {
                            method.invoke(t, DateUtil.DatatoLocalDate(new Date(date.getTime())));
                        }
                    }
                }
                list.add(t);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
