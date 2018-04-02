package com.cmdt.carrental.common.util;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypeUtils {
	private static final Logger LOG = LoggerFactory.getLogger(TypeUtils.class);

	public static void main(String[] args){
		System.out.println(obj2DateFormat("2000/1/1"));
	}
	
	public static String obj2String(Object obj) {
		try {
			if(null==obj)
			{
				return "";
			}else{
				return String.valueOf(obj);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke obj2String method!", e);
			return "";
		}
	}

	public static Integer obj2Integer(Object obj) {
		try {
			String val=obj2String(obj);
			val=new DecimalFormat("0").format(obj2Double(val));
			if(StringUtils.isBlank(val))
			{
				return 0;
			}else{
				return Integer.valueOf(val);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke obj2Integer method!", e);
			return 0;
		}
	}
	
	public static Long obj2Long(Object obj) {
		try {
			String val=obj2String(obj);
			val=new DecimalFormat("0").format(obj2Double(val));
			if(StringUtils.isBlank(val))
			{
				return 0l;
			}else{
				return Long.valueOf(val);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke obj2Long method!", e);
			return 0l;
		}
	}
	
	public static Double obj2Double(Object obj) {
		try {
			String val=obj2String(obj);
			if(StringUtils.isBlank(val))
			{
				return 0d;
			}else{
				return Double.valueOf(val);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke obj2Double method!", e);
			return 0d;
		}
	}
	
	public static Date obj2Date(Object obj) {
		try {
			String val=obj2String(obj);
			if(StringUtils.isBlank(val))
			{
				return null;
			}else{
				if(val.indexOf("/")>-1){
					val=val.replaceAll("/", "-");
				}
				return DateUtils.string2Date(val,DateUtils.FORMAT_YYYY_MM_DD_HH_MI_SS);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke Timestamp method!", e);
			return null;
		}
	}
	public static Date obj2DateFormat(Object obj) {
		try {
			String val=obj2String(obj);
			if(StringUtils.isBlank(val))
			{
				return null;
			}else{
				if(val.indexOf("/")>-1){
					val=val.replaceAll("/", "-");
				}
				return DateUtils.string2Date(val,DateUtils.FORMAT_YYYY_MM_DD);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke Timestamp method!", e);
			return null;
		}
	}
	public static Date obj2DateSp(Object obj) {
		try {
			String val=obj2String(obj);
			if(StringUtils.isBlank(val))
			{
				return null;
			}else{
				return DateUtils.string2Date(val,DateUtils.FORMAT);
			}
		} catch (Exception e) {
			LOG.error("Failed to invoke Timestamp method!", e);
			return null;
		}
	}
	
	public static Timestamp obj2Timestamp(Object obj) throws Exception{
		String val = obj2String(obj);
		if (StringUtils.isBlank(val)) {
			return null;
		} else {
			if (val.indexOf("/") > -1) {
				val = val.replaceAll("/", "-");
			}
			return Timestamp.valueOf(val);
		}
	}
	
	public static Boolean obj2Bool(Object obj){
		String val = obj2String(obj);
		if (StringUtils.isBlank(val)) {
			return false;
		} else {
		    if(val.trim().toUpperCase().equals("TRUE")||val.trim().toUpperCase().equals("FALSE")){
		    	return Boolean.valueOf(val.trim());
		    }else{
		    	return null;
		    }
		}
	}
	
}
