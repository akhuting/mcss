package com.sobey.common.util;

import sun.misc.Cleaner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yanggang.
 * Date: 10-4-22
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class DateUtil {
    public final static int YEAR = 1;


    public final static int MONTH = 2;


    public final static int WEEK_OF_YEAR = 3;

    public final static int WEEK_OF_MONTH = 4;


    public final static int DATE = 5;


    public final static int DAY_OF_MONTH = 5;

    public final static int DAY_OF_YEAR = 6;

    public final static int DAY_OF_WEEK = 7;

    public final static int DAY_OF_WEEK_IN_MONTH = 8;


    public final static int HOUR_OF_DAY = 11;

    public final static int MINUTE = 12;

    public final static int SECOND = 13;

    public final static String YY_MM_DD_TIME = "yyyy/MM/dd HH:mm:ss";
    public final static String _YY_MM_DD_TIME = "yyyy-MM-dd HH:mm:ss";
    public final static String YY_MM_DD = "yyyy/MM/dd";
    public final static String _YY_MM_DD = "yyyy-MM-dd";
    public final static String YY_MM = "yyyyMM";
    public final static String YY_MM_D = "yyyyMMdd";

    /**
     * 获得默认格式的当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        String str = "";
        str = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(new Date());
        return str;
    }

    /**
     * 根据参数格式获得当前时间
     *
     * @param formatStr
     * @return
     */
    public static String getCurrentTime(String formatStr) {
        String str = "";
        str = new SimpleDateFormat(formatStr).format(new Date());
        return str;
    }

    public static boolean checkDay(String beginTime, String endTime) {
        String begin = beginTime.split("-")[2];
        String end = endTime.split("-")[2];
        if (begin.length() == 1) {
            begin = "0" + begin;
        }
        if (end.length() == 1) {
            end = "0" + end;
        }
        if (begin.length() != 2) {
            begin = begin.substring(0, 2);
        }
        if (end.length() != 2) {
            end = end.substring(0, 2);
        }
        if (!begin.equals(end))
            return false;
        return true;
    }

    /**
     * 获得时间的特定的值
     *
     * @return
     */
    public static int getSpecificTime(int type) {
        return Calendar.getInstance().get(type);
    }

    @SuppressWarnings({"deprecation"})
    public static int getSpecificTime(String date, int type) {
        Calendar calendar = Calendar.getInstance();
        if (date.split("-").length != 0) {
            String temp[] = date.split("-");
            date = temp[0] + "/" + temp[1] + "/" + temp[2];
        }
        calendar.setTime(new Date(date));
        return calendar.get(type);
    }

    public static Date getDate(String date) {
        try {
            return DateFormat.getDateInstance().parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String getSpecificTime(String date, String formatStr) {
        String str = "";
        str = new SimpleDateFormat(formatStr).format(getDate(date));
        return str;
    }

    public static String getSpecificTime(Date date, String formatStr) {
        String str = "";
        str = new SimpleDateFormat(formatStr).format(date);
        return str;
    }

    /**
     * 得到本周第一天
     * @return  yyyy-mm-dd
     */
    public static String getFirstDayOfWeek() {
        StringBuffer result = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -(calendar.get(Calendar.DAY_OF_WEEK) - 2));
        result.append(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        result.append("-").append(month).append("-");
        String day = String.valueOf(calendar.get(Calendar.DATE));
        if (day.length() == 1) {
            day = "0" + day;
        }
        result.append(day);
        return result.toString();
    }

    /**
     * 得到本周最后一天
     * @return yyyy-mm-dd
     */
    public static String getLastDayOfWeek() {
        StringBuffer result = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 7-(calendar.get(Calendar.DAY_OF_WEEK)) + 1);
        result.append(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        if (month.length() == 1) {
            month = "0" + month;
        }
        result.append("-").append(month).append("-");
        String day = String.valueOf(calendar.get(Calendar.DATE));
        if (day.length() == 1) {
            day = "0" + day;
        }
        result.append(day);
        return result.toString();
    }

    /**
     * 得到本月最后一天
     * @return  dd
     */
    public static int getLastDayOfMonth(){
        Calendar cal = Calendar.getInstance();
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
