package com.mmc.work.bean.profit;

import com.mmc.work.base.BeanBase;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDate;

/**
 * @Author: mmc
 * @Date: 2018/10/23 23:02
 * @Version 1.0
 * 盈利表
 */

@Data
@Entity
public class Profit extends BeanBase {

    private String name;

    private Double amt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradeDate;

    private String remarks;
}
