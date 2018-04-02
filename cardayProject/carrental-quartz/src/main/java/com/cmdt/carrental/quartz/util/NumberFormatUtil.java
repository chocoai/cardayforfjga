package com.cmdt.carrental.quartz.util;

import java.math.BigDecimal;

public class NumberFormatUtil {
	
	public static double formatNumberDecimals(double number,int decimal){
		BigDecimal b = new BigDecimal(number);  
		double f = b.setScale(decimal,BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}

}
