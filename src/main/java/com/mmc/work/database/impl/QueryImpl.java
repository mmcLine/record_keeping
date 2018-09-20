package com.mmc.work.database.impl;

import com.mmc.utils.ClassUtil;
import com.mmc.utils.DateUtil;
import com.mmc.utils.SpringContextUtil;
import com.mmc.work.base.ExtName;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.TableApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @Author: mmc
 * @Date: 2018/9/8 23:04
 * @Version 1.0
 */
@Component
public class QueryImpl<T> implements QueryApi<T> {

    Logger logger = LoggerFactory.getLogger(QueryImpl.class);

    public static final int PAGE_SIZE = 20;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableApi tableApi;


    @Override
    public List<Map<String,Object>> listData(Class clazz, Integer pageNo, String where, String order) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        sql.append(StringUtils.hasLength(where) ? where : "1=1");
        sql.append(" and");
        sql.append(getAuthorithWhere(clazz));
        int start = (pageNo - 1) * PAGE_SIZE;
        int end = pageNo * PAGE_SIZE;
        sql.append(" ORDER BY pkey " + (order == null ? "DESC" : order));
        sql.append(" limit " + start + "," + end);
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
        return extJoinData(mapList);
    }

    @Override
    public List<Map<String,Object>> listCombobox(Class clazz) {
        try {
            ExtName extName = (ExtName) clazz.newInstance();
            String sql = MessageFormat.format("select pkey,{0} from {1} ORDER BY pkey DESC", extName.fldName(), tableApi.getTable(clazz));
            logger.info("执行数据库查询：" + sql);
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
            return mapList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Object findFldByPkey(Class clazz, String fld, Integer pkey) {
        return this.findValueByPkey(tableApi.getTable(clazz), fld, pkey);
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
        Field[] allFields = ClassUtil.getAllFields(clazz);
        List<T> list = new ArrayList<T>();
        while (sqlRowSet.next()) {
            try {
                T t = (T) clazz.newInstance();
                for (Field field : allFields) {
                        Method method = ClassUtil.getSetMethod(clazz, field, field.getType());
                        String fieldCode=tableApi.getSqlCode(field.getName());
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
                            if(date!=null){
                                method.invoke(t, DateUtil.DatatoLocalDate(new Date(date.getTime())));
                            }
                        }
                    }
                }catch (InstantiationException | IllegalAccessException|InvocationTargetException e) {
                e.printStackTrace();
            }
            }
        return list;
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
        for (String param : paramNames) {
            sql.append(param + "= ? and ");
        }
        sql.append(getAuthorithWhere(clazz));
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString(),objects);
        return mapList;
    }

    @Override
    public List<Map<String,Object>> query(String sql, Object... objects) {
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, objects);
        return extJoinData(mapList);
    }

    /**
     * 获取权限的where语句
     * 判断是否是有家庭的，如果有按照家庭显示记录
     *
     * @return
     */
    @Override
    public String getAuthorithWhere(Class clazz) {
        Integer family = SpringContextUtil.getLoginUser().getFamily();
        StringBuilder sql = new StringBuilder();
        //登录时查询
        if(SpringContextUtil.getLoginUser().getUser()==null){
            sql.append(" 1=1 ");
            return sql.toString();
        }
        if (family != null && tableApi.hasPropertis(clazz, "family")) {
            sql.append(" family=" + family);
        } else if (tableApi.hasPropertis(clazz, "user")) {
            sql.append(" user=" + SpringContextUtil.getLoginUser().getUser());
        } else {
            sql.append(" pkey=" + SpringContextUtil.getLoginUser().getUser());
        }

        return sql.toString();
    }

    //对结果数据进行二次加工
    private List<Map<String, Object>> extJoinData(List<Map<String, Object>> mapList) {
        //缓存关联数据Map<表名,Map<主键，新数据>>
        Map<String, Map<Integer, Object>> cashMap = new HashMap<>();
        List<Map<String, Object>> newMapList =new ArrayList<>(mapList.size());
        for (Map<String, Object> map : mapList) {
            Map<String, Object> newMap=new HashMap<>(map);
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                if (next.getValue() instanceof Date) {
                    newMap.put(next.getKey(),next.getValue().toString());
                } else if (next.getKey().equals("create_time") && next.getValue() instanceof Date){
                    newMap.put(next.getKey(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(next.getValue()));
                } else if (next.getKey().contains("_id")) {
                    newMap.put(next.getKey().replace("_id", "_name"), getJoinWithPkey(cashMap, next));
                }else if(next.getKey().equals("user")){
                    newMap.put("user_name",getJoinWithPkey(cashMap,next));
                }
            }
            newMapList.add(newMap);
        }
        return newMapList;
    }


    /*  将数据中关联表的pkey替换为值
     * @return
     */
    private String getJoinWithPkey(Map<String, Map<Integer, Object>> cashMap, Map.Entry<String, Object> next) {
        try {
            //根据命名规则获取表名
            String extTable = next.getKey().replace("_id", "");
            //组装全类名
            String fullClassName = "com.mmc.work.bean." + extTable.replace("_", "") + "."
                    + tableApi.getClassName(extTable);
            ExtName instance = (ExtName) Class.forName(fullClassName).newInstance();
            //要关联的字段的主键
            Integer joinPkey = Integer.valueOf(next.getValue().toString());
            //先检查缓存里是否有数据
            Object joinValue;
            if (cashMap.get(extTable) != null && (joinValue = cashMap.get(extTable).get(joinPkey)) != null) {

            } else {
                joinValue = findValueByPkey(extTable, instance.fldName(), joinPkey);
                Map<Integer, Object> objectMap = new HashMap<>();
                objectMap.put(joinPkey, joinValue);
                cashMap.put(extTable, objectMap);
            }
            return joinValue.toString();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 根据pkey查询一个表里的一个字段
     *
     * @param fld
     * @param table
     * @param pkey
     * @return
     */
    private Object findValueByPkey(String table, String fld, Integer pkey) {
        String extSql = MessageFormat.format("select {0} from {1} where pkey =?", fld, table);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(extSql, pkey);
        //仅会有一条数据
        while (sqlRowSet.next()) {
            return sqlRowSet.getObject(fld);
        }
        return "";
    }


    private void checkParams(String[] paramNames,Object... objects){
        if(paramNames!=null &&paramNames.length != objects.length) {
                throw new IllegalArgumentException("输入的属性名和属性值长度不同");
        }
    }
}
