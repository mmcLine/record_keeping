package com.mmc.assist.login;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SpringContextUtil;
import com.mmc.work.bean.loginlog.LoginLog;
import com.mmc.work.bean.loginlog.LoginLogService;
import com.mmc.work.bean.marriage.Marriage;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.QueryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private QueryApi queryApi;

    @Autowired
    private LoginLogService loginLogService;

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password, HttpServletRequest request){
        User user=loginService.checkLogin(username,password);
        if(user!=null){
            //存登录用户
            Optional family = queryApi.findFldByParams(Marriage.class,"family",new String[]{"user"},user.getPkey());
            if(family.isPresent()){
                SpringContextUtil.getLoginUser().setFamily(Integer.valueOf(family.toString()));
            }
            SpringContextUtil.getLoginUser().setUser(user.getPkey());

            //存登录日志
            LoginLog loginLog=new LoginLog();
            loginLog.setIp(request.getRemoteAddr());
            String userAgent= request.getHeader("User-Agent");
            loginLog.setBrowser(loginLogService.getBrowser(userAgent));
            loginLogService.addLoginLog(loginLog);

            return ResultUtil.writeSuccess(user);
        }else {
            Result result = ResultUtil.writeLogout();
            result.setMsg("用户名或密码错误");
            return result;
        }

    }
}
