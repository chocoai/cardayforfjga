package com.cmdt.carrental.common.util;

public class BrowserUtil {
	
	private static final String BROWSER_TYPE_AGENT_IE7 = "msie 7";
	private static final String BROWSER_TYPE_AGENT_IE8 = "msie 8";
	private static final String BROWSER_TYPE_AGENT_IE9 = "msie 9";
	private static final String BROWSER_TYPE_AGENT_IE10 = "msie 10";
	private static final String BROWSER_TYPE_AGENT_IE = "msie";
	private static final String BROWSER_TYPE_AGENT_OPERA = "opera";
	private static final String BROWSER_TYPE_AGENT_FIREFOX = "firefox";
	private static final String BROWSER_TYPE_AGENT_WEBKIT = "webkit";
	private static final String BROWSER_TYPE_AGENT_IE11 = "gecko";
	private static final String BROWSER_TYPE_AGENT_IE11_VE = "rv:11";
	
	public static final String BROWSER_NAME_IE7 = "IE7";
	public static final String BROWSER_NAME_IE8 = "IE8";
	public static final String BROWSER_NAME_IE9 = "IE9";
	public static final String BROWSER_NAME_IE10 = "IE10";
	public static final String BROWSER_NAME_IE = "IE";
	public static final String BROWSER_NAME_OPERA = "OPERA";
	public static final String BROWSER_NAME_FIREFOX = "FIREFOX";
	public static final String BROWSER_NAME_WEBKIT = "WEBKIT";
	public static final String BROWSER_NAME_IE11 = "IE11";
	public static final String BROWSER_NAME_OTHER = "OTHER";
	
	
	public static String getBrowserName(String agent) {
		  if(agent.indexOf(BROWSER_TYPE_AGENT_IE7)>0){
		   return BROWSER_NAME_IE7;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_IE8)>0){
		   return BROWSER_NAME_IE8;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_IE9)>0){
		   return BROWSER_NAME_IE9;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_IE10)>0){
		   return BROWSER_NAME_IE10;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_IE)>0){
		   return BROWSER_NAME_IE;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_OPERA)>0){
		   return BROWSER_NAME_OPERA;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_FIREFOX)>0){
		   return BROWSER_NAME_FIREFOX;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_WEBKIT)>0){
		   return BROWSER_NAME_WEBKIT;
		  }else if(agent.indexOf(BROWSER_TYPE_AGENT_IE11)>0 && agent.indexOf(BROWSER_TYPE_AGENT_IE11_VE)>0){
		   return BROWSER_NAME_IE11;
		  }else{
		   return BROWSER_NAME_OTHER;
		  }
   }
}
