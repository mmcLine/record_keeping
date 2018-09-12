package com.mmc.work.bean.family;

import com.mmc.work.base.IdBase;
import lombok.Data;

import javax.persistence.Entity;

@Data
@Entity
public class Family extends IdBase {
    private String name;
}
