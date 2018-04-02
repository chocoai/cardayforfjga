package com.cmdt.carrental.common.model;


public class UsageReportAllMileageAndFuleconsModel {

	String todayMileage;//当日里程
	String yesterdayMileage;//昨日里程
	String currentweekMileage;//本周里程
	String currentMonthMileage;//本月里程
	String totalMileage;//累计总里程(里程表数据,接口getLocationByImei)
	
	String todayFuelcons;//当日油耗
	String yesterdayFuelcons;//昨日油耗
	String currentweekFuelcons;//本周油耗
	String currentMonthFuelcons;//本月油耗
	String totalFuelcons;//累计总油耗(油耗表数据,接口getLocationByImei)
	
	public String getTodayMileage() {
		return todayMileage;
	}
	public void setTodayMileage(String todayMileage) {
		this.todayMileage = todayMileage;
	}
	public String getYesterdayMileage() {
		return yesterdayMileage;
	}
	public void setYesterdayMileage(String yesterdayMileage) {
		this.yesterdayMileage = yesterdayMileage;
	}
	public String getCurrentweekMileage() {
		return currentweekMileage;
	}
	public void setCurrentweekMileage(String currentweekMileage) {
		this.currentweekMileage = currentweekMileage;
	}
	public String getCurrentMonthMileage() {
		return currentMonthMileage;
	}
	public void setCurrentMonthMileage(String currentMonthMileage) {
		this.currentMonthMileage = currentMonthMileage;
	}
	public String getTotalMileage() {
		return totalMileage;
	}
	public void setTotalMileage(String totalMileage) {
		this.totalMileage = totalMileage;
	}
	public String getTodayFuelcons() {
		return todayFuelcons;
	}
	public void setTodayFuelcons(String todayFuelcons) {
		this.todayFuelcons = todayFuelcons;
	}
	public String getYesterdayFuelcons() {
		return yesterdayFuelcons;
	}
	public void setYesterdayFuelcons(String yesterdayFuelcons) {
		this.yesterdayFuelcons = yesterdayFuelcons;
	}
	public String getCurrentweekFuelcons() {
		return currentweekFuelcons;
	}
	public void setCurrentweekFuelcons(String currentweekFuelcons) {
		this.currentweekFuelcons = currentweekFuelcons;
	}
	public String getCurrentMonthFuelcons() {
		return currentMonthFuelcons;
	}
	public void setCurrentMonthFuelcons(String currentMonthFuelcons) {
		this.currentMonthFuelcons = currentMonthFuelcons;
	}
	public String getTotalFuelcons() {
		return totalFuelcons;
	}
	public void setTotalFuelcons(String totalFuelcons) {
		this.totalFuelcons = totalFuelcons;
	}
	
	
	
}
