package com.mmc.work.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * 主键类型基础表
 */
@Setter
@Getter
@MappedSuperclass
public class IdBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pkey;

    private LocalDateTime createTime;
}
