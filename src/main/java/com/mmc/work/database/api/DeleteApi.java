package com.mmc.work.database.api;

import com.mmc.work.base.IdBase;
import org.springframework.stereotype.Component;

/**
 * @Author: mmc
 * @Date: 2018/9/20 22:23
 * @Version 1.0
 */
@Component
public interface DeleteApi<T extends IdBase> extends DataBaseApi {

    int[] delete(String className,String ids);
}
