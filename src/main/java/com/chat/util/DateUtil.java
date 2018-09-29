package com.chat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description 时间工具类
 * @author loveyouso
 * @create 2018-09-18 10:44
 **/
public class DateUtil {

    private final String  partternAll = "yyyy-MM-dd: HH:mm:ss";

    private final String partternPart = "yyyy-MM-dd";

    /**
     * @description 获取格式化后的当前时间yyyy-MM-dd
     * @return
     */
    public String getDateString(){
        return new SimpleDateFormat(partternPart).format(new Date());
    }

    /**
     * @description 获取特定格式的当前时间
     * @return
     */
    public String getDateTimeString (String parttern){
        return new SimpleDateFormat(parttern).format(new Date());
    }
    /**
     * @description 获取当前时间yyyy-MM-dd: HH:mm:ss
     * @return
     */
    public String getDateTimeString(){
        return new SimpleDateFormat(partternAll).format(new Date());
    }
    /**
     * @description 获取特定格式的当前时间 yyyy-MM-dd
     * @return
     */
    public Date getDate(){
        try {
            return new SimpleDateFormat(partternPart).parse(getDateString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 获取特定格式的当前时间 yyyy-MM-dd: HH:mm:ss
     * @return
     */
    public Date getDateTime(){
        try {
            return new SimpleDateFormat(partternAll).parse(getDateTimeString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description 获取当前时间Long类型
     * @return
     */
    public Long getLongDateTime(){
        return new Date().getTime();
    }

}
