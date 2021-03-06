package com.mmc.assist.home;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.login.Idu;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.bean.loginlog.LoginLog;
import com.mmc.work.bean.marriage.Marriage;
import com.mmc.work.bean.order.Order;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.QueryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: mmc
 * @Date: 2018/9/19 21:52
 * @Version 1.0
 */
@Service
public class HomeService {

    @Autowired
    private QueryApi queryApi;

    @Autowired
    @Qualifier("queryImplNoAuthority")
    private QueryApi queryApiNoAuth;


    public Result getTotalAmtAndNumber(){
        String sql="SELECT CAST( SUM(amt) AS DECIMAL (15, 2))  as monthamt FROM trade_order WHERE DATE_FORMAT(trade_date,'%Y%m')=DATE_FORMAT(CURDATE(),'%Y%m')";
        String sql2="SELECT CAST( SUM(amt) AS DECIMAL (15, 2))  AS totalamt,COUNT(*) AS totalcount FROM trade_order";
        List<Map<String,Object>> list = queryApi.query(Order.class,sql);
        List<Map<String,Object>> list2 = queryApi.query(Order.class,sql2);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("monthamt",list.get(0).get("monthamt"));
        jsonObject.put("totalamt",list2.get(0).get("totalamt"));
        jsonObject.put("totalcount",list2.get(0).get("totalcount"));
        return ResultUtil.writeSuccess(jsonObject);
    }

    public Result loginInfo(){
        String sql="SELECT ip,create_time FROM login_log where 1=1 ORDER BY create_time DESC LIMIT 1,1";
        return ResultUtil.writeSuccess(queryApi.query(LoginLog.class,sql));
    }

    public Result scopList(){
        JSONArray jsonArray=new JSONArray();
        if(Idu.getLoginUser().getFamily()==null){
            User user = (User)(queryApi.findByPkey(User.class, Idu.getLoginUser().getUser()));
            jsonArray.add(new JSONObject().fluentPut("label",user.getPkey()).fluentPut("labelName",user.getName()));
        }else {
            List<Map<String,Object>> userList = queryApiNoAuth.listManyFldByParams(Marriage.class, new String[]{"user"}, new String[]{"family"},Idu.getLoginUser().getFamily());
            userList.forEach(m->{
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("label",m.get("user"));
                jsonObject.put("labelName",m.get("user_name"));
                jsonArray.add(jsonObject);
            });
            jsonArray.add(new JSONObject().fluentPut("label",0).fluentPut("labelName","全部"));
        }
        return ResultUtil.writeSuccess(jsonArray);
    }

}

