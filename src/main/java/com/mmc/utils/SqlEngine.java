package com.mmc.utils;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.base.ExtName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.Table;
import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

@Component
public class SqlEngine {

    Logger logger = LoggerFactory.getLogger(SqlEngine.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;


    /*********************列表查询*************************/

    public Result listBean(Class clazz, int pageNo,String where,String order) {
        return this.listBeanWhere(clazz, pageNo, where,order);
    }

    public Result listBean(Class clazz, int pageNo,String where) {
      return this.listBeanWhere(clazz, pageNo, where,"DESC");
    }

    public Result listBean(Class clazz, int pageNo) {
       return this.listBeanWhere(clazz,pageNo,"1=1","DESC");
    }


    /*********************列表查询*************************/



    /**
     * 下拉框查询
     * @param clazz
     * @return
     */
    public Result listComboBean(Class clazz){
        String table=getTable(clazz);
        try {
            ExtName extName=(ExtName) clazz.newInstance();
            String sql=MessageFormat.format("select pkey,{0} from {1} ORDER BY pkey DESC",extName.fldName(),table);
            logger.info("执行数据库查询：" + sql);
            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
           return ResultUtil.writeSuccess(mapList);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ResultUtil.writeError();
    }


    public Result delete(String className,String pkeys) {
        String[] pkeyArray = pkeys.split(",");
        String table = getTable(className);
        int[] dels=jdbcTemplate.batchUpdate("delete from " + table + " where pkey=?",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, Integer.valueOf(pkeyArray[i]));
                    }

                    public int getBatchSize() {
                        return pkeyArray.length;
                    }
                });
        return ResultUtil.writeSuccess(dels);
    }

    private Result listBeanWhere(Class clazz, int pageNo,String where,String order) {
        Integer family = SpringContextUtil.getLoginUser().getFamily();
        StringBuilder sql = new StringBuilder();
        sql.append("select * from " + getTable(clazz) + " where ");
        sql.append(StringUtils.hasLength(where)?where:"1=1");
        sql.append(" and");
        if (family != null && hasPropertis(clazz,"family")) {
            sql.append(" family=" + family);
        } else if(hasPropertis(clazz,"user")){
            sql.append(" user=" + SpringContextUtil.getLoginUser().getUser());
        }else {
            sql.append(" pkey="+ SpringContextUtil.getLoginUser().getUser());
        }
        int start = (pageNo - 1) * ResultUtil.PAGE_SIZE;
        int end = pageNo * ResultUtil.PAGE_SIZE;
        sql.append(" ORDER BY pkey "+order);
        sql.append(" limit " + start + "," + end);
        logger.info("执行数据库查询：" + sql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql.toString());
        extJoinData(mapList);
        return ResultUtil.writeSuccess(mapList);
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

    /**
     *
     *  将数据中关联表的pkey替换为值
     * @return
     */
    private void setJoinWithPkey(Map<String, Map<Integer, Object>> cashMap,Map.Entry<String, Object> next){
            try {
                //根据命名规则获取表名
                String extTable = next.getKey().replace("_id", "");
                //组装全类名
                String fullClassName = "com.mmc.work.bean." + extTable.replace("_", "") + "."
                        + getClassName(extTable);
                ExtName instance = (ExtName) Class.forName(fullClassName).newInstance();
                //要关联的字段的主键
                Integer joinPkey = Integer.valueOf(next.getValue().toString());
                //先检查缓存里是否有数据
                Object joinValue;
                if(cashMap.get(extTable)!=null&&(joinValue=cashMap.get(extTable).get(joinPkey))!=null){

                }else {
                    joinValue=findValueByPkey(instance.fldName(), extTable,joinPkey );
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
    public Object findValueByPkey(String fld, String table, Integer pkey) {
        String extSql=MessageFormat.format("select {0} from {1} where pkey =?",fld,table);
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(extSql, pkey);
        //仅会有一条数据
        while (sqlRowSet.next()) {
            return sqlRowSet.getObject(fld);
        }
        return "";
    }


    /**
     * 根据类名获取表名
     * @param className
     * @return
     */
    private String getTable(String className){
        String fullClassName = "com.mmc.work.bean." + className.toLowerCase() + "."
                + className;
        try {
            return this.getTable( Class.forName(fullClassName));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 根据类获取表名，考虑注解定义了表名的情况
     * @param clazz
     * @return
     */
    private String getTable(Class clazz){
        String className = clazz.getName();
        //判断是否指定了表名
        Table annotation = (Table) clazz.getAnnotation(Table.class);
        if (annotation != null && StringUtils.hasLength(annotation.name())) {
            return annotation.name();
        } else {
            return getTableByName(className.substring(className.lastIndexOf(".")+1));
        }
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

    private String getClassName(String table) {
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

    private boolean hasPropertis(Class clazz,String fldName){
        try{
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, fldName);
            return propertyDescriptor!=null;
        }catch (Exception e){
            return false;
        }
    }

}
