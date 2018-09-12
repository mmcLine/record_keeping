package com.mmc.work.bean.menstruation;

import com.mmc.work.api.MenstruationApi;
import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SqlEngine;
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
    private SqlEngine sqlEngine;

    @Autowired
    private MenstruationRepository menstruationRepository;

    @Override
    public Result list(Integer pageNo) {
        return sqlEngine.listBean(Menstruation.class,pageNo);
    }

    @Override
    public Result update(Menstruation menstruation) {
        return ResultUtil.writeSuccess(menstruationRepository.save(menstruation));
    }
}
