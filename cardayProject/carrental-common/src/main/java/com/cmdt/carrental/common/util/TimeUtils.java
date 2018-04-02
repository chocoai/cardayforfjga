package com.cmdt.carrental.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.model.TimeRangeModel;

public class TimeUtils {

	private static final String SDF_DATE = "yyyy-MM-dd HH:mm:ss";

	private static String defaultDatePattern = null;
	public static final String TS_FORMAT = TimeUtils.getDatePattern() + " HH:mm:ss.S";
	public static final String DAY_END_STRING_HHMMSS = " 23:59:59";

	public static long timeBetween(Date startDate, Date endDate, int type) {
		switch (type) {
		case Calendar.MINUTE:
			return endDate.getTime() / 60000 - startDate.getTime() / 60000;
		case Calendar.HOUR:
			return endDate.getTime() / 3600000 - startDate.getTime() / 3600000;
		case Calendar.DATE:
			return endDate.getTime() / 86400000 - startDate.getTime() / 86400000;
		case Calendar.MONTH: {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);
			return (endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR)) * 12
					+ endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
		}
		case Calendar.YEAR: {
			Calendar startCalendar = Calendar.getInstance();
			startCalendar.setTime(startDate);
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.setTime(endDate);
			return endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
		}
		}
		return 0;
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getDatetime() {
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		return dateformat.format(new Date());
	}

	public static String getTimestampTime(Timestamp time) {
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		return dateformat.format(time);
	}

	/**
	 * 将时间字符串转换为Date类
	 * 
	 * @param time
	 * @return
	 */
	public static Date getDatetime(String time) {
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		Date date = null;
		try {
			if (!StringUtils.isEmpty(time)) {
				date = dateformat.parse(time);
			} else {
				date = new Date();
			}
		} catch (ParseException e) {
		}
		return date;
	}
	
	/**
	 * 将时间字符串转换为Date类
	 * 
	 * @param time
	 * @return
	 */
	public static java.sql.Date getDaytime(String time) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Date date = null;
		try {
			if (!StringUtils.isEmpty(time)) {
				date = dateformat.parse(time);
			} else {
				date = new Date();
			}
		} catch (ParseException e) {
		}
		return new java.sql.Date(date.getTime());
	}
	
	/**
	 * 将时间字符串转换为Date类
	 * 
	 * @param time
	 * @return
	 */
	public static java.sql.Date getDaytimeInfo(String time) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Date date = null;
		try {
			if (!StringUtils.isEmpty(time)) {
				date = dateformat.parse(time);
			} else {
				date = new Date();
			}
		} catch (ParseException e) {
		}
		return new java.sql.Date(date.getTime());
	}

	/**
	 * 将时间字符串转换为Timestamp类
	 * 
	 * @param time
	 * @return
	 */
	public static Timestamp getTimestamptime(String time) {
		return new Timestamp(getDatetime(time).getTime());
	}

	public static long getBetweenSeconds(String startTime, String endTime) {
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		Date startTimeVal = null;
		Date endTimeVal = null;
		try {
			startTimeVal = dateformat.parse(startTime);
			endTimeVal = dateformat.parse(endTime);
		} catch (ParseException e) {
		}
		if (startTimeVal != null && endTimeVal != null) {
			return (endTimeVal.getTime() - startTimeVal.getTime()) / 1000;
		}
		return 0;
	}

	/**
	 * 比较两个日期，date1是否小于date2
	 * 
	 * @param date1
	 * @param date2
	 * @return true：date1《date2,false:date1>=date2
	 */
	public static boolean compareDate(String date1, String date2) {
		boolean flag = false;
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		try {
			Date a = dateformat.parse(date1);
			Date b = dateformat.parse(date2);
			flag = a.before(b);
		} catch (ParseException e) {
		}
		return flag;
	}

	/**
	 * validate the date format to yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateString
	 * @return
	 */
	public static boolean isValidDate(String dateString) {
		boolean convertSuccess = true;
		SimpleDateFormat dateformat = new SimpleDateFormat(SDF_DATE, Locale.CHINA);
		try {
			dateformat.setLenient(false);
			dateformat.parse(dateString);
		} catch (ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}

	public static Date formatDate(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			throw new RuntimeException("input_date_invalid_102");
		}
		return date;
	}

	public static String formatDate(Date date) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		} catch (Exception e) {
			throw new RuntimeException("input_date_invalid_102");
		}
	}

	public static String formatWithPattern(String pattern, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static synchronized String getDatePattern() {
		defaultDatePattern = "yyyy-MM-dd";
		return defaultDatePattern;
	}

	public static String getDate() {
		try {
			Calendar cale = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(cale.getTime());
		} catch (Exception e) {
			return "";
		}
	}


	public static String getLastMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date curDate = java.sql.Date.valueOf(date);
		calendar.setTime(curDate);
		calendar.set(Calendar.MONDAY, calendar.get(Calendar.MONDAY) - 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
		return sdf.format(calendar.getTime());
	}

	public static String getBeforeTime(String date, Integer minutes){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		Date curDate = java.sql.Date.valueOf(date);
		calendar.setTime(curDate);
		calendar.add(Calendar.MINUTE, minutes);
		return sdf.format(calendar.getTime());
	}
	public static Date getEndTimeByYesterday(Date date){
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
	    todayEnd.set(Calendar.SECOND, 59);
	    todayEnd.set(Calendar.MILLISECOND, 999);
	    todayEnd.add(Calendar.DATE, -1);
		return todayEnd.getTime();
	}
	public static Date getStartTimeByYesterday(Date date){
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(date);
		todayEnd.set(Calendar.HOUR_OF_DAY, 0);
		todayEnd.set(Calendar.MINUTE, 0);
	    todayEnd.set(Calendar.SECOND, 0);
	    todayEnd.set(Calendar.MILLISECOND, 0);
	    todayEnd.add(Calendar.DATE, -1);
		return todayEnd.getTime();
	}
	public static String formatScheduleTime(Date date) {
		String dateFormat = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd$HH:mm:ss");
			dateFormat = sdf.format(date);
			dateFormat = dateFormat.replaceFirst("\\$", "T");
		} catch (Exception e) {
			throw new RuntimeException("input_date_invalid_102");
		}
		return dateFormat;
	}
	
	public static Date countDateByMonth(Date startDate, int monthNum) {
		int days = 0;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(startDate);
		c1.add(Calendar.MONTH, monthNum);
		Date date=c1.getTime();
		return date;
	}
	
	public static int calDiffMonth(Date startDate, Date endDate) {
		int result = 0;
		int startYear = getYear(startDate);
		int startMonth = getMonth(startDate);
		int startDay = getDay(startDate);
		int endYear = getYear(endDate);
		int endMonth = getMonth(endDate);
		int endDay = getDay(endDate);
		if (startDay > endDay) { // 1月17 大于 2月28
			if (endDay == getDaysOfMonth(getYear(new Date()), 2)) { // 也满足一月
				result = (endYear - startYear) * 12 + endMonth - startMonth;
			} else {
				result = (endYear - startYear) * 12 + endMonth - startMonth - 1;
			}
			if(result >= 1) {
				result += 1;
			}
		} else {
			result = (endYear - startYear) * 12 + endMonth - startMonth;
			if(endDay > startDay) {
				result += 1;
			}
		}

		return result;
	}
	
	private static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }
	
	private static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
	
	private static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    private static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
    
    /* 
     * 将时间格式化
     */
	public static String dateToString(Date date){
		SimpleDateFormat time=new SimpleDateFormat(getDatePattern()); 
		return time.format(date);
	}
	
    /**
     * 判断用户输入时间是否大于当前时间
     * @param inputTime
     * @return
     */
    public static boolean isMoreThanCurrentTime(String inputTime,Date date){
    	Date inputDate = formatDate(inputTime);
    	if(inputDate.compareTo(date) > 0){
    		return true;
    	}
    	return false;
    }
	
    //由出生日期获得年龄
    public static int getAge(Date birthDay) {  
        Calendar cal = Calendar.getInstance();  
  
        if (cal.before(birthDay)) {  
            return 0;
        }  
        int yearNow = cal.get(Calendar.YEAR);  
        int monthNow = cal.get(Calendar.MONTH);  
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);  
        cal.setTime(birthDay);  
  
        int yearBirth = cal.get(Calendar.YEAR);  
        int monthBirth = cal.get(Calendar.MONTH);  
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);  
  
        int age = yearNow - yearBirth;  
  
        if (monthNow <= monthBirth) {  
            if (monthNow == monthBirth) {  
                if (dayOfMonthNow < dayOfMonthBirth) age--;  
            }else{  
                age--;  
            }  
        }  
        return age;  
    } 
  //由出生日期获得年龄
    public static int getAge(String birthDay) {  
    	return getAge(getDaytime(birthDay));
    }  
    
    /**
     * 时间分割
     * @param starttime
     * @param endtime
     * @return
     */
    public static List<TimeRangeModel> getSplitTimeList(String starttime,String endtime){
    	List<TimeRangeModel> retList = splitTime(starttime,endtime);
    	populateTimeRange(retList,endtime);
    	return retList;
    }
    
    public static List<TimeRangeModel> splitTime(String starttime,String endtime){
    	
    	List<TimeRangeModel> retList = new ArrayList<TimeRangeModel>();
    	try {
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	
        	Calendar calBegin = Calendar.getInstance();
    	    Date dBegin = sdf.parse(starttime);
      	    calBegin.setTime(dBegin);
      	    
      	    Calendar calEnd = Calendar.getInstance();
      	    Date dEnd = sdf.parse(endtime);
    	    calEnd.setTime(dEnd);
      	    
    	    TimeRangeModel timeRangeModel = null;
    	    
      	    while(calBegin.before(calEnd)){
      	    	timeRangeModel = new TimeRangeModel();
      	    	timeRangeModel.setStarttime(sdf.format(calBegin.getTime()));
      	    	retList.add(timeRangeModel);
      	    	
      	    	calBegin.add(Calendar.HOUR_OF_DAY, 24);
      	    }
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return retList;
    }
    
    public static void populateTimeRange(List<TimeRangeModel> retList,String endtime){
    	
    	if(retList != null && retList.size() > 0){
    		int lastIndex = retList.size() - 1;
        	for(int i = 0 ; i < retList.size() ; i ++){
        		TimeRangeModel timeRangeModel = retList.get(i);
        		if(i != lastIndex){
        			timeRangeModel.setEndtime(getLastSecTime(retList.get(i+1).getStarttime()));
        		}else{
        			timeRangeModel.setEndtime(endtime);
        		}
        	}
    	}
    }
    
    public static String getLastSecTime(String currentTime){
    	String lastSecTime = "";
    	try {
    	 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date date = sdf.parse(currentTime);
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.SECOND, -1);
		 
		 lastSecTime = sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return lastSecTime;
    } 
    
    public static Double getBetweenMinutes(Date date1,Date date2){
    	
    	return (double)(date2.getTime()-date1.getTime())/(1000*60);
    }
}
