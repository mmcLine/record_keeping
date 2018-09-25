package com.mmc.record;

import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.TableApi;
import com.mmc.work.database.api.UpdateApi;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.InvocationTargetException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecordApplicationTests {




    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Autowired
    private QueryApi queryApi;

    @Autowired
    private UpdateApi updateApi;


    @Autowired
    private InsertApi insertApi;

    @Autowired
    private TableApi tableApi;

    @Test
    public void contextLoads() throws InvocationTargetException, IllegalAccessException {


    }

    public static void main(String[] args) {

    }


}
