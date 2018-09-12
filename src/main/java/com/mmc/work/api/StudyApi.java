package com.mmc.work.api;

import com.mmc.assist.result.Result;
import com.mmc.work.bean.study.Study;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: mmc
 * @Date: 2018/9/1 13:57
 * @Version 1.0
 */
public interface StudyApi {

    @RequestMapping("record/study/list")
    public Result list(Integer pageNo);

    @RequestMapping("record/study/save")
    public Result save(Study study);
}
