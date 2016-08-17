package com.sobey.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {

    private static GregorianCalendar gc = new GregorianCalendar();

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getNowTime(String format) {
        return new SimpleDateFormat(format).format(Calendar.getInstance().getTime());
    }

    /**
     * 日期转字符串  格式:yyyy年MM月dd日 E (带星期)
     *
     * @param date
     * @return
     */
    public static String getTime(Date date) {
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat("yyyy年MM月dd日 E").format(date);
        }
    }

    /**
     * 日期转字符串  格式:yyyy/MM/dd
     *
     * @param date
     * @return
     */
    public static String getTimeSimple(Date date) {
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat("yyyy/MM/dd").format(date);
        }
    }

    /**
     * 根据格式日期转字符串
     *
     * @param date
     * @return
     */
    public static String getTimeFor(String format, Date date) {
        if (date == null) {
            return "";
        } else {
            return new SimpleDateFormat(format).format(date);
        }
    }

    /**
     * 根据格式转日期
     *
     * @param format
     * @param dateStr
     * @return
     */
    public static Date getDateByStr(String format, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 把时间字符串转为指定格式的时间字符串
     * @param formatfrom
     * @param formatto
     * @param dateStr
     * @return
     */
    public static String getStrByStr(String formatfrom,String formatto, String dateStr){
        Date date = getDateByStr(formatfrom, dateStr);
        String ret = getTimeFor(formatto, date);
        return ret;
    }

    /**
     * 去掉日期后面的时分秒
     *
     * @param date
     * @return
     */
    public static Date getDateNoSC(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String temp = sdf.format(date);
        try {
            date = sdf.parse(temp);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     * 日期加减
     * *gc.add(1,-1)表示年份减一.  YEAR
     * gc.add(2,-1)表示月份减一.  MONTH
     * gc.add(3.-1)表示周减一.   WEEK_OF_YEAR
     * gc.add(5,-1)表示天减一.   DATE
     */
    public static Date add(Date date, int field, int value) {
        gc.setTime(date);
        gc.add(field, value);
        return gc.getTime();
    }

    public static String add(String format,String datestr, int field, int value) {
        Date date = TimeUtil.getDateByStr(format, datestr);
        Date datelast = TimeUtil.add(date, field, value);
        return TimeUtil.getTimeFor(format, datelast);
    }

    /**
     * 两天相减获取天数
     */
    public static int subDay(Date beginDate, Date endDate){
        return (int) ((endDate.getTime()-beginDate.getTime())/(24*60*60*1000));
    }

    /**
     * 日起相减获取月份差
     */
    public static int subMouth(Date start, Date end) {
//        if (start.after(end)) {
//            Date t = start;
//            start = end;
//            end = t;
//        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(start);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(end);
        Calendar temp = Calendar.getInstance();
        temp.setTime(end);
        temp.add(Calendar.DATE, 1);

        int year = endCalendar.get(Calendar.YEAR)
                - startCalendar.get(Calendar.YEAR);
        int month = endCalendar.get(Calendar.MONTH)
                - startCalendar.get(Calendar.MONTH);

        if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month + 1;
        } else if ((startCalendar.get(Calendar.DATE) != 1)
                && (temp.get(Calendar.DATE) == 1)) {
            return year * 12 + month;
        } else if ((startCalendar.get(Calendar.DATE) == 1)
                && (temp.get(Calendar.DATE) != 1)) {
            return year * 12 + month;
        } else {
            return (year * 12 + month - 1) < 0 ? 0 : (year * 12 + month);
        }
    }

    public static String cutDateStr(String dateStr) {
        return dateStr.substring(0, 7);
    }

    public static void main(String[] args) {
//		System.out.println(TimeUtil.getNowTime("yyyy-MM-dd HH:mm:ss"));
//		System.out.println(TimeUtil.getNowTime("yyyy年MM月dd日 HH:mm"));
        System.out.println(TimeUtil.getTime(new Date()));
    }
}
