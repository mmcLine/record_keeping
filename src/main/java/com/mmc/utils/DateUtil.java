package com.mmc.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * LocalDate 和Date的互转
 * @Author: mmc
 * @Date: 2018/9/14 0:21
 * @Version 1.0
 */
public class DateUtil {

   public static LocalDate DatatoLocalDate(Date date){
       Instant instant = date.toInstant();
       ZoneId zoneId = ZoneId.systemDefault();
       return instant.atZone(zoneId).toLocalDate();
   }

   public static Date LocalDate2Date(LocalDate date){
       ZonedDateTime zdt = date.atStartOfDay(ZoneId.systemDefault());
       return Date.from(zdt.toInstant());
   }
}
