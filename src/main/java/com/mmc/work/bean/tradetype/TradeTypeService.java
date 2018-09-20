package com.mmc.work.bean.tradetype;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.UpdateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mmc
 * @Date: 2018/8/28 22:46
 * @Version 1.0
 */
@Service
public class TradeTypeService {

    @Autowired
    private QueryApi queryApi;

    @Autowired
    private InsertApi insertApi;

    @Autowired
    private UpdateApi updateApi;

    public Result listCombo(){
        return ResultUtil.writeSuccess(queryApi.listCombobox(TradeType.class));
    }

    public Result save(TradeType tradeType){
        if(tradeType.getPkey()==null){
            insertApi.insert(tradeType);
        }else {
            updateApi.update(tradeType);
        }
        return ResultUtil.writeSuccess();
    }
}
