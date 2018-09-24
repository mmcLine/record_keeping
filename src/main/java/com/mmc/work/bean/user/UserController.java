package com.mmc.work.bean.user;

import com.mmc.assist.result.Result;
import com.mmc.work.api.UserApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Override
    public Result regist(User user) {
        return userService.save(user);
    }

    @Override
    public Result list(Integer pageNo) {
        return userService.list(pageNo);
    }

    @Override
    public Result updPasswd(Integer user,String oldPasswd, String newPasswd) {
        return userService.updPasswd(user,oldPasswd,newPasswd);
    }


}
