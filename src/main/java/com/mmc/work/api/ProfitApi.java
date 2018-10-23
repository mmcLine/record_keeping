package com.mmc.work.api;

import com.mmc.assist.result.Result;
import com.mmc.work.bean.dateperiod.DatePeriod;
import com.mmc.work.bean.profit.Profit;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: mmc
 * @Date: 2018/10/23 23:04
 * @Version 1.0
 */
public interface ProfitApi {

    @RequestMapping("record/profit/list")
    Result profitList(int pageNo);

    @RequestMapping("record/profit/save")
    Result saveProfit(Profit profit);

    //日期搜索
    @RequestMapping("record/profit/datelist")
    Result profitList(DatePeriod datePeriod);

    //输入建议搜索，返回近期的输入项
    @RequestMapping("record/profit/names")
    Result names();

}
