package com.mmc.work.bean.marriage;

import com.mmc.work.base.IdBase;
import lombok.Data;

import javax.persistence.Entity;

/**
 * 用于绑定家庭与用户
 */
@Entity
@Data
public class Marriage extends IdBase {
    private Integer user;

    private Integer family;


}
