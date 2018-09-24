package com.mmc.assist.login;

import com.mmc.utils.RecordException;

/**
 * @Author: mmc
 * @Date: 2018/9/20 22:09
 * @Version 1.0
 * 保存登录的对象
 */

public final class Idu {

    public static final String LOGIN_USER="record_login_user";

    private static ThreadLocal<MyUser> userThreadLocal=new ThreadLocal<>();

    public static void init(MyUser myUser){
        userThreadLocal.set(myUser);
    }

    public static MyUser getLoginUser(){
        MyUser myUser = userThreadLocal.get();
        if(myUser==null){
            throw new RecordException("Idu","未获取到登录用户");
        }else {
            return myUser;
        }

    }


}
