package com.mmc.work.bean.menstruation;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.RecordException;
import com.mmc.work.database.api.InsertApi;
import com.mmc.work.database.api.QueryApi;
import com.mmc.work.database.api.QueryResultApi;
import com.mmc.work.database.api.UpdateApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: mmc
 * @Date: 2018/9/21 15:55
 * @Version 1.0
 */
@Service
public class MenstruationService {

    @Autowired
    private QueryApi<Menstruation> queryApi;

    @Autowired
    private QueryResultApi queryResultApi;

    @Autowired
    private InsertApi insertApi;

    @Autowired
    private UpdateApi updateApi;

    private static final Integer CYCLE=30;

    public Result lineChart(){
        return queryResultApi.listData(Menstruation.class,1,null,QueryApi.SQL_ORDER.ASC,10);
    }

    public Result save(Menstruation menstruation){
       //新增
        if(menstruation.getPkey()==null){
            List<Menstruation> menslist = queryApi.listByWhere(Menstruation.class, "ORDER BY mens_date DESC LIMIT 1 ");
            if(CollectionUtils.isEmpty(menslist)){
                menstruation.setCycle(CYCLE);
            }else {
                long cycleLong=menstruation.getMensDate().toEpochDay()-menslist.get(0).getMensDate().toEpochDay();
                if(cycleLong<=0){
                    throw new RecordException("MenstruationService","请按日期顺序依次添加记录");
                }
                menstruation.setCycle((int)cycleLong);
            }
        insertApi.insert(menstruation);
        }
        //修改
        else {
            Menstruation dbMenstruation = queryApi.findByPkey(Menstruation.class, menstruation.getPkey());
            //改了经期日期要修改周期
            if(!dbMenstruation.getMensDate().isEqual(menstruation.getMensDate())){
                int bewteen=(int)(menstruation.getMensDate().toEpochDay()-dbMenstruation.getMensDate().toEpochDay());
                menstruation.setCycle(dbMenstruation.getCycle()+bewteen);
            }
            updateApi.update(menstruation);
        }
        return ResultUtil.writeSuccess();
    }


}
