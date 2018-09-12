package com.mmc.work.api;

import com.mmc.assist.result.Result;
import com.mmc.work.bean.menstruation.Menstruation;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: mmc
 * @Date: 2018/9/1 22:11
 * @Version 1.0
 */
public interface MenstruationApi {

    @RequestMapping("record/menstruation/list")
    public Result list(Integer pageNo);

    @RequestMapping("record/menstruation/save")
    public Result update(Menstruation menstruation);
}
