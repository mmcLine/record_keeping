package com.mmc.work.bean.user;


import com.mmc.work.base.IdBase;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public  class User extends IdBase {

    private String name;

    private String tel;

    private String email;

    private String headPortrait;

    private String password;

}
