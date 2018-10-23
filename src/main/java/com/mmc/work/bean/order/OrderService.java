package com.mmc.work.bean.order;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mmc.assist.login.Idu;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.DateUtil;
import com.mmc.utils.RecordException;
import com.mmc.work.bean.marriage.Marriage;
import com.mmc.work.bean.tradetype.TradeTypeService;
import com.mmc.work.bean.user.User;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.UpdateApi;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {


    @Autowired
    private InsertApi<Order> insertApi;

    @Autowired
    private UpdateApi<Order> updateApi;

    @Autowired
    private QueryApi<Order> queryApi;

    @Autowired
    private TradeTypeService tradeTypeService;

    @Autowired
    @Qualifier("queryImplNoAuthority")
    private QueryApi<Order> queryApiNoAuth;

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

    public Result names() {
        List<Map<String, Object>> list = queryApi.query(Order.class, "SELECT name FROM trade_order WHERE create_time > DATE_ADD(NOW(),INTERVAL -3 MONTH)");
        Map<String,String> names=new HashMap<>();
        return ResultUtil.writeSuccess( list.stream().distinct().collect(Collectors.toList()));
    }

    public JSONObject monthchart() {
        //先判断权限，是否只能看到自己的,没有家庭权限，只查自己的数据
        if (Idu.getLoginUser().getFamily() == null) {
            String sql="SELECT DATE_FORMAT(t.trade_date,'%Y-%m') month,CAST( SUM(t.amt) AS DECIMAL (15, 2)) amtsum  FROM trade_order t where t.user=? GROUP BY MONTH";
            List<Map<String,Object>> data=queryApiNoAuth.query(Order.class,sql,Idu.getLoginUser().getUser());
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
            List<Map<String, Object>> userList = queryApiNoAuth.listOneFldByParams(Marriage.class, "user",null);
            JSONArray amtJson=new JSONArray();
            JSONArray namesArray=new JSONArray();
            for(Map<String, Object> userMap:userList){
                JSONArray jsonArray=new JSONArray();
                String sql="SELECT DATE_FORMAT(t.trade_date,'%Y-%m') month,CAST( SUM(t.amt) AS DECIMAL (15, 2)) amtsum  FROM trade_order t where t.user=? GROUP BY MONTH";
                List<Map<String, Object>> monthAmtList = queryApiNoAuth.query(Order.class,sql,userMap.get("user"));
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

    public void importExcel(InputStream input){
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(input);
            Sheet sheet = workbook.getSheetAt(0);
            List<Map<String, Object>> typeList = tradeTypeService.listNameAndPkey();
            // 工作表内的行数大于一行的读取数据
            System.out.println(sheet.getLastRowNum());
            for(int i=1;i<=sheet.getLastRowNum();i++){
                Order order=new Order();
                Row row = sheet.getRow(i);
                //名称
                if(row.getCell(0).getCellTypeEnum()==CellType._NONE){
                   throw new RecordException("OrderService","名称不能为空");
                }else {
                    row.getCell(0).setCellType(CellType.STRING);
                    order.setName(row.getCell(0).getStringCellValue());
                }
                //类别
                if(row.getCell(1).getCellTypeEnum()!=CellType._NONE){
                    row.getCell(1).setCellType(CellType.STRING);
                    String typeName = row.getCell(1).getStringCellValue();
                    typeList.forEach(x->{
                        if(x.get("type_name").equals(typeName)){
                            order.setTradeTypeId((int)(x.get("pkey")));
                        }
                    });
                }
                //日期
                if(row.getCell(2).getCellTypeEnum()==CellType._NONE){
                    throw new RecordException("OrderService","日期不能为空");
                }else {
                    Date value = row.getCell(2).getDateCellValue();
                    order.setTradeDate(DateUtil.DatatoLocalDate(value));
                }
                //金额
                if(row.getCell(3).getCellTypeEnum()==CellType._NONE){
                    throw new RecordException("OrderService","金额不能为空");
                }else {
                    order.setAmt( row.getCell(3).getNumericCellValue());
                }
                insertApi.insert(order);
            }
        }catch (IOException e){
            e.printStackTrace();
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