package com.mmc.work.database.api;

import com.mmc.assist.result.Result;
import com.mmc.work.base.IdBase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: mmc
 * @Date: 2018/9/22 14:14
 * @Version 1.0
 * 封装了返还的结果
 */
@Component
public interface QueryResultApi<T extends IdBase> {

    Result listData(Class clazz, Integer pageNo);

    Result listData(Class clazz, Integer pageNo, String where, QueryApi.SQL_ORDER order);

    Result listData(Class clazz, Integer pageNo, String where, QueryApi.SQL_ORDER order, Integer pageSize);

    Result listData(Class clazz, Integer pageNo, String where, String orderFld,QueryApi.SQL_ORDER order, Integer pageSize);

    Result listCombobox(Class clazz);

    Result listCombobox(Class clazz, QueryApi.SQL_ORDER order);

    List<Map<String, Object>> extJoinData(List<Map<String, Object>> mapList);
}
