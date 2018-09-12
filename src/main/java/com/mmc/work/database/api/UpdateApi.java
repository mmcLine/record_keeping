package com.mmc.work.database.api;

/**
 * @Author: mmc
 * @Date: 2018/9/10 22:44
 * @Version 1.0
 */

import com.mmc.work.base.IdBase;
import org.springframework.stereotype.Component;

/**
 * 修改数据库操作的Api
 */
@Component
public interface UpdateApi<T extends IdBase> extends DataBaseApi{

    public boolean update(T object);

}
