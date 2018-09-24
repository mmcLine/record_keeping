package com.mmc.assist.home;

import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.result.Result;
import com.mmc.work.bean.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/9/19 21:50
 * @Version 1.0
 */
@RestController
public class HomeController {


    @Autowired
    private HomeService homeService;

    @Autowired
    private OrderService orderService;

    /**
     * 首页的计算金额和笔数
     * @return
     */
    @PostMapping("record/home/calculation")
    public Result calculation(){
       return homeService.getTotalAmtAndNumber();
    }

    @PostMapping("record/home/logininfo")
    public Result loginInfo(){
        return homeService.loginInfo();
    }

    @RequestMapping("record/order/monthchart")
    public JSONObject monthchart(){
        return orderService.monthchart();
    }
}
