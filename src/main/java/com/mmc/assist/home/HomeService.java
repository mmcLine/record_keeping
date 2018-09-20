package com.mmc.assist.home;

import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.bean.loginlog.LoginLog;
import com.mmc.work.bean.order.Order;
import com.mmc.work.database.api.QueryApi;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Result getTotalAmtAndNumber(){
        String sql="SELECT SUM(amt) as monthamt FROM trade_order WHERE DATE_FORMAT(trade_date,'%Y%m')=DATE_FORMAT(CURDATE(),'%Y%m') AND "+queryApi.getAuthorithWhere(Order.class);
        String sql2="SELECT SUM(amt) AS totalamt,COUNT(*) AS totalcount FROM trade_order WHERE 1=1 AND "+queryApi.getAuthorithWhere(Order.class);
        List<Map<String,Object>> list = queryApi.query(sql);
        List<Map<String,Object>> list2 = queryApi.query(sql2);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("monthamt",list.get(0).get("monthamt"));
        jsonObject.put("totalamt",list2.get(0).get("totalamt"));
        jsonObject.put("totalcount",list2.get(0).get("totalcount"));
        return ResultUtil.writeSuccess(jsonObject);
    }

    public Result loginInfo(){
        String sql="SELECT ip,create_time FROM login_log where "+queryApi.getAuthorithWhere(LoginLog.class)+" ORDER BY create_time DESC LIMIT 1";
        return ResultUtil.writeSuccess(queryApi.query(sql));
    }
}
