package com.mmc.work.bean.profit;

import com.mmc.assist.result.Result;
import com.mmc.work.api.ProfitApi;
import com.mmc.work.bean.dateperiod.DatePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/10/23 23:16
 * @Version 1.0
 */
@RestController
public class ProfitController implements ProfitApi {

    @Autowired
    private ProfitService profitService;

    @Override
    public Result profitList(int pageNo) {
        return profitService.profitList(pageNo);
    }

    @Override
    public Result saveProfit(Profit profit) {
        return profitService.saveProfit(profit);
    }

    @Override
    public Result profitList(DatePeriod datePeriod) {
        return profitService.listByDate(datePeriod.getStartDate(),datePeriod.getEndDate());
    }

    @Override
    public Result names() {
        return profitService.names();
    }
}
