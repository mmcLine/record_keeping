package com.mmc.test;

import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.TableApi;
import com.mmc.work.database.api.UpdateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: mmc
 * @Date: 2018/9/18 14:20
 * @Version 1.0
 */
@Controller
public class ActionTest {

    @Autowired
    private QueryApi queryApi;

    @Autowired
    private UpdateApi updateApi;


    @Autowired
    private InsertApi insertApi;

    @Autowired
    private TableApi tableApi;

    @RequestMapping("testAction")
    @ResponseBody
    public String  testAction(){
       return "";
    }
}
