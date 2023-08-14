package com.example.bookkeeping.utils;

import java.util.Calendar;

public class TimeUtils {
    public static Calendar a = Calendar.getInstance();

    /**
     * 获取某一年某一月份的总天数
     * @param year
     * @param month
     * @return
     */
    public static int getDays(int year, int month) {
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);// Calendar月份是以0开始的 所以要-1
        a.set(Calendar.DATE, 1);//把日期设置为当月第一天
        a.roll(Calendar.DATE, -1);//日期回滚一天，也就是最后一天
        int day = a.get(Calendar.DATE);
        return day;
    }

    public static int getCurYear(){
        return a.get(Calendar.YEAR);
    }

    public static int getCurMonth(){
        return a.get(Calendar.MONTH) + 1;
    }

    public static int getCurDay(){
        return a.get(Calendar.DAY_OF_MONTH);
    }
}
