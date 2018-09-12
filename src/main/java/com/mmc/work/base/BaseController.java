package com.mmc.work.base;

import com.mmc.assist.result.Result;
import com.mmc.utils.SqlEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 *
 * 处理通用请求
 * @Author: mmc
 * @Date: 2018/8/31 21:18
 * @Version 1.0
 */
@RestController
public class BaseController {

    @Autowired
    private SqlEngine sqlEngine;

    @RequestMapping("record/del")
    public Result delete(@RequestParam("className") @NotNull String className, String pkeys){
       return sqlEngine.delete(className,pkeys);
    }
}
