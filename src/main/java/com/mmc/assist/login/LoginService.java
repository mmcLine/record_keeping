package com.mmc.assist.login;

import com.mmc.utils.SHA512;
import com.mmc.work.bean.user.User;
import com.mmc.work.bean.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    public User checkLogin(String username,String password){
        User user;
        if(username.contains("@")){
            user=userRepository.findByEmail(username);
        }else{
            user=userRepository.findByTel(username);
        }
        if(user!=null){
            if(user.getPassword().equals(SHA512.encry256(password))){
                return user;
            }
        }
        return null;
    }

}
