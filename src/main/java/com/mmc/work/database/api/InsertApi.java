package com.mmc.work.database.api;

import com.mmc.work.base.IdBase;
import org.springframework.stereotype.Component;

/**
 * @Author: mmc
 * @Date: 2018/9/11 22:31
 * @Version 1.0
 */
@Component
public interface InsertApi<T extends IdBase> extends DataBaseApi {

     Integer insert(T t);
}
