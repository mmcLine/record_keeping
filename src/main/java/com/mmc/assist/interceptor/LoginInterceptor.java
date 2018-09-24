package com.mmc.assist.interceptor;

import com.alibaba.fastjson.JSON;
import com.mmc.assist.login.Idu;
import com.mmc.assist.login.MyUser;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.bean.user.UserController;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: mmc
 * @Date: 2018/8/31 0:06
 * @Version 1.0
 */

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MyUser myUser=(MyUser) request.getSession().getAttribute(Idu.LOGIN_USER);
        if(myUser!=null){
            LoggerFactory.getLogger(LoginInterceptor.class).info("验证用户登录中...已登录,请继续操作");
            Idu.init(myUser);
            return true;
        }else{
            LoggerFactory.getLogger(UserController.class).warn("验证用户登录中...未登录,立即跳转登录页面");
            response.setHeader("Access-Control-Allow-Origin","http://www.mmcao.top:8091");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
//            response.setCharacterEncoding("utf-8");
            response.getWriter().print(JSON.toJSON(ResultUtil.writeLogout()));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
