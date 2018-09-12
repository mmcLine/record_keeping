package com.mmc.work.bean.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SpringContextUtil;
import com.mmc.utils.SqlEngine;
import com.mmc.work.bean.marriage.Marriage;
import com.mmc.work.bean.marriage.MarriageRepository;
import com.mmc.work.bean.user.User;
import com.mmc.work.bean.user.UserRepository;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.UpdateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    private static final int SIZE = 20;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarriageRepository marriageRepository;

    @Autowired
    private SqlEngine sqlEngine;

    @Autowired
    private InsertApi<Order> insertApi;

    @Autowired
    private UpdateApi<Order> updateApi;

    @Autowired
    private QueryApi<Order> queryApi;

    public Result orderList(int pageNo) {
        return ResultUtil.writeSuccess(queryApi.listData(Order.class,pageNo,"",null));
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
        return sqlEngine.listBean(Order.class, 1, "trade_date BETWEEN '" + startDate + "' AND '" + endDate + "'");
    }

    public JSONObject monthchart() {
        //先判断权限，是否只能看到自己的,没有家庭权限，zhicha只查自己的数据
        if (SpringContextUtil.getLoginUser().getFamily() == null) {
            List<Object[]> data = orderRepository.findMonthDataByUser(SpringContextUtil.getLoginUser().getUser());
            JSONArray monthArray = new JSONArray();
            JSONArray amtArray = new JSONArray();
            JSONArray nameArray = new JSONArray();
            nameArray.add(userRepository.findById(SpringContextUtil.getLoginUser().getUser()).get().getName());
            for (Object[] object : data) {
                monthArray.add(object[0]);
                amtArray.add(object[1]);
            }
            JSONObject dataJson = new JSONObject().fluentPut("months", monthArray).fluentPut("amts", amtArray).fluentPut("names", nameArray);
            return new JSONObject().fluentPut("size", 1).fluentPut("data", dataJson);

        } else {
            //获取10个月
            String[] currentMonth = getCurrentMonth(10);
            List<Marriage> marriages = marriageRepository.findByFamily(SpringContextUtil.getLoginUser().getFamily());
            JSONArray amtJson=new JSONArray();
            JSONArray namesArray=new JSONArray();
            for(Marriage marriage:marriages){
                JSONArray jsonArray=new JSONArray();
                List<Object[]> monthDataByUser = orderRepository.findMonthDataByUser(marriage.getUser());
                for(int i=0;i<currentMonth.length;i++){
                    String month=currentMonth[i];
                    Object[] objects = monthDataByUser.stream().filter(x -> x[0].equals(month)).map(x -> x[1]).toArray();
                    jsonArray.add(objects.length==0?0:objects[0]);
                }
                User user = userRepository.findById(marriage.getUser()).get();
                namesArray.add(user.getName());
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


    //有家庭权限查多条数据

    public static void main(String[] args) {
        int n=10;
        LocalDate now = LocalDate.now();
        String[] months=new String[n];
        months[n-1]=now.toString().substring(0, 7);
        for(int i=n-2;i>=0;i--){
            LocalDate localDate = now.minusMonths(n-i-1);
            String substring = localDate.toString().substring(0, 7);
            months[i]=substring;
        }
        for (int i=0;i<months.length;i++){
            System.out.println(months[i]);
        }
    }


}