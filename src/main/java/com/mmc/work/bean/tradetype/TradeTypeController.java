package com.mmc.work.bean.tradetype;

import com.mmc.work.api.TradeTypeApi;
import com.mmc.assist.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/8/28 22:33
 * @Version 1.0
 */
@RestController
public class TradeTypeController implements TradeTypeApi {

    @Autowired
    private TradeTypeService tradeTypeService;

    @Override
    public Result listComb() {
        return tradeTypeService.listCombo();
    }

    @Override
    public Result save(TradeType tradeType) {
        return tradeTypeService.save(tradeType);
    }
}
