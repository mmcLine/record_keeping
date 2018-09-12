package com.mmc.work.bean.study;

import com.mmc.work.api.StudyApi;
import com.mmc.assist.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mmc
 * @Date: 2018/9/1 13:58
 * @Version 1.0
 */
@RestController
public class StudyController implements StudyApi {

    @Autowired
    private StudyService studyService;

    @Override
    public Result list(Integer pageNo) {
        return studyService.list(pageNo);
    }

    @Override
    public Result save(Study study) {
        return studyService.save(study);
    }
}
