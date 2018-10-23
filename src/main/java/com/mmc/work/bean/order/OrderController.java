package com.mmc.work.bean.order;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.api.OrderApi;
import com.mmc.work.bean.dateperiod.DatePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class OrderController implements OrderApi {

    @Autowired
    private OrderService orderService;

    @Override
    public Result orderList(int pageNo){

        return orderService.orderList(pageNo);
    }

    @Override
    public Result updOrder(Order order) {
        return orderService.updOrder(order);
    }



    @Override
    public Result orderList(DatePeriod datePeriod) {
        return orderService.listByDate(datePeriod.getStartDate(),datePeriod.getEndDate());
    }

    @Override
    public Result importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        orderService.importExcel(file.getInputStream());
        return ResultUtil.writeSuccess();
    }

    @Override
    public Result names() {
        return orderService.names();
    }

}
