package com.mmc.assist.login;

import com.mmc.utils.SHA512;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.QueryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class LoginService {


    @Autowired
    @Qualifier("queryImplNoAuthority")
    private QueryApi<User> queryApi;

    public User checkLogin(String username,String password){
        User user;
        if(username.contains("@")){
            user=queryApi.findByParams(User.class,new String[]{"email"},username);
        }else{
            user=queryApi.findByParams(User.class,new String[]{"tel"},username);
        }
        if(user!=null){
            if(user.getPassword().equals(SHA512.encry256(password))){
                return user;
            }
        }
        return null;
    }


}
