package com.mmc.assist.result;

import com.mmc.utils.SHA512;

import java.util.Collection;

public class ResultUtil {

    public static final int PAGE_SIZE = 20;

    public static Result writeSuccess(){
        Result result=new Result();
        result.setCode("200");
        result.setSuccess(true);
        result.setMsg("Option Success");
        result.setData(new Result.Data());
        return result;
    }

    public static Result writeError(){
        Result result=new Result();
        result.setCode("100");
        result.setSuccess(false);
        result.setMsg("Option Error");
        result.setData(null);
        return result;
    }

    public static Result writeSuccess(Object data){
        Result result=new Result();
        result.setCode("200");
        result.setSuccess(true);
        result.setMsg("Option Success");
        if(data instanceof Collection){
            int pageSize = (((Collection) data).size()  +  PAGE_SIZE  - 1) / PAGE_SIZE;
            result.setData(new Result.Data(data,pageSize));
        }else {
            result.setData(new Result.Data(data,0));
        }

        return result;
    }

    public static Result writeLogout(){
        Result result=new Result();
        result.setCode("1600");
        result.setSuccess(false);
        result.setMsg("登录失效,请重新登录!");
        result.setData(null);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(SHA512.encry256("123456"));
    }
}
