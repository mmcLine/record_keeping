package com.mmc.work.database.impl;

import com.mmc.assist.login.Idu;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.AuthorithApi;
import com.mmc.work.database.api.TableApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: mmc
 * @Date: 2018/9/22 14:58
 * @Version 1.0
 */
@Component
public class AuthorithImpl implements AuthorithApi {

    @Autowired
    private TableApi tableApi;

    @Override
    public String getAuthorithWhere(Class clazz) {
        Integer family = Idu.getLoginUser().getFamily();
        //登录时查询
        if(Idu.getLoginUser().getUser()==null){
            return " 1=1 ";
        }
        if (family != null && tableApi.hasPropertis(clazz, "family")) {
            return " family=" + family;
        } else if (tableApi.hasPropertis(clazz, "user")) {
            return  " user=" + Idu.getLoginUser().getUser();
        } else if(clazz.equals(User.class)){
            return " pkey=" + Idu.getLoginUser().getUser();
        }else {
            return " 1=1 ";
        }
    }
}
