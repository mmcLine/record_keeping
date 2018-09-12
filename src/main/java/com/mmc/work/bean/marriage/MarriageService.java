package com.mmc.work.bean.marriage;

import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.login.LoginService;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SpringContextUtil;
import com.mmc.utils.SqlEngine;
import com.mmc.work.bean.family.Family;
import com.mmc.work.bean.family.FamilyRepository;
import com.mmc.work.bean.user.User;
import com.mmc.work.bean.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MarriageService {

    Logger logger=LoggerFactory.getLogger(MarriageService.class);

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MarriageRepository marriageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SqlEngine sqlEngine;

    public Result buildFamily(String name, String tel, String password) {
        User user=loginService.checkLogin(tel,password);
        //判断要结合的用户是否存在并且不能是自己
        if(user!=null&&user.getPkey()!=SpringContextUtil.getLoginUser().getUser()){
        //先建立一个家庭
        Family family=new Family();
        family.setName(name);
        family.setCreateTime(LocalDate.now());
        Family saveFamily = familyRepository.save(family);
            //新增对方信息
        Marriage marriage=new Marriage();
        marriage.setUser(user.getPkey());
        marriage.setFamily(saveFamily.getPkey());
        marriageRepository.save(marriage);

        Marriage partner=new Marriage();
        partner.setUser(SpringContextUtil.getLoginUser().getUser());
        partner.setFamily(saveFamily.getPkey());
        marriageRepository.save(partner);
        return ResultUtil.writeSuccess();
        }else {
            logger.error("用户{0}试图与用户{1}组建家庭失败",SpringContextUtil.getLoginUser().getUser(),tel);
            return ResultUtil.writeError();
        }
    }

    public Result list(Integer pageNo){
        if(SpringContextUtil.getLoginUser().getFamily()!=null){
            List<Marriage> marriageList = marriageRepository.findByFamily(SpringContextUtil.getLoginUser().getFamily());
            List<User> userList = userRepository.findAllById(marriageList.stream().map(x -> x.getUser()).collect(Collectors.toList()));
            Optional<Family> family = familyRepository.findById(SpringContextUtil.getLoginUser().getFamily());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",family.get().getName());
            jsonObject.put("member", userList.get(0).getName()+"   ❤    "+userList.get(1).getName());
            return ResultUtil.writeSuccess(jsonObject);
        }else{
            return ResultUtil.writeSuccess(new JSONObject().fluentPut("name","单身狗").fluentPut("member","没对象"));
        }
    }
}
