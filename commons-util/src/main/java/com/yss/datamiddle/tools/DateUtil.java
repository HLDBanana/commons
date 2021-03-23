package com.yss.datamiddle.tools;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * @description: 日期工具类
 * @author: fangzhao
 * @create: 2020/3/24 13:09
 * @update: 2020/3/24 13:09
 */
@Slf4j
public final class DateUtil {

    private static final String STR_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final String STR_YYYY_MM_DD = "yyyy-MM-dd";
    private static final String STR_YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    private static final String STR_ERROR = "error";

    /**
     * 将日期字符串转换成长时间格式yyyy-MM-dd HH:mm:ss
     *
     */
    public static LocalDateTime longStrToDate(String strDate) {
        if (StringUtils.isEmpty(strDate)) {
            return null;
        } else {
            TemporalAccessor parse = DateTimeFormatter.ofPattern(STR_YYYY_MM_DD_HH_MM_SS).parse(strDate);
            return LocalDateTime.from(parse);
        }
    }

    /**
     * 获取系统当前时间戳
     *
     * @return
     */
    public static String getCurUnixTimeStamp() {
        long unixTimeStamp = System.currentTimeMillis() / 1000L;
        return String.valueOf(unixTimeStamp);
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    public static Integer getCurUnixTimeStampInt() {
        return Integer.valueOf(getCurUnixTimeStamp());
    }

    /**
     * 把unix时间戳转换为时间字符串 [格式：“yyyy-MM-dd HH:mm:ss”]
     *
     * @param timeStamp 时间戳
     * @return 格式化好的时间字符串
     */
    public static String toDate(long timeStamp) {
        return toDate(timeStamp, STR_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 把unix时间戳转换为时间字符串 [格式：“yyyy-MM-dd”]
     *
     * @param timeStamp 时间戳
     * @return 格式化好的时间字符串
     */
    public static String toDateShort(long timeStamp) {
        return toDate(timeStamp, STR_YYYY_MM_DD);
    }

    /**
     * 把unix时间戳转换为时间字符串
     *
     * @param timeStamp  时间戳
     * @param dateFormat 时间格式 yyyy-MM-dd HH:mm:ss a
     * @return 格式化好的时间字符串
     */
    public static String toDate(long timeStamp, String dateFormat) {
        if (0 == timeStamp) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), ZoneId.systemDefault());
        return dateTimeFormatter.format(dateTime);
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthday 生日字符串【yyyy-MM-dd】
     * @return
     */
    public static int getAgeByBirth(String birthday) {
        int age = 0;
        if (!ValidateUtil.isDate(birthday)) {
            return age;
        }
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STR_YYYY_MM_DD);
            LocalDate parse = LocalDate.parse(birthday, dateTimeFormatter);
            if(LocalDate.now().isAfter(parse)){
                int nowYear = LocalDateTime.now().getYear();
                int year = parse.getYear();
                age = nowYear - year;
            }
            return age;
        } catch (Exception e) { //兼容性更强,异常后返回数据
            return age;
        }
    }

    /**
     * 获取当前时间的 年月日时分秒毫秒
     */
    public static String getCurrentTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STR_YYYYMMDDHHMMSSSSS);
        return dateTimeFormatter.format(LocalDateTime.now());
    }

    /**
     * 转换成format
     */
    public static String dateToStr(LocalDateTime dateTime, String format) {
        if (null == dateTime) {
            return "";
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            return dateTimeFormatter.format(dateTime);
        }
    }

    /**
     * 将长时间格式yyyy-MM-dd HH:mm:ss字符串转换为日期
     */
    public static String dateToStr(LocalDateTime dateTime) {
        return dateToStr(dateTime, STR_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 将指定的日期加上指定的天数
     */
    public static LocalDate addDays(LocalDate date, int days) {
        if (0 == days) {
            return date;
        } else {
            return date.plusDays(days);
        }
    }

    /**
     * 将指定的日期加上指定的小时
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, int hours) {
        if (0 == hours) {
            return dateTime;
        } else {
            return dateTime.plusHours(hours);
        }
    }

    /**
     * 把指定日期的时分秒设为0
     *
     * @param c 指定的日期
     */
    private static LocalDateTime setNoTime(LocalDateTime dateTime) {
        if(null != dateTime){
            return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
        }
        return null;
    }

    /**
     * 把时间字符串转换为时间戳
     *
     * @param time 时间格式 yyyy-MM-dd HH:mm:ss
     * @return 10位Integer类型时间戳
     */
    public static Integer date2TimeStamp(String time) {
        Integer timeStemp = 0;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STR_YYYY_MM_DD_HH_MM_SS);

        try {
            LocalDateTime dateTime = LocalDateTime.parse(time, dateTimeFormatter);
            timeStemp = dateTime.getSecond();
        } catch (Exception e) {
            log.error(STR_ERROR, e);
        }
        return timeStemp;
    }


    /**
     * 把时间字符串转换为时间戳
     *
     * @param time 时间格式 yyyy-MM-dd
     * @return 10位Integer类型时间戳
     */
    public static LocalDate date2TimeStamp2(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(STR_YYYY_MM_DD);
        return LocalDate.parse(time, dateTimeFormatter);
    }

    /**
     * 创建Timestamp
     *
     * @return
     */
    public static Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前时间 格式：年月日
     *
     * @return
     */
    public static String getActivityTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return dateTimeFormatter.format(LocalDate.now());
    }


    /**
     * 返回以当前时期的开始时间
     *
     * @param date
     * @return
     */
    public static LocalDateTime getStartDateTime(LocalDateTime dateTime) {
        return timeMinOrMax(dateTime, 0);
    }

    /**
     * 返回以当前时期的结束时间
     *
     * @param date
     * @return
     */
    public static LocalDateTime getEndDateTime(LocalDateTime dateTime) {
        return timeMinOrMax(dateTime, 1);
    }

    /**
     * 凌晨
     *
     * @param dateTime
     * @return
     * @flag 0 返回yyyy-MM-dd 00:00:00日期<br>
     * 1 返回yyyy-MM-dd 23:59:59日期
     */
    private static LocalDateTime timeMinOrMax(LocalDateTime dateTime, int flag) {

        if(null != dateTime){
            if (0 == flag){
                // 当天零点
                return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MIN);
            }else{
                // 当天的最后时间
                return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
            }
        }
        return null;
    }

    /**
     * 获取剩余秒数
     *
     * @param endTime
     * @return
     */
    public static long getLastTimeSecond(String endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(STR_YYYY_MM_DD_HH_MM_SS);
        LocalDateTime dateTime = LocalDateTime.parse(endTime, df);
        Duration duration = Duration.between(dateTime, LocalDateTime.now());
        return 0 < duration.getSeconds() ? duration.getSeconds() : 0;
    }

    /**
     * 比较两个时间大小
     *
     * @param nowTime
     * @param updateTime
     * @return
     */
    public static int compaerTime(LocalDateTime nowTime, LocalDateTime updateTime) {

        //容错
        if (null == nowTime || null == updateTime) {
            return 0;
        }

        //做差，取出相差毫秒数
        long i = nowTime.toInstant(ZoneOffset.of("+8")).toEpochMilli() - updateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();

        //定义一个月为30天，取30天的毫秒数
        //一天86400000毫秒
        long oneMonthTime = (new BigDecimal(86400000).multiply(new BigDecimal(30))).longValue();
        if (i > oneMonthTime) {
            //证明超过一个月
            return 1;
        }
        return 0;

    }

    public static LocalDate strToDate(String strDate) {
        TemporalAccessor parse1 = DateTimeFormatter.ofPattern(STR_YYYY_MM_DD).parse(strDate);
        return LocalDate.from(parse1);
    }

    /**
     * 描述：获取指定时间的时间戳
     *
     * @param dateTime
     * @return long
     * @author fangzhao at 2020/11/20 10:53
     */
    public static long getEpochMilli(LocalDateTime dateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = dateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static void main(String[] args) {

//        System.out.println(longStrToDate("2020-01-03 12:12:12"));

//        LocalDateTime dateTime = longStrToDate("2020-01-03 12:12:12");
//        System.out.println(dateToString(dateTime));

//        System.out.println(getCurUnixTimeStamp());

//        System.out.println(getCurUnixTimeStampInt());

//        System.out.println(toDate(System.currentTimeMillis()));

//        System.out.println(toDateShort(System.currentTimeMillis()));

//        System.out.println(getAgeByBirth("2990-01-01"));

//        System.out.println(getCurrentTime());

//        System.out.println(addDays(LocalDate.now(), 10));

//        System.out.println(addHours(LocalDateTime.now(), 2));

//        LocalDateTime dateTime = longStrToDate("2020-01-03 12:12:12");
//        System.out.println(setNoTime(dateTime));

//        System.out.println(date2TimeStamp2("2020-01-03"));

//        System.out.println(getTime());

//        System.out.println(getActivityTime());

//        LocalDateTime dateTime = longStrToDate("2020-01-03 12:12:12");
//        System.out.println(getStartDateTime(dateTime));
//        System.out.println(getEndDateTime(dateTime));

//        System.out.println(getLastTimeSecond("2020-04-08 14:07:12"));

//        System.out.println(strToDate("2020-04-08"));

    }
}
