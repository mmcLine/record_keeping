package com.mmc.work.bean.user;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SHA512;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.UpdateApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mmc
 * @Date: 2018/9/5 23:00
 * @Version 1.0
 */
@Service
public class UserService {

    Logger logger=LoggerFactory.getLogger(UserService.class);

    @Autowired
    private QueryResultApi<User> queryResultApi;

    @Autowired
    private QueryApi<User> queryApi;

    @Autowired
    private InsertApi insertApi;

    @Autowired
    private UpdateApi updateApi;

    public Result list(Integer pageNo){
        return queryResultApi.listData(User.class,pageNo);
    }

    public Result save(User user){
        //新增
        if(user.getPkey()==null){
            user.setPassword(SHA512.encry256(user.getPassword()));
            insertApi.insert(user);
        }else{
           String passwd= queryApi.findFldByPkey(User.class,"password",user.getPkey()).toString();
           user.setPassword(passwd);
           updateApi.update(user);
        }
        return ResultUtil.writeSuccess();
    }

    public Result updPasswd(Integer user,String oldPasswd,String newPasswd){
        User user1 = queryApi.findByPkey(User.class,user);
        if(user1==null){
            logger.warn("非法用户修改密码");
            return ResultUtil.writeError();
        }else {
            if(user1.getPassword().equals(SHA512.encry256(oldPasswd))){
                user1.setPassword(SHA512.encry256(newPasswd));
                updateApi.update(user1);
                return ResultUtil.writeSuccess();
            }else {
                logger.warn("输入的原密码不对");
                return ResultUtil.writeError();
            }
        }
    }

}
