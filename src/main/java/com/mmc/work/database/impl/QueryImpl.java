package com.mmc.work.database.impl;

import com.mmc.utils.SpringContextUtil;
import com.mmc.work.base.ExtName;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.TableApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;

/**
 * @Author: mmc
 * @Date: 2018/9/8 23:04
 * @Version 1.0
 */
@Component
public class QueryImpl implements QueryApi {

    Logger logger=LoggerFactory.getLogger(QueryImpl.class);

    public static final int PAGE_SIZE = 20;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableApi tableApi;



    @Override
    public List listData(Class clazz, Integer pageNo, String where, String order) {
            StringBuilder sql = new StringBuilder();
            sql.append("select * from " + tableApi.getTable(clazz) + " where ");
            sql.append(StringUtils.hasLength(where)?where:"1=1");
            sql.append(" and");
            sql.append(getAuthorithWhere(clazz));
            int start = (pageNo - 1) * PAGE_SIZE;
            int end = pageNo * PAGE_SIZE;
            sql.append(" ORDER BY pkey "+(order==null?"DESC":order));
            sql.append(" limit " + start + "," + end);
            logger.info("执行数据库查询：" + sql);
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
            extJoinData(mapList);
            return mapList;
    }

    @Override
    public List listCombobox(Class clazz) {
        try {
            ExtName extName = (ExtName) clazz.newInstance();
            String sql = MessageFormat.format("select pkey,{0} from {1} ORDER BY pkey DESC", extName.fldName(), tableApi.getTable(clazz));
            logger.info("执行数据库查询：" + sql);
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
            return mapList;
        }catch (Exception e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Object findFldByPkey(Class clazz, String fld, Integer pkey) {
        return this.findValueByPkey(tableApi.getTable(clazz),fld,pkey);
    }

    @Override
    public Object findByPkey(Class clazz, Integer pkey) {
        String sql="select * from "+tableApi.getTable(clazz)+" where pkey=?";
        logger.info("执行数据库查询：" + sql);
        return
                jdbcTemplate.queryForObject(
                sql, new Object[]{pkey},
                new BeanPropertyRowMapper(clazz));
    }

    @Override
    public List listByParams(Class clazz,String[] paramNames,Object... objects) {
        if(paramNames.length!=objects.length){
            throw new IllegalArgumentException("输入的属性名和属性值长度不同");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + tableApi.getTable(clazz) + " where ");
        for(String param:paramNames){
            sql.append(param+"= ? and ");
        }
        sql.append(getAuthorithWhere(clazz));
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
        return mapList;
    }

    @Override
    public List findOneFldByParams(Class clazz, String fld, String[] paramNames, Object... objects) {
        return this.findManyFldByParams(clazz,new String[]{fld},paramNames,objects);
    }

    @Override
    public List findManyFldByParams(Class clazz, String[] flds, String[] paramNames, Object... objects) {
        if(paramNames.length!=objects.length){
            throw new IllegalArgumentException("输入的属性名和属性值长度不同");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for(int i=0;i<flds.length;i++){
            sql.append(flds[i]);
            if(i<flds.length-1){
                sql.append(",");
            }
        }
        sql.append(" from " + tableApi.getTable(clazz) + " where ");
        for(String param:paramNames){
            sql.append(param+"= ? and ");
        }
        sql.append(getAuthorithWhere(clazz));
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
        return mapList;
    }

    @Override
    public List query(String sql, Object... objects) {
        logger.info("执行数据库查询：" + sql);
        return jdbcTemplate.queryForList(sql,objects);
    }

    /**
     * 获取权限的where语句
     * 判断是否是有家庭的，如果有按照家庭显示记录
     * @return
     */
    private String getAuthorithWhere(Class clazz){
        Integer family = SpringContextUtil.getLoginUser().getFamily();
        StringBuilder sql=new StringBuilder();
        if (family != null && tableApi.hasPropertis(clazz,"family")) {
            sql.append(" family=" + family);
        } else if(tableApi.hasPropertis(clazz,"user")){
            sql.append(" user=" + SpringContextUtil.getLoginUser().getUser());
        }else {
            sql.append(" pkey="+ SpringContextUtil.getLoginUser().getUser());
        }
        return sql.toString();
    }

    //对结果数据进行二次加工
    private List<Map<String, Object>> extJoinData(List<Map<String, Object>> mapList) {
        //缓存关联数据Map<表名,Map<主键，新数据>>
        Map<String, Map<Integer, Object>> cashMap = new HashMap<>();
        for (Map<String, Object> map : mapList) {
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                if(next.getValue() instanceof Date){
                    next.setValue(next.getValue().toString());
                }else if (next.getKey().contains("_id")){
                    setJoinWithPkey(cashMap,next);
                }
            }
        }
        return mapList;
    }


    /*  将数据中关联表的pkey替换为值
     * @return
    */
    private void setJoinWithPkey(Map<String, Map<Integer, Object>> cashMap,Map.Entry<String, Object> next){
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
            if(cashMap.get(extTable)!=null&&(joinValue=cashMap.get(extTable).get(joinPkey))!=null){

            }else {
                joinValue=findValueByPkey( extTable,instance.fldName(),joinPkey );
                Map<Integer,Object> objectMap=new HashMap<>();
                objectMap.put(joinPkey,joinValue);
                cashMap.put(extTable,objectMap);
            }
            next.setValue(joinValue);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据pkey查询一个表里的一个字段
     *
     * @param fld
     * @param table
     * @param pkey
     * @return
     */
    private Object findValueByPkey(String table,String fld, Integer pkey) {
        String extSql=MessageFormat.format("select {0} from {1} where pkey =?",fld,table);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(extSql, pkey);
        //仅会有一条数据
        while (sqlRowSet.next()) {
            return sqlRowSet.getObject(fld);
        }
        return "";
    }

}