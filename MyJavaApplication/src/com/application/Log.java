package com.application;

import java.util.Calendar;

public class Log {

    private static boolean DeBUG = false;

    private static void writeLog(String tag, String info) {
        // 其日历字段已由当前日期和时间初始化：
        Calendar rightNow = Calendar.getInstance(); // 子类对象
        // 获取年
        int year = rightNow.get(Calendar.YEAR);
        // 获取月
        int month = rightNow.get(Calendar.MONTH);
        // 获取日
        int date = rightNow.get(Calendar.DATE);
        //获取几点
        int hour = rightNow.get(Calendar.HOUR_OF_DAY);
        int minute = rightNow.get(Calendar.MINUTE);
        int second = rightNow.get(Calendar.SECOND);
        int milSecond = rightNow.get(Calendar.MILLISECOND);
        //获取上午下午
        int moa=rightNow.get(Calendar.AM_PM);
        String am_pm;
        if(moa==1)
            am_pm = "PM";
        else
            am_pm = "AM";
        String time = year + "/" + month + "/" + date + " " + hour + ":" + minute + ":" + second + ":" + milSecond + " " + am_pm;
        System.out.println(time + "  " + tag + ":  " + info);
    }

    public static void d(String tag, String info) {
        if (DeBUG) {
            writeLog(tag, info);
        }
    }

    static void i(String tag, String info) {
        writeLog(tag, info);
    }

    static void e(String tag, String info) {
        writeLog(tag, info);
    }

    static void w(String tag, String info) {
        writeLog(tag, info);
    }
}
