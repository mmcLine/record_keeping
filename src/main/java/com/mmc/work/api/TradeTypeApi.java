package com.mmc.work.api;

import com.mmc.assist.result.Result;
import com.mmc.work.bean.tradetype.TradeType;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: mmc
 * @Date: 2018/8/28 22:14
 * @Version 1.0
 */
public interface TradeTypeApi {

    @RequestMapping("record/tradeType/listCombo")
    public Result listComb();


    @RequestMapping("record/tradeType/save")
    public Result save(TradeType tradeType);
}
