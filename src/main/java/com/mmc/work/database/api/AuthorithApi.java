package com.mmc.work.database.api;

import org.springframework.stereotype.Component;

/**
 * @Author: mmc
 * @Date: 2018/9/22 14:47
 * @Version 1.0
 */
@Component
public interface AuthorithApi {

    /**
     * 获取权限的where语句
     * 判断是否是有家庭的，如果有按照家庭显示记录
     * @param clazz
     * @return
     */
    String getAuthorithWhere(Class clazz);
}
