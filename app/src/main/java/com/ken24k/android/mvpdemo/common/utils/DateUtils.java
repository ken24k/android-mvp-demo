package com.ken24k.android.mvpdemo.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 日期工具
 * Created by wangming on 2020-05-28
 */

public class DateUtils {

    /**
     * 获取当前系统时间
     */
    public static String getCurrentDate(String format) {
        return dateTransfer(new Date(), format);
    }

    /**
     * 获取当前系统时间
     */
    public static String getCurrentDate() {
        return getCurrentDate(DateFormat.date_standard);
    }

    /**
     * 日期格式
     */
    public class DateFormat {
        public static final String yyyyMMdd = "yyyyMMdd";
        public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
        public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
        public static final String date_standard = "yyyy-MM-dd HH:mm:ss";
        public static final String date_default = "yyyy-MM-dd";
    }

    /**
     * 日期转换 默认yyyy-MM-dd
     */
    public static String dateTransfer(Date date, String format) {
        SimpleDateFormat df;
        switch (format) {
            case DateFormat.yyyyMMdd:
                df = new SimpleDateFormat(DateFormat.yyyyMMdd, Locale.CHINESE);
                break;
            case DateFormat.yyyyMMddHHmmss:
                df = new SimpleDateFormat(DateFormat.yyyyMMddHHmmss, Locale.CHINESE);
                break;
            case DateFormat.yyyyMMddHHmmssSSS:
                df = new SimpleDateFormat(DateFormat.yyyyMMddHHmmssSSS, Locale.CHINESE);
                break;
            case DateFormat.date_standard:
                df = new SimpleDateFormat(DateFormat.date_standard, Locale.CHINESE);
                break;
            default:
                df = new SimpleDateFormat(DateFormat.date_default, Locale.CHINESE);
        }
        return (df.format(date));
    }

    public static String getYearMonthDay() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return dateTransfer(c.getTime(), DateFormat.date_default);
    }

    public static String getWeek() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int week = c.get(Calendar.DAY_OF_WEEK);
        String mWeek;
        switch (week) {
            case 1:
                mWeek = "周日";
                break;
            case 2:
                mWeek = "周一";
                break;
            case 3:
                mWeek = "周二";
                break;
            case 4:
                mWeek = "周三";
                break;
            case 5:
                mWeek = "周四";
                break;
            case 6:
                mWeek = "周五";
                break;
            case 7:
                mWeek = "周六";
                break;
            default:
                mWeek = null;
                break;
        }
        return mWeek;
    }

    public static String getMonthDay() {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        int month = c.get(Calendar.MONTH) + 1;// 获取当前月份
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mDate;
        switch (month) {
            case 1:
                mDate = "JAN";
                break;
            case 2:
                mDate = "FEB";
                break;
            case 3:
                mDate = "MAR";
                break;
            case 4:
                mDate = "APR";
                break;
            case 5:
                mDate = "MAY";
                break;
            case 6:
                mDate = "JUN";
                break;
            case 7:
                mDate = "JUL";
                break;
            case 8:
                mDate = "AUG";
                break;
            case 9:
                mDate = "SEP";
                break;
            case 10:
                mDate = "OCT";
                break;
            case 11:
                mDate = "NOV";
                break;
            case 12:
                mDate = "DEC";
                break;
            default:
                mDate = null;
                break;
        }
        if (mDate != null) {
            mDate = mDate + " " + day;
        }
        return mDate;
    }

    public static long dateTransfer(String dateStr, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = formatter.parse(dateStr);
        } catch (ParseException e) {

        }
        if (date == null) {
            return 0;
        } else {
            long currentTime = date.getTime();
            return currentTime;
        }
    }

}