package com.mmc.work.bean.tradetype;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SqlEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @Author: mmc
 * @Date: 2018/8/28 22:46
 * @Version 1.0
 */
@Service
public class TradeTypeService {

    @Autowired
    private SqlEngine sqlEngine;

    @Autowired
    private TradeTypeRepository tradeTypeRepository;

    public Result listCombo(){
        return sqlEngine.listComboBean(TradeType.class);
    }

    public Result save(TradeType tradeType){
        tradeType.setCreateTime(LocalDate.now());
        tradeTypeRepository.save(tradeType);
        return ResultUtil.writeSuccess();
    }
}
