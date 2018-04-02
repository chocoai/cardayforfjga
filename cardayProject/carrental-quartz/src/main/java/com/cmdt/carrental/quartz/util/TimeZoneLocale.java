package com.cmdt.carrental.quartz.util;

import java.util.Date;
import java.util.TimeZone;

public class TimeZoneLocale extends TimeZone{

	private static final long serialVersionUID = 6294690622868052699L;

	public TimeZoneLocale(){
		super.setID("Asia/Shanghai");
		super.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	@Override
	public int getOffset(int era, int year, int month, int day, int dayOfWeek, int milliseconds) {
		return 0;
	}

	
	@Override
	public void setRawOffset(int offsetMillis) {
	}

	@Override
	public int getRawOffset() {
		return 28800000;
	}

	@Override
	public boolean useDaylightTime() {
		return false;
	}

	@Override
	public boolean inDaylightTime(Date date) {
		return false;
	}
}
