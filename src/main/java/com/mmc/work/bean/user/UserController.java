package com.mmc.work.bean.user;

import com.mmc.work.api.UserApi;
import com.mmc.assist.result.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.beans.PropertyDescriptor;

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

    public static void main(String[] args) {
        PropertyDescriptor family = BeanUtils.getPropertyDescriptor(User.class, "ff");
        System.out.println(family);
        System.out.println(family.isPreferred());
        System.out.println(family.isConstrained());
        System.out.println(family.isHidden());

    }
}
