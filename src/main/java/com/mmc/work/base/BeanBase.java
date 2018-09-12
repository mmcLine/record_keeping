package com.mmc.work.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

/**
 * 实体类的基础表
 */
@Setter
@Getter
@MappedSuperclass
public abstract class BeanBase extends IdBase {

    private Integer user;

    private Integer family;

}
