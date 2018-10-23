package com.mmc.work.api;

import com.mmc.assist.result.Result;
import com.mmc.work.bean.dateperiod.DatePeriod;
import com.mmc.work.bean.order.Order;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface OrderApi {

    @RequestMapping("record/order/list")
    Result orderList(int pageNo);

    @RequestMapping("record/order/save")
    Result updOrder(Order order);

    //日期搜索
    @RequestMapping("record/order/datelist")
    Result orderList(DatePeriod datePeriod);

    //数据导入
    @RequestMapping("record/order/importExcel")
    Result importExcel(@RequestParam("file") MultipartFile file) throws IOException;

    //输入建议搜索，返回近期的输入项
    @RequestMapping("record/order/names")
    Result names();
}
