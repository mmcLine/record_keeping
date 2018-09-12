package com.mmc.work.bean.marriage;

import com.mmc.work.api.MarriageApi;
import com.mmc.assist.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/9/7 23:56
 * @Version 1.0
 */
@RestController
public class MarriageController implements MarriageApi {

    @Autowired
    private MarriageService marriageService;


    @Override
    public Result list(Integer pageNo) {
        return marriageService.list(pageNo);
    }

    @Override
    public Result buildFamily(String name, String tel, String password) {
        //先建立家庭
       return marriageService.buildFamily(name, tel, password);
    }
}
