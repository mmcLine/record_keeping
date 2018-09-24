package com.mmc.work.bean.menstruation;

import com.mmc.assist.result.Result;
import com.mmc.work.api.MenstruationApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/9/1 22:14
 * @Version 1.0
 */
@RestController
public class MenstruationController implements MenstruationApi {



    @Autowired
    private QueryResultApi queryResultApi;

    @Autowired
    private MenstruationService menstruationService;

    @Override
    public Result list(Integer pageNo) {
        return queryResultApi.listData(Menstruation.class,pageNo);

    }

    @Override
    public Result update(Menstruation menstruation) {
        return menstruationService.save(menstruation);
    }

    @RequestMapping("record/menstruation/lineChart")
    public Result lineChart(){
        return menstruationService.lineChart();
    }
}
