package com.mmc.work.api;

import com.mmc.assist.result.Result;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: mmc
 * @Date: 2018/9/7 23:53
 * @Version 1.0
 */
public interface MarriageApi {

    @RequestMapping("record/marriage/list")
    public Result list(Integer pageNo);


    @RequestMapping("record/marriage/buildFamily")
    public Result buildFamily(String name,String tel,String password);
}
