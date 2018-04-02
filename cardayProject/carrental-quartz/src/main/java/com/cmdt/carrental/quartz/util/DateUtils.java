package com.cmdt.carrental.quartz.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class DateUtils {
	private static final Logger LOG = LoggerFactory.getLogger(DateUtils.class);
    public static final String FORMAT_YYYY_MM_DD_HH_MI_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_YYYY_MM_DD_HH_MI_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
     
    /**
     * 获取当前时间yyyy-MM-dd
     * @return
     */
    public static String getNowDate() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_YYYY_MM_DD);
        return df.format(new Date());
    }
    
    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getNowDate(String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(new Date());
    }
    
    /**
     * 获取系统当前时间yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public static String getNowTime() {
        return getNowDate(FORMAT_YYYY_MM_DD_HH_MI_SS_SSS);
    }
    
    /**
     * Date类型转为指定格式的String类型
     * 
     * @param source
     * @param pattern
     * @return
     */
    public static String date2String(Date source, String pattern) {
    	SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(source);
    }

    /**
     * 字符串转换为对应日期(可能会报错异常)
     * @param source
     * @param pattern
     * @return
     */
    public static Date string2Date(String source, String pattern) {
    	SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = df.parse(source);
        } catch (ParseException e) {
            LOG.error("字符串转换日期异常", e);
        }
        return date;
    }
    
    /**
     * 日期计算加减年
     */
    public static Date addYears(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.YEAR, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减月
     */
    public static Date addMonths(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MONTH, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减周
     */
    public static Date addWeeks(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.WEEK_OF_YEAR, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减天
     */
    public static Date addDays(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.DATE, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减小时
     */
    public static Date addHours(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.HOUR, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减分钟
     */
    public static Date addMinutes(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MINUTE, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减秒
     */
    public static Date addSeconds(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.SECOND, val);
    	return cal.getTime();
    }
    
    /**
     * 日期计算加减毫秒
     */
    public static Date addMilliseconds(Date dt,int val)
    {
    	Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        cal.add(Calendar.MILLISECOND, val);
    	return cal.getTime();
    }
    
    /**
     * 两个时间比较
     * @param date
     * @return
     */
    public static int compareDateWithNow(Date date1,Date date2){
        int rnum =date1.compareTo(date2);
        return rnum;
    }
    
    /**
     * 获得星期序列
     * @param date
     * @return
     */
    public static int getWeekOfDate(Date date) {      
    	//"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
        int[] weekOfDays = {1, 2, 3, 4, 5, 6, 7};        
        Calendar calendar = Calendar.getInstance();      
        if(date != null){        
             calendar.setTime(date);      
        }        
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;      
        if (w < 0){        
            w = 0;      
        }      
        return weekOfDays[w];    
    }
    
    /**
     * 获得截止到本周的日期list
     * @param args
     */
     public static List<String> getDaysOFCurrentWeek(Date today){
    	List<String> dates = new ArrayList<String>();
     	int currentDayIndex = getWeekOfDate(today);
     	if(currentDayIndex == 1){
     		dates.add(date2String(today,"yyyy-MM-dd"));
     	}else{
     		for(int i = 1 ; i < currentDayIndex ; i ++){
         		Date oldDate = addDays(today,-i);
         		dates.add(date2String(oldDate,"yyyy-MM-dd"));
         	}
            dates.add(date2String(today,"yyyy-MM-dd"));
     	}
     	return dates;
     }
}