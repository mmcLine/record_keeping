package com.mmc.work.bean.study;

import com.mmc.work.base.BeanBase;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDate;

@Data
@Entity
public class Study extends BeanBase {

    //学习内容
    private String studyContent;

    //要完成的目标
    private String aim;

    //开始学习日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    //预计完成日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    //实际完成日期
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate achieveDate;

    //完成情况
    private String result;

}
