package com.mmc.work.bean.study;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
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
    private QueryApi queryApi;

    @Autowired
    private InsertApi insertApi;

    public Result list(Integer pageNo){
        return ResultUtil.writeSuccess(queryApi.listData(Study.class,pageNo,"",null));
    }

    public Result save(Study study){
        return ResultUtil.writeSuccess(insertApi.insert(study));
    }
}
