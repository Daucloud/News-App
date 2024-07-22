package com.java.zhangxinyuan.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Assistant {
    static public String getEndDate(){
        // 创建日期对象并获取当前时间
        Date currentDate = new Date();
        // 创建日期格式化对象
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 格式化日期对象为字符串
       return dateFormat.format(currentDate);
    }
}
