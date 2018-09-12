package com.mmc.work.bean.study;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.SqlEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mmc
 * @Date: 2018/9/1 13:58
 * @Version 1.0
 */
@Service
public class StudyService {

    @Autowired
    private SqlEngine sqlEngine;

    @Autowired
    private StudyRepository studyRepository;

    public Result list(Integer pageNo){
        return sqlEngine.listBean(Study.class,pageNo);
    }

    public Result save(Study study){
        return ResultUtil.writeSuccess(studyRepository.save(study));
    }
}
