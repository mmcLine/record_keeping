package com.mmc.work.bean.order;

import com.mmc.work.base.BeanBase;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "trade_order",indexes = {@Index(name = "INDEX_USER",columnList = "user"),@Index(name = "INDEX_FAMILY",columnList = "family")})
public class Order extends BeanBase {

    private String name;

    private Double amt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate tradeDate;

    private Integer tradeTypeId;

    private String remarks;


}
