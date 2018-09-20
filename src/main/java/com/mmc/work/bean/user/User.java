package com.mmc.work.bean.user;


import com.mmc.work.base.ExtName;
import com.mmc.work.base.IdBase;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public  class User extends IdBase implements ExtName {

    private String name;

    private String tel;

    private String email;

    private String headPortrait;

    private String password;

    @Override
    public String name() {
        return getName();
    }

    @Override
    public String fldName() {
        return "name";
    }
}
