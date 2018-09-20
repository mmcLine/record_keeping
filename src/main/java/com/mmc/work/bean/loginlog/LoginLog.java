package com.mmc.work.bean.loginlog;

import com.mmc.work.base.BeanBase;
import lombok.Data;

import javax.persistence.Entity;

/**
 * @Author: mmc
 * @Date: 2018/9/19 22:46
 * @Version 1.0
 * 登录日志
 */
@Entity
@Data
public class LoginLog extends BeanBase {

    private String ip;

    private String browser;
}
