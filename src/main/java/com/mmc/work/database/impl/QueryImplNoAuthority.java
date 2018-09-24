package com.mmc.work.database.impl;

import org.springframework.stereotype.Service;

/**
 * @Author: mmc
 * @Date: 2018/9/21 0:04
 * @Version 1.0
 * 不带权限控制的
 */
@Service("queryImplNoAuthority")
public class QueryImplNoAuthority<T> extends QueryImpl<T>{

    @Override
    public String getAuthorithWhere(Class clazz) {
        return " 1=1 ";
    }
}
