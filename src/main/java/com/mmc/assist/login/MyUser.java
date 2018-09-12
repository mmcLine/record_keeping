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
}
