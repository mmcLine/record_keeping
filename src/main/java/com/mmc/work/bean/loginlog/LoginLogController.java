package com.mmc.work.bean.loginlog;

import com.mmc.assist.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: mmc
 * @Date: 2018/9/19 22:57
 * @Version 1.0
 */
@Controller
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    @PostMapping("record/loginlog/list")
    @ResponseBody
    public Result listLog(Integer pageNo){
        return loginLogService.list(pageNo);
    }
}
