package com.mmc.work.database.impl;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.base.ExtName;
import com.mmc.work.base.IdBase;
import com.mmc.work.database.api.AuthorithApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.TableApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mmc.assist.result.ResultUtil.PAGE_SIZE;

/**
 * @Author: mmc
 * @Date: 2018/9/22 14:18
 * @Version 1.0
 *
 * 封装了返还的结果
 */
@Service
public class QueryResultImpl<T extends IdBase> implements QueryResultApi<T> {

    private static  final  Logger logger=LoggerFactory.getLogger(QueryResultImpl.class);

    @Autowired
    private TableApi tableApi;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthorithApi authorithApi;

    @Override
    public Result listData(Class clazz, Integer pageNo) {
        return this.listData(clazz,pageNo,null,null,null);
    }

    @Override
    public Result listData(Class clazz, Integer pageNo, String where, QueryApi.SQL_ORDER order) {
        return this.listData(clazz,pageNo,where,order,null);
    }

    @Override
    public Result listData(Class clazz, Integer pageNo, String where, QueryApi.SQL_ORDER order, Integer pageSize) {
        //参数检查
        if(pageNo==null||pageNo==0){
            pageNo=1;
        }
        if(order==null){
            order=QueryApi.SQL_ORDER.DESC;
        }
        if(pageSize==null){
            pageSize=PAGE_SIZE;
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        sql.append(StringUtils.hasLength(where) ? where : "1=1");
        sql.append(" and");
        sql.append(getAuthorithWhere(clazz));
        String countSql=sql.toString().replace("*","count(*)");
        int start = (pageNo - 1) * pageSize;
        int end = pageNo * pageSize;
        sql.append(" ORDER BY pkey " + order);
        sql.append(" limit " + start + "," + end);
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
        //加工
        List<Map<String, Object>> resultList = extJoinData(mapList);
        //计算总条数
        Integer totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
        return ResultUtil.writeSuccess(resultList,totalCount);
    }

    @Override
    public Result listData(Class clazz, Integer pageNo, String where, String orderFld, QueryApi.SQL_ORDER order, Integer pageSize) {
        //参数检查
        if(pageNo==null||pageNo==0){
            pageNo=1;
        }
        if(order==null){
            order=QueryApi.SQL_ORDER.DESC;
        }
        if(pageSize==null){
            pageSize=PAGE_SIZE;
        }
        if(!StringUtils.hasLength(orderFld)){
            orderFld="pkey";
        }
        if(!StringUtils.hasLength(where)){
            where="1=1";
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        sql.append(where);
        sql.append(" and");
        sql.append(getAuthorithWhere(clazz));
        //计算总条数的sql
        String countSql=sql.toString().replace("*","count(*)");
        //分页
        int start = (pageNo - 1) * pageSize;
        int end = pageNo * pageSize;
        sql.append(" ORDER BY ");
        //排序
        sql.append(orderFld +" "+order);
        sql.append(" limit " + start + "," + end);
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
        //数据加工,将关联表的id转换为对应的name
        List<Map<String, Object>> resultList = extJoinData(mapList);
        //计算总条数
        Integer totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
        return ResultUtil.writeSuccess(resultList,totalCount);
    }

    @Override
    public Result listCombobox(Class clazz) {
        return this.listCombobox(clazz,QueryApi.SQL_ORDER.DESC);
    }

    @Override
    public Result listCombobox(Class clazz, QueryApi.SQL_ORDER order) {
        try {
            ExtName extName = (ExtName) clazz.newInstance();
            String sql = MessageFormat.format("select pkey,{0} from {1} ORDER BY pkey {2}", extName.fldName(), tableApi.getTable(clazz),order);
            logger.info("执行数据库查询：" + sql);
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
            return ResultUtil.writeSuccess(mapList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultUtil.writeSuccess(Collections.emptyList());
    }

    private String getAuthorithWhere(Class clazz) {
       return authorithApi.getAuthorithWhere(clazz);
    }

    @Override
    public List<Map<String, Object>> extJoinData(List<Map<String, Object>> mapList) {
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

}
