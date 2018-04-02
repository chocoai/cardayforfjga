package com.cmdt.carrental.common.util;

import java.sql.Timestamp;
import java.util.Date;

import com.cmdt.carrental.common.bean.AlertType;
import com.cmdt.carrental.common.model.VehicleAlertModel;

public class MessageTemplateUtil {
	
	public static String composeMessageForAlert(VehicleAlertModel alert){
		String msg = null;
		String dateStr = alert.getAlertTime()!=null?DateUtils.date2String(alert.getAlertTime(), "yyyy年MM月dd日 HH时mm分ss秒"):"<未知时间>";
		if(alert.getAlertType().equals(AlertType.OVERSPEED.toString())){
			msg = "车辆["+alert.getVehicleNumber()+"]于"+dateStr+"在"+alert.getAlertPosition()+"超速"+alert.getOverspeedPercent()+",时速"+alert.getAlertSpeed()+"KM/H";
		}
		if(alert.getAlertType().equals(AlertType.VEHICLEBACK.toString())){
			msg = "车辆["+alert.getVehicleNumber()+"]于"+dateStr+"回站报警，当前位置"+alert.getAlertPosition();
		}
		if(alert.getAlertType().equals(AlertType.OUTBOUND.toString())){
			msg = "车辆["+alert.getVehicleNumber()+"]于"+dateStr+"在"+alert.getAlertPosition()+"越界"+",越界起始位置"+alert.getAlertPosition(); 
		}
		if(alert.getAlertType().equals(AlertType.VIOLATE.toString())){
			msg = "车辆["+alert.getVehicleNumber()+"]于"+dateStr+"违规报警，当前位置"+alert.getAlertPosition();
		}
		return msg;
	}
	
	public String composeMaintenanceMileageAlertMsg(int type,String vehicleNumber, Long remainingMileage) {
		String temp = type == 0 ? "即将到期" : "已经过期";
		return "车辆" + vehicleNumber + "目前剩余保养里程为" + remainingMileage + "公里，保养" 
		+ temp + "，请及时安排车辆保养";
	}
	
	public String composeMaintenanceTimeAlertMsg(int type,String vehicleNumber, Date maintenanceDueTime) {
		String temp = type == 0 ? "即将到期" : "已经过期";
		String dateStr = DateUtils.date2String(maintenanceDueTime, "yyyy年MM月dd日");
        String nowStr = DateUtils.date2String(new Date(), "yyyy年MM月dd日");
		return "车辆" + vehicleNumber + "的下次保养日期为" + dateStr + "，当前时间是" + nowStr 
		+ temp + "，请及时安排车辆保养";
	}
	
	public String composeInsuranceAnnualInspectionMsg(String msgType, int msgModel, String vehicleNumber
				,Date alertTime) {
		String temp = msgModel == 0 ? "即将到期" : "已经过期";
		String dateStr = DateUtils.date2String(alertTime, "yyyy年MM月dd日");
		if(msgType.equals("INSURANCE")) {	//保险
			return "车辆" + vehicleNumber + "保险" + temp + "，保险到期日应为" + dateStr 
					+ "，请注意续约保险并在系统内更新保险到期日";
		} else {							//年检
			return "车辆" + vehicleNumber + "年检" + temp + "，年检日期为" + dateStr
					+ "，请注意安排年检并在系统内更新下次年检日期";
		}
	}
	
	public static String composeReleaseOutBoundAlertMsg(VehicleAlertModel alert){
		String dateStr = alert.getAlertTime()!=null?DateUtils.date2String(alert.getAlertTime(), "yyyy年MM月dd日 HH时mm分ss秒"):"<未知时间>";
		return "车辆["+alert.getVehicleNumber()+"]于"+dateStr+"在"+alert.getAlertPosition()+"回归界内"+",回归位置"+alert.getAlertPosition(); 
	}
	
	public static void main(String args[]){
		System.out.println(DateUtils.date2String(new Timestamp(new Date().getTime()), "yyyy年MM月dd日 HH时mm分ss秒"));
	}

}
