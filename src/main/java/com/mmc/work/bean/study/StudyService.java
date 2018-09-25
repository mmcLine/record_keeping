package com.mmc.work.bean.study;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.UpdateApi;
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
    private QueryResultApi queryResultApi;

    @Autowired
    private InsertApi insertApi;

    @Autowired
    private UpdateApi updateApi;

    public Result list(Integer pageNo){
        return queryResultApi.listData(Study.class,pageNo,null,"start_date",QueryApi.SQL_ORDER.ASC,null);
    }

    public Result save(Study study){
        if(study.getPkey()==null){
            return ResultUtil.writeSuccess(insertApi.insert(study));
        }else {
            return ResultUtil.writeSuccess(updateApi.update(study));
        }
    }
}
