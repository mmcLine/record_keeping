package com.mmc.assist.home;

import com.mmc.assist.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
}
