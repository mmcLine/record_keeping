package com.mmc.assist.login;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
public class MyUser {
    private Integer user;
    private Integer family;
    //权限范围选择，如选择家庭，则值为0，默认选择当前登录的用户
    private Integer checkedUser;
}
