package com.mmc.work.bean.order;

import com.mmc.assist.result.Result;
import com.mmc.work.api.OrderApi;
import com.mmc.work.bean.dateperiod.DatePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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

}
