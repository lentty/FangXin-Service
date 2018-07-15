package com.cqfangxin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatUtil {
    public static String formatTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String formatDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String timeFormat(String time){
        return time == null ? null : time.substring(0,19);
    }
}
