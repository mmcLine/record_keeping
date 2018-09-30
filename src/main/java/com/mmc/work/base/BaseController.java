package com.mmc.work.base;

import com.mmc.assist.result.Result;
import com.mmc.assist.result.ResultUtil;
import com.mmc.utils.RecordException;
import com.mmc.work.database.api.DeleteApi;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * 处理通用请求
 * @Author: mmc
 * @Date: 2018/8/31 21:18
 * @Version 1.0
 */
@RestController
public class BaseController {

    private static Logger logger=LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private DeleteApi deleteApi;

    @RequestMapping("record/del")
    public Result delete(@RequestParam("className") @NotNull String className, String pkeys){
       return ResultUtil.writeSuccess(deleteApi.delete(className,pkeys));
    }

    @RequestMapping("record/trade/downexcel")
    public void downloadExcel(String excelName, HttpSession session, HttpServletResponse response)
            throws IOException {
        long start=System.currentTimeMillis();
        String url =  "/usr/local/myproject/templateexcel/" + excelName + ".xlsx";
//        String url =  "d:/templateexcel/" + excelName + ".xlsx";
        try ( FileInputStream fileInputStream = new FileInputStream(url)){
            byte[] data = IOUtils.toByteArray(fileInputStream);
            response.reset();
            response.setHeader("content-disposition", "attachment");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("image/png; charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin","http://www.mmcao.top:8091");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
            response.setCharacterEncoding("utf-8");
            response.getOutputStream().write(data);
            System.out.println(System.currentTimeMillis()-start);
        } catch (Exception e) {
            throw new RecordException("BaseController","下载导入模板出错");
        }
    }
}
