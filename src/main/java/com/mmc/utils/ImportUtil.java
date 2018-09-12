package com.mmc.utils;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * 从excel中导入记账数据
 */
public class ImportUtil {

    @Autowired
//    private OrderRepsitory orderRepsitory;

    public static void importExcel(String path){
        try {
            HSSFWorkbook hssfWorkbook=new HSSFWorkbook(new FileInputStream(path));
            HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            for(int i=1;i<lastRowNum;i++){
                HSSFRow row = sheet.getRow(i);
                for (int j=0;j<row.getLastCellNum();j++){
                    System.out.println(row.getCell(j));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        importExcel("C:\\Users\\Administrator\\Desktop\\记账.xls");
    }
}
