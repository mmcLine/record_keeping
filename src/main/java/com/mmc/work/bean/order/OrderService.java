package com.mmc.work.bean.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.login.Idu;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.bean.marriage.Marriage;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.UpdateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {


    @Autowired
    private InsertApi<Order> insertApi;

    @Autowired
    private UpdateApi<Order> updateApi;

    @Autowired
    private QueryApi<Order> queryApi;


    @Autowired
    private QueryResultApi queryResultApi;

    public Result orderList(int pageNo) {
        return queryResultApi.listData(Order.class,pageNo);
    }

    public Result updOrder(Order order) {
       if(order.getPkey()==null){
           insertApi.insert(order);
       }else {
           updateApi.update(order);
       }
        return ResultUtil.writeSuccess();
    }

    public Result listByDate(LocalDate startDate, LocalDate endDate) {
       return queryResultApi.listData(Order.class,1,"trade_date BETWEEN '" + startDate + "' AND '" + endDate + "'",null);
    }

    public JSONObject monthchart() {
        //先判断权限，是否只能看到自己的,没有家庭权限，只查自己的数据
        if (Idu.getLoginUser().getFamily() == null) {
            String sql="SELECT DATE_FORMAT(t.trade_date,'%Y-%m') month,CAST( SUM(t.amt) AS DECIMAL (15, 2)) amtsum  FROM trade_order t where t.user=? GROUP BY MONTH";
            List<Map<String,Object>> data=queryApi.query(sql,Idu.getLoginUser().getUser());
            JSONArray monthArray = new JSONArray();
            JSONArray amtArray = new JSONArray();
            JSONArray nameArray = new JSONArray();
            nameArray.add(queryApi.findFldByPkey(User.class,"name",Idu.getLoginUser().getUser()));
            for (Map<String,Object> map : data) {
                monthArray.add(map.get("month"));
                amtArray.add(map.get("amtsum"));
            }
            JSONObject dataJson = new JSONObject().fluentPut("months", monthArray).fluentPut("amts", amtArray).fluentPut("names", nameArray);
            return new JSONObject().fluentPut("size", 1).fluentPut("data", dataJson);

        } else {
            //获取10个月
            String[] currentMonth = getCurrentMonth(10);
            List<Map<String, Object>> userList = queryApi.listOneFldByParams(Marriage.class, "user",null);
            JSONArray amtJson=new JSONArray();
            JSONArray namesArray=new JSONArray();
            for(Map<String, Object> userMap:userList){
                JSONArray jsonArray=new JSONArray();
                String sql="SELECT DATE_FORMAT(t.trade_date,'%Y-%m') month,SUM(t.amt) amtsum  FROM trade_order t where t.user=? GROUP BY MONTH";
                List<Map<String, Object>> monthAmtList = queryApi.query(sql,userMap.get("user"));
                for(int i=0;i<currentMonth.length;i++){
                    String month=currentMonth[i];
                    boolean flag=true;
                    for (Map<String,Object> map:monthAmtList){
                        if(map.get("month").equals(month)){
                            jsonArray.add(map.get("amtsum"));
                            flag=false;
                        }
                    }
                    if(flag){jsonArray.add(0);}
                }
                Object userName = queryApi.findFldByPkey(User.class, "name", Integer.valueOf(userMap.get("user").toString()));
                namesArray.add(userName);
                amtJson.add(jsonArray);
            }
            JSONObject dataJson = new JSONObject().fluentPut("months", currentMonth).fluentPut("amts", amtJson).fluentPut("names", namesArray);
            return new JSONObject().fluentPut("size", namesArray.size()).fluentPut("data", dataJson);

        }
    }


    //获取最近的n个月的字符串表示
    private String[] getCurrentMonth(int n) {
        LocalDate now = LocalDate.now();
        String[] months=new String[n];
        months[n-1]=now.toString().substring(0, 7);
        for(int i=n-2;i>=0;i--){
            LocalDate localDate = now.minusMonths(n-i-1);
            String substring = localDate.toString().substring(0, 7);
            months[i]=substring;
        }
        return months;
    }





}