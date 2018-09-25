package com.mmc.assist.home;

import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.login.Idu;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
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

    @PostMapping("record/home/scopList")
    public Result scopList(){
        return homeService.scopList();
    }

    /**
     * 修改用户查询数据的权限
     * 支持查询单个用户和整个家庭
     * @return
     */
    @PostMapping("record/home/updateScope")
    public Result updateScope(Integer user){
        Idu.getLoginUser().setCheckedUser(user);
        return ResultUtil.writeSuccess();
    }

    @PostMapping("record/home/getCheckedUser")
    public Result getCheckedUser(){
        return ResultUtil.writeSuccess(Idu.getLoginUser().getCheckedUser());
    }
}
