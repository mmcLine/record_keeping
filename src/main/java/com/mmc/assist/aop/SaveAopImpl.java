package com.mmc.assist.aop;

import com.mmc.utils.SpringContextUtil;
import com.mmc.work.base.BeanBase;
import com.mmc.work.base.IdBase;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @Author: mmc
 * @Date: 2018/9/12 22:11
 * @Version 1.0
 */
@Aspect
@Component
public class SaveAopImpl {


    @Pointcut("execution(public boolean  com.mmc.work.database.api.InsertApi.insert(..))")// the pointcut expression
    private void anyOldTransfer() {}

    @Before("anyOldTransfer()")
    public void before(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        System.out.println(signature);
        Object[] args = joinPoint.getArgs();
        if(args!=null&&args.length>0){
            Object object=args[0];
            if(object instanceof BeanBase){
                BeanBase bean=(BeanBase)object;
                bean.setUser(SpringContextUtil.getLoginUser().getUser());
                bean.setFamily(SpringContextUtil.getLoginUser().getFamily());
                bean.setCreateTime(LocalDate.now());
            }else if(object instanceof IdBase){
                IdBase bean=(IdBase)object;
                bean.setCreateTime(LocalDate.now());
            }
        }
    }
}
