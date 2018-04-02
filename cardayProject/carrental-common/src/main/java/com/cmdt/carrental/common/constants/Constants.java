package com.cmdt.carrental.common.constants;

import java.util.HashMap;
import java.util.Map;

import com.cmdt.carrental.common.bean.AlertType;

public abstract class Constants {

	public final static Map<String,String> ALERT_NOTIFICATION_TITLES = new HashMap<>();
	/** 特殊警务权限标识 */
	public final static String SPECIAL_SERVICE = "order:special-service";

	static{
		ALERT_NOTIFICATION_TITLES.put(AlertType.OUTBOUND.toString(), "车辆越界报警");
		ALERT_NOTIFICATION_TITLES.put(AlertType.OVERSPEED.toString(), "车辆超速报警");
		ALERT_NOTIFICATION_TITLES.put(AlertType.VEHICLEBACK.toString(), "车辆回站报警");
	}
	
	public final static String CARDAY_ADMIN = "CARDAY.admin";
	public final static String CARDAY_ENDUSER = "CARDAY.enduser";
	public final static String CARDAY_DRIVER = "CARDAY.driver";
	public final static String PUSH_VENDOR = "JPush";
	
	public final static String SMS_VENDOR = "2";
	public final static String SMS_CUSTOMER_ID = "10002";
	
	public final static Long ENTER_MANAGER = 4L;
	public final static Long DEPT_MANAGER = 5L;
	public final static Long EMPLOYEE = 6L;
	public final static Long DRIVER = 7L;
	
	public final static String COMMAND_INSTRUCTION_EQUALS="=";
	public final static String COMMAND_INSTRUCTION_COLON=":";
	public final static String COMMAND_KEY = "VEHICLE_BATCH_SET";
	public final static String COMMAND_INSTRUCTION_SPEED="SPEED";
	
	
	public final static String VEHICLE_RUNTIME_STATUS_OFFLINE="离线";
	
	public final static String STATISTIC_TYPE_WARNING = "warning";
	
	public final static String USAGE_TYPE_MILE = "mile";
	public final static String USAGE_TYPE_FUEL = "fuel";
	public final static String USAGE_TYPE_TIME = "time";
	
	
	public final static String USAGE_TYPE_MILE_FLAG = "里程";
	public final static String USAGE_TYPE_MILE_UNIT = "单位:千米";
	
	public final static String USAGE_TYPE_FUEL_FLAG = "耗油量";
	public final static String USAGE_TYPE_FUEL_UNIT = "单位:升";
	
	public final static String USAGE_TYPE_TIME_FLAG = "行驶时长";
	public final static String USAGE_TYPE_TIME_UNIT = "单位:小时";

	
	public final static String DEVICE_VENFOR_DBJ = "DBJ";
	public final static String DEVICE_VENFOR_DH = "DH";
	public final static String DEVICE_VENFOR_GENIUS = "LS_GENIUS";
	public final static String DEVICE_VENFOR_GOSAFE = "GOSAFE";

	/** 短信推送成功时 返回的 statusCode */
	public final static String SMS_Publish_Response_Success = "20000";
	
	
	/* m2m */
	public final static String M2M_CONTENT_TYPE = "application/www-form-urlencoded";
	public final static String M2M_CITYCODE = "591";
	public final static String M2M_APPID = "5911323401";
	public final static String M2M_EBID = "000000001297";
//	public final static String M2M_PASSWORD = "fzD2D_401";
	public final static String M2M_PASSWORD = "D2Dapi_591";

	/* 地理围栏绘制类型*/
	public final static String MARKER_TYPE_REGION_DIVID = "0";
	public final static String MARKER_TYPE_DRAW_SELF = "1";
	public final static String MARKER_TYPE_CIRCLE = "2";
	
}
