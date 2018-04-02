package com.cmdt.carday.microservice.util;

public class Patterns {
	
	public static final String REG_MOBILE_PHONE = "^(1\\d{10})$";
	public static final String REG_DATE_FORMAT_SIX_DIG="(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))" +
                                                       "|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-8]))))" +
                                                       "|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))0229)";

	public static final String REG_DATE_YYYY_MM_DD="^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$";
	public static final String REG_DATE_YYYY_MM_DD_HH24_MI_SS="^((?:19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01]) (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
	public static void main(String args[]){
		String phoneNumber = "1397274759111";
	    String sixDigDate = "20178810";
	    System.out.println(phoneNumber.matches(REG_MOBILE_PHONE)+" "+sixDigDate.matches(REG_DATE_FORMAT_SIX_DIG));
	}
}
