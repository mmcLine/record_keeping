package com.mmc.work.database.impl;

import com.mmc.utils.RecordException;
import com.mmc.work.base.IdBase;
import com.mmc.work.database.api.TableApi;
import com.mmc.work.database.api.UpdateApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: mmc
 * @Date: 2018/9/10 22:46
 * @Version 1.0
 */
@Component
public class UpdateImpl<T extends IdBase> implements UpdateApi<T> {

    private Logger logger=LoggerFactory.getLogger(UpdateImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TableApi tableApi;

    @Override
    public boolean update(T object) {
        if(object.getPkey()==null){
            throw new RecordException("UpdateImpl","修改操作时，对象必须有主键");
        }
        StringBuilder sql=new StringBuilder();
        sql.append("update "+tableApi.getTable(object.getClass())+" set ");
        int j=0;
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(object);
        PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
        List<Object> valueList=new ArrayList<>();
        for(int i=0;i< propertyDescriptors.length;i++){
            String name = propertyDescriptors[i].getName();
            if(name.equals("class")||name.equals("pkey")){
                continue;
            }
            Object value = beanWrapper.getPropertyValue(name);
            if(value!=null){
                sql.append(tableApi.getSqlCode(name));
                sql.append("=?,");
                valueList.add(value);
            }
        }
        valueList.add(beanWrapper.getPropertyValue("pkey"));
        sql.deleteCharAt(sql.length()-1);
        sql.append(" where pkey=?");
        logger.info("执行数据库修改操作："+sql);
        return jdbcTemplate.update(sql.toString(),valueList.toArray())>0;
    }
}
