package com.mmc.work.bean.marriage;

import com.mmc.work.base.IdBase;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * 用于绑定家庭与用户
 */
@Entity
@Data
@Table(indexes = {@Index(name = "user_index",columnList = "user",unique = true)})
public class Marriage extends IdBase {
    private Integer user;

    private Integer family;


}
