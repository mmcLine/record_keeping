package com.mmc.work.database.impl;

import com.mmc.work.base.IdBase;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.TableApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mmc
 * @Date: 2018/9/11 22:32
 * @Version 1.0
 */
@Component
public class InsertImpl<T extends IdBase> implements InsertApi<T> {

    private Logger logger =LoggerFactory.getLogger(InsertImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableApi tableApi;

    @Override
    public Integer insert(T t){
        StringBuilder sql = new StringBuilder();
        sql.append("insert into " + tableApi.getTable(t.getClass()) + "(");
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(t);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        List<Object> valueList = new ArrayList<>();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            String name = propertyDescriptors[i].getName();
            if (name.equals("class")) {
                continue;
            }
            if (beanWrapper.getPropertyValue(name) != null) {
                sql.append(tableApi.getSqlCode(name));
                sql.append(",");
                valueList.add(beanWrapper.getPropertyValue(name));
            }
        }
        sql.replace(sql.length()-1,sql.length(),")");
        sql.append("values (");
        for (int i = 0; i < valueList.size(); i++) {
            if (i < valueList.size() - 1) {
                sql.append("?,");
            } else {
                sql.append("?");
            }
        }
        sql.append(")");
        logger.info("执行插入数据库插入:"+sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator(){
                    public java.sql.PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                        java.sql.PreparedStatement ps = conn.prepareStatement(sql.toString());
                        ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
                        int i = 0;
                        for(Object object:valueList){
                            ps.setObject(++i,object);
                        }
                        return ps;
                    }
                },
                keyHolder);
        return keyHolder.getKey().intValue();
    }

}
