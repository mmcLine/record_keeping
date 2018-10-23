package com.mmc.work.bean.profit;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.bean.order.Order;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.UpdateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: mmc
 * @Date: 2018/10/23 23:17
 * @Version 1.0
 */

@Service
public class ProfitService {

    @Autowired
    private QueryResultApi queryResultApi;

    @Autowired
    private InsertApi<Profit> insertApi;

    @Autowired
    private UpdateApi<Profit> updateApi;

    @Autowired
    private QueryApi queryApi;


    public Result profitList(int pageNo) {
        return queryResultApi.listData(Profit.class,pageNo);
    }

    public Result saveProfit(Profit profit) {
        if(profit.getPkey()==null){
            insertApi.insert(profit);
        }else {
            updateApi.update(profit);
        }
        return ResultUtil.writeSuccess();
    }

    public Result listByDate(LocalDate startDate, LocalDate endDate) {
        return queryResultApi.listData(Profit.class,1,"trade_date BETWEEN '" + startDate + "' AND '" + endDate + "'",null);
    }

    public Result names() {
        List<Map<String, Object>> list = queryApi.query(Profit.class, "SELECT name FROM profit WHERE create_time > DATE_ADD(NOW(),INTERVAL -3 MONTH)");
        Map<String,String> names=new HashMap<>();
        return ResultUtil.writeSuccess( list.stream().distinct().collect(Collectors.toList()));
    }
}
