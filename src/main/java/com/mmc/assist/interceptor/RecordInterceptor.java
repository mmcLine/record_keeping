package com.mmc.assist.interceptor;

import com.alibaba.fastjson.JSON;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SpringContextUtil;
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

public class RecordInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(SpringContextUtil.getLoginUser().getUser()!=null){
            LoggerFactory.getLogger(RecordInterceptor.class).info("验证用户登录中...已登录,请继续操作");
            return true;
        }else{
            LoggerFactory.getLogger(UserController.class).warn("验证用户登录中...未登录,立即跳转登录页面");
            response.setHeader("Access-Control-Allow-Origin","*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
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
