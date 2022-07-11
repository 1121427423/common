package com.upking.project.common.utils;

import com.upking.project.common.constant.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author king
 * @version 1.0
 * @className DateUtils
 * @description 日期工具类
 * @date 2022/6/18
 */
public class DateUtils {

    /**
     * 获取当前时间戳
     * @return 返回13位的long型数字
     */
    public static long getCurrTimeStamp() {
        return System.currentTimeMillis();
    }

    /**
     * 日期转时间戳
     * @param date 指定日期
     * @return
     */
    public static String date2TimeStamp(Date date) {
        return String.valueOf(date.getTime());
    }

    /**
     * 时间戳转指定格式日期
     * @param timeStamp 长整型
     * @param format 日期格式
     * @return 返回format格式字符串日期
     */
    public static String timeStamp2Date(long timeStamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timeStamp));
    }

    /**
     * 获取指定格式的当前时间
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }


    /**
     * 可以获取当前日期往后推N天的日期
     * @param days 大于零的正整数
     * @return yyyy-MM-dd格式的字符串
     */
    public String backDate(int days) {
        if (days < 0) {
            try {
                throw new IllegalArgumentException("days 必须为大于零的正整数");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 可以获取当前日期往前推N天的日期
     * @param days 大于零的正整数
     * @return yyyy-MM-dd格式的字符串
     */
    public String forwardDate(int days) {
        if (days < 0) {
            try {
                throw new IllegalArgumentException("days 必须为大于零的正整数");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 可以获取当前日期往前h或往后推N天的日期
     * @param days 正数往后，负数往前
     * @return yyyy-MM-dd格式的字符串
     */
    public String forwardOrBackDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    /**
     * 可以获取当前日期往前h或往后推N天的日期
     * @param date 指定日期
     * @param days 正数往后，负数往前
     * @param format 日期格式
     * @return format格式的字符串
     */
    public String forwardOrBackDate(Date date, int days, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(calendar.getTime());
    }

    /**
     *
     * @param date 日期
     * @param format 日期格式
     * @return format格式化日期
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前年份
     */
    public static String getCurrentYear() {
        SimpleDateFormat yearSDF = new SimpleDateFormat(DateFormat.YEAR);
        return yearSDF.format(new Date());
    }

    /**
     * 获取当前月份
     */
    public static String getCurrentMonth() {
        SimpleDateFormat monthSDF = new SimpleDateFormat(DateFormat.MONTH);
        return monthSDF.format(new Date());
    }

    /**
     * 获取当前日
     */
    public static String getCurrentDay() {
        SimpleDateFormat daySDF = new SimpleDateFormat(DateFormat.DAY);
        return daySDF.format(new Date());
    }

    /**
     * 获取当前日期是星期几
     * @return 星期一
     */
    public static String getCurrDayOfWeek() {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        return sdf.format(new Date());
    }

    /**
     * 获取指定日期是星期几
     * @param date 指定日期
     * @return 星期一
     */
    public static String getCurrDayOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        return sdf.format(date);
    }

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2022-06-19");
        System.out.println(getCurrDayOfWeek());

    }

}
