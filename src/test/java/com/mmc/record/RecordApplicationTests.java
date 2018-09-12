package com.mmc.record;

import com.mmc.utils.SqlEngine;
import com.mmc.work.bean.marriage.MarriageRepository;
import com.mmc.work.bean.order.OrderRepository;
import com.mmc.work.bean.tradetype.TradeType;
import com.mmc.work.bean.user.UserRepository;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.UpdateApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecordApplicationTests {

    @Autowired
    private OrderRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarriageRepository marriageRepository;

    @Autowired
    private SqlEngine sqlEngine;


    @Autowired
    private QueryApi queryApi;

    @Autowired
    private UpdateApi updateApi;


    @Autowired
    private InsertApi insertApi;

    @Test
    public void contextLoads() {
        TradeType type=new TradeType();
        type.setCreateTime(LocalDate.now());
        type.setTypeName("吃饭");
        insertApi.insert(type);
    }


}
