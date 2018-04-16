package com.jfinal.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间格式化工具
 * Created by liuandong on 2017/5/31.
 */
public class TimeUtil {
    //获取时间戳下 00：00：00 的时间戳
    public static long getStart(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp * 1000));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        return calendar.getTimeInMillis()/1000;
    }

    public static long getYesterdayStart(){
        return getStart(getNow() - (3600 * 24));
    }

    //获取今天23：59：59 的时间戳
    public static long getEnd(long timestamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp * 1000));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar.getTimeInMillis()/1000;
    }

    public static Date getDateMonthsAgo(int months){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, 0 - months);
        return calendar.getTime();
    }

    public static Date getDateYearsAgo(int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 0 - years);
        return calendar.getTime();
    }

    public static int getYearsAgo(int years){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 0 - years);
        return new Long(calendar.getTimeInMillis()/1000).intValue();
    }

    public static long getYesterdayEnd(){
        return getEnd(getNow() - (3600 * 24));
    }

    //获取 day 天后的时间戳
    public static long addTime(long timestamp, int day){
        return timestamp + (day * 3600 * 24);
    }

    public static Date getDate(long timestamp){
        return new Date(timestamp * 1000);
    }

    public static long getNow(){
        return System.currentTimeMillis()/1000;
    }

    public static int getIntNow(){
        return new Long(System.currentTimeMillis() / 1000).intValue();
    }

    //一个月以前
    public static long getMouthAgo(){
        return TimeUtil.addTime(TimeUtil.getStart(getNow()), -30);
    }

    public static long getDaysAgo(int days){
        return TimeUtil.addTime(getNow(), -days);
    }

    public static String getDistanceTimeStr(long startDate, long endDate) {
        try {
            long dTime = endDate - startDate;
            long dDay;
            if(dTime>0) {
                dDay = dTime / (3600 * 24);
            } else {
                return "";
            }
            String result = "";
            if( dTime < 60 ){
                result = dTime + "秒";
            }else if( dTime < 3600 ){
                result = (dTime/60) + "分钟";
            }else if( dTime >= 3600 && dDay == 0  ){
                result = (dTime/3600) + "小时";
            }else if( dDay > 0 && dDay<=7 ){
                result = dDay + "天";
            }else if( dDay > 7 &&  dDay <= 30 ){
                result = (dDay/7) + "周";
            }else if( dDay > 30 ){
                result = (dDay/30) + "个月";
            }
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getDateYYYYMMDD() {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(new Date());
    }

    public static String getDateYYYYMMDDDaysAgo(int days) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(new Date(getDaysAgo(days)*1000));
    }

    public static String getYYYYMMByMonthAgo(int months){
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        return df.format(getDateMonthsAgo(months));
    }

}
