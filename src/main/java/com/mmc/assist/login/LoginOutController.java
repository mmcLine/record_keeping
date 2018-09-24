package com.mmc.assist.login;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Author: mmc
 * @Date: 2018/9/20 16:20
 * @Version 1.0
 */
@RestController
public class LoginOutController {

    private static final Logger logger=LoggerFactory.getLogger(LoginOutController.class);

    //清理缓存的登录数据
    @PostMapping("record/loginOut")
    public Result loginOut(HttpSession httpSession){
        logger.info("用户:["+Idu.getLoginUser().getUser()+"]退出登录");
        httpSession.removeAttribute(Idu.LOGIN_USER);
        return ResultUtil.writeSuccess();
    }
}
