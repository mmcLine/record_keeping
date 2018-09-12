package com.mmc.work.bean.menstruation;

import com.mmc.work.base.BeanBase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * 大姨妈记录
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class Menstruation extends BeanBase {

    //来大姨妈的日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mensDate;

    //月经周期
    private int cycle;
}
