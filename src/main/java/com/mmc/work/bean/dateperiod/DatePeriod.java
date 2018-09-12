package com.mmc.work.bean.dateperiod;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @Author: mmc
 * @Date: 2018/9/1 11:03
 * @Version 1.0
 * 用于接收前台传回的日期区间参数
 */
@Data
public class DatePeriod {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}
