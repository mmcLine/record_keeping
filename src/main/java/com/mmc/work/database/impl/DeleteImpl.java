package com.mmc.work.database.impl;

import com.mmc.work.database.api.DeleteApi;
import com.mmc.work.database.api.TableApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author: mmc
 * @Date: 2018/9/20 22:25
 * @Version 1.0
 */
@Component
public class DeleteImpl implements DeleteApi {

    @Autowired
    private TableApi tableApi;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int[] delete(String className,String ids) {
            String[] pkeyArray = ids.split(",");
            String table = tableApi.getTable(className);
            int[] dels=jdbcTemplate.batchUpdate("delete from " + table + " where pkey=?",
                    new BatchPreparedStatementSetter() {
                        public void setValues(PreparedStatement ps, int i)
                                throws SQLException {
                            ps.setInt(1, Integer.valueOf(pkeyArray[i]));
                        }
                        public int getBatchSize() {
                            return pkeyArray.length;
                        }
                    });
            return dels;
        }
    }

