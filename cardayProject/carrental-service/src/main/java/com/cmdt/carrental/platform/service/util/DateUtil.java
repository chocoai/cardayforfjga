package com.cmdt.carrental.platform.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.log4j.Logger;

public class DateUtil {
	
	private static final Logger LOG = Logger.getLogger(DateUtil.class);
	private static final String SDF_DATE = "yyyy-MM-dd HH:mm:ss";
	
	/* 
     * 将时间戳转换为时间
     */
	public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
	
	/* 
     * 将时间格式化
     */
	public static String dateToString(Date date){
		SimpleDateFormat time=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		return time.format(date);
	}
	
	/**
	 * 比较两个日期，date1是否小于date2
	 * 
	 * @param date1
	 * @param date2
	 * @return true：date1《date2,false:date1>=date2
	 * @throws ParseException 
	 */
	public static boolean compareDate(String date1, String date2) throws ParseException {
		boolean flag = false;
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		Date a = dateformat.parse(date1);
		Date b = dateformat.parse(date2);
		flag = a.before(b);
		return flag;
	}
	
}
