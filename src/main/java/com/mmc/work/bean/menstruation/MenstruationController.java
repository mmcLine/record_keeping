package com.mmc.work.bean.menstruation;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.api.MenstruationApi;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/9/1 22:14
 * @Version 1.0
 */
@RestController
public class MenstruationController implements MenstruationApi {

    @Autowired
    private QueryApi queryApi;

    @Autowired
    private InsertApi insertApi;

    @Override
    public Result list(Integer pageNo) {
        return ResultUtil.writeSuccess(queryApi.listData(Menstruation.class,pageNo,"",null));

    }

    @Override
    public Result update(Menstruation menstruation) {
        return ResultUtil.writeSuccess(insertApi.insert(menstruation));
    }
}
