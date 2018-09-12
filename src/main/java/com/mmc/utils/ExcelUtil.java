//package com.mmc.book_keeping.utils;
//
//import com.mmc.book_keeping.work.tradeType.TradeType;
//import com.mmc.book_keeping.work.user.User;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.BeanWrapper;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.beans.PropertyDescriptor;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class ExcelUtil {
//
//    public static Workbook exportCustom(List<BeanWrapper> wrapperList){
//        XSSFWorkbook wb = new XSSFWorkbook();
//        XSSFSheet sheet = wb.createSheet();
//        XSSFRow row = sheet.createRow(0);
//
//        if(wrapperList!=null&&wrapperList.size()>0){
//            BeanWrapper beanWrapper = wrapperList.get(0);
//            PropertyDescriptor[] propertyDescriptors = beanWrapper.getPropertyDescriptors();
//            Object[] columnName =  Arrays.stream(propertyDescriptors).map(property -> property.getName()).filter(name -> !name.equals("class")).toArray();
//            CellStyle cellStyle=getTitleStyle(wb);
//            //行标题
//            for(int i=0;i<columnName.length;i++){
//                XSSFCell cell = row.createCell(i);
//                cell.setCellStyle(cellStyle);
//                sheet.setColumnWidth(i,columnName[i].toString().length()*256*3);
//                cell.setCellValue(columnName[i].toString());
//            }
//
//            //填充数据
//            for(int i=0;i<wrapperList.size();i++){
//                XSSFRow row1 = sheet.createRow(i + 1);
//                for (int j=0;j<columnName.length;j++){
//                    XSSFCell cell = row1.createCell(j);
//                    cell.setCellStyle(getValueStyle(wb));
//                    specailValue(cell,wrapperList.get(i).getPropertyValue(columnName[j].toString()));
//                }
//
//
//            }
//        }
//
//        return wb;
//    }
//
//    private static CellStyle getTitleStyle(XSSFWorkbook wb){
//        CellStyle cellStyle=wb.createCellStyle();
//        Font font = wb.createFont();
//        font.setFontHeightInPoints((short)15);
//        font.setFontName("微软雅黑");
//        font.setBold(true);
//        cellStyle.setFont(font);
//        cellStyle.setBorderTop(BorderStyle.THIN);
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//        cellStyle.setBorderRight(BorderStyle.THIN);
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        return cellStyle;
//    }
//
//    private static CellStyle getValueStyle(XSSFWorkbook wb){
//        CellStyle cellStyle=wb.createCellStyle();
//        Font font = wb.createFont();
//        font.setFontHeightInPoints((short)13);
//        font.setFontName("楷体");
//        cellStyle.setFont(font);
//        cellStyle.setBorderTop(BorderStyle.THIN);
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//        cellStyle.setBorderRight(BorderStyle.THIN);
//        cellStyle.setBorderBottom(BorderStyle.THIN);
//        return cellStyle;
//    }
//
//    private static void specailValue(Cell cell,Object object){
//        if(StringUtils.isEmpty(object)){
//            cell.setCellValue("");
//        } else if(object instanceof  User){
//            cell.setCellValue(((User) object).getName());
//        }else  if(object instanceof TradeType){
//            cell.setCellValue(((TradeType)object).getTypeName());
//        }
//        else{
//            cell.setCellValue(object.toString());
//        }
//
//    }
//}
