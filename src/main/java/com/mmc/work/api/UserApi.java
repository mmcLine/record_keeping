package com.mmc.work.api;

import com.mmc.assist.result.Result;
import com.mmc.work.bean.user.User;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: mmc
 * @Date: 2018/9/5 22:49
 * @Version 1.0
 */
public interface UserApi {

    @RequestMapping("record/user/regist")
    public Result regist(User user);

    @RequestMapping("record/user/list")
    public Result list(Integer pageNo);

    @RequestMapping("record/user/updpasswd")
    public Result updPasswd(Integer user,String oldPasswd,String newPasswd);
}
