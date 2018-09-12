package com.mmc.assist.login;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SpringContextUtil;
import com.mmc.work.bean.marriage.Marriage;
import com.mmc.work.bean.marriage.MarriageRepository;
import com.mmc.work.bean.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MarriageRepository marriageRepository;

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username,String password){
        User user=loginService.checkLogin(username,password);
        if(user!=null){
            //存登录用户
            Marriage marriage = marriageRepository.findByUser(user.getPkey());
            if(marriage!=null){
                SpringContextUtil.getLoginUser().setFamily(marriage.getFamily());
            }
            SpringContextUtil.getLoginUser().setUser(user.getPkey());
            return ResultUtil.writeSuccess(user);
        }else {
            Result result = ResultUtil.writeLogout();
            result.setMsg("用户名或密码错误");
            return result;
        }

    }
}
