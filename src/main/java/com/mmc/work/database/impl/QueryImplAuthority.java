package com.mmc.work.database.impl;

import com.mmc.work.database.api.AuthorithApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mmc
 * @Date: 2018/9/8 23:04
 * @Version 1.0
 * 带权限判断
 */
@Service("queryApi")
public class QueryImplAuthority<T> extends QueryImpl<T> {


    @Autowired
    private AuthorithApi authorithApi;

    @Override
    public String getAuthorithWhere(Class clazz) {
        return authorithApi.getAuthorithWhere(clazz);
    }

}
