package com.mmc.work.bean.user;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SHA512;
import com.mmc.utils.SqlEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @Author: mmc
 * @Date: 2018/9/5 23:00
 * @Version 1.0
 */
@Service
public class UserService {

    Logger logger=LoggerFactory.getLogger(UserService.class);

    @Autowired
    private SqlEngine sqlEngine;

    @Autowired
    private UserRepository userRepository;

    public Result list(Integer pageNo){
        return sqlEngine.listBean(User.class,pageNo);
    }

    public Result save(User user){
        //新增
        if(user.getPkey()==null){
            user.setCreateTime(LocalDate.now());
            user.setPassword(SHA512.encry256(user.getPassword()));
        }else{
           String passwd= userRepository.findById(user.getPkey()).get().getPassword();
           user.setPassword(passwd);
        }
        userRepository.save(user);
        return ResultUtil.writeSuccess();
    }

    public Result updPasswd(Integer user,String oldPasswd,String newPasswd){
        Optional<User> user1 = userRepository.findById(user);
        if(!user1.isPresent()){
            logger.warn("非法用户修改密码");
            return ResultUtil.writeError();
        }else {
            if(user1.get().getPassword().equals(SHA512.encry256(oldPasswd))){
                User user2 = user1.get();
                user2.setPassword(SHA512.encry256(newPasswd));
                userRepository.save(user2);
                return ResultUtil.writeSuccess();
            }else {
                logger.warn("输入的原密码不对");
                return ResultUtil.writeError();
            }
        }
    }

}
