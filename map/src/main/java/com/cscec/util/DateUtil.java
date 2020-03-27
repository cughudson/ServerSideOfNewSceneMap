package com.cscec.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    static DateTimeFormatter yyyyMMddHHmmss = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    public static String now() {
        return yyyyMMddHHmmss.format(LocalDateTime.now());
    }
    public static String nowStr(){
        return  yyyyMMddHHmmssStr.format(LocalDateTime.now());
    }
    static DateTimeFormatter yyyyMMddHHmmssStr = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

}
