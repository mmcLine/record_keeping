package com.mmc.work.bean.marriage;

import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.login.Idu;
import com.mmc.assist.login.LoginService;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.bean.family.Family;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MarriageService {

    Logger logger=LoggerFactory.getLogger(MarriageService.class);


    @Autowired
    private LoginService loginService;

    @Autowired
    private QueryApi queryApi;

    @Autowired
    private InsertApi insertApi;

    public Result buildFamily(String name, String tel, String password) {
        User user=loginService.checkLogin(tel,password);
        //判断要结合的用户是否存在并且不能是自己
        if(user!=null&&user.getPkey()!=Idu.getLoginUser().getUser()){
        //先建立一个家庭
            Family family=new Family();
            family.setName(name);
            Integer newFamily = insertApi.insert(family);
                //新增对方信息
            Marriage marriage=new Marriage();
            marriage.setUser(user.getPkey());
            marriage.setFamily(newFamily);
            insertApi.insert(marriage);

            Marriage partner=new Marriage();
            partner.setUser(Idu.getLoginUser().getUser());
            partner.setFamily(newFamily);
            insertApi.insert(partner);
            return ResultUtil.writeSuccess();
        }else {
            logger.error("用户{0}试图与用户{1}组建家庭失败",Idu.getLoginUser().getUser(),tel);
            return ResultUtil.writeError();
        }
    }

    public Result list(Integer pageNo){
        if(Idu.getLoginUser().getFamily()!=null){
            List<Map<String,Object>> userList = queryApi.listOneFldByParams(Marriage.class, "user", new String[]{"family"}, Idu.getLoginUser().getFamily());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",queryApi.findFldByPkey(Family.class,"name",(int)Idu.getLoginUser().getFamily()));
            if(userList.size()==1){
                //权限控制了查不到对方数据
                jsonObject.put("member", userList.get(0).get("user_name")+"   ❤    保密");
            }else {
                jsonObject.put("member", userList.get(0).get("user_name")+"   ❤    "+userList.get(1).get("user_name"));
            }

            return ResultUtil.writeSuccess(jsonObject);
        }else{
            return ResultUtil.writeSuccess(new JSONObject().fluentPut("name","单身狗").fluentPut("member","没对象"));
        }
    }
}
