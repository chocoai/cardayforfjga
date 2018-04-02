package com.cmdt.carrental.quartz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleMaintenance;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleMaintenanceService;
import com.cmdt.carrental.common.util.DateUtils;

@Service
public class VehicleMaintenanceJobServiceImpl<T> implements VehicleMaintenanceJobService<T>{
	
	@Autowired
	private VehicleMaintenanceService vmService;
	
	@Autowired
	private MessageService mService;
	
	@Autowired
	private UserService uService;
	
	@Autowired
	private CommunicationService commService;
	
	@Autowired
	private OrganizationService oService;
	
	@Autowired
	private UserDao userDao;
	
	private List<VehicleMaintenance> queryMaintenanceMileageAlert() {
		return vmService.queryMaintenanceMileageAlert();
	}
	
	private boolean modifyDataStatus(int type,String filedName, Long id) {
		if(type == 0) {
			return vmService.modifyJobStatus(filedName, 1, id);
		}else if(type == 1) {
			return vmService.modifyJobStatus(filedName, 2, id);
		}
		return false;
	}
	
	private User findEnterAdmin(Map<String, Object> param) {
		return uService.findEnterAdmin(param);
	}
	
	private void saveMessages(List<Message> list) {
		mService.saveMessages(list);
	}
	
	private void sendPush(List<String> mobile,String title,String content) {
		//commService.sendPush(mobile, title, content);
	}
	
	private List<VehicleMaintenance> queryMaintenanceTimeAlert() {
		return vmService.queryMaintenanceTimeAlert();
	}
	
	public void excMaintenanceMileageAlert() {
		List<VehicleMaintenance> list= queryMaintenanceMileageAlert();
		List<Message> mList = new ArrayList<Message>();
		Message m;
		if(list != null) {
			for(VehicleMaintenance vehicleMaintenance : list) {
				m = new Message();
				m.setType(MessageType.MAINTAIN);
				m.setCarNo(vehicleMaintenance.getVehicleNumber());
				m.setTime(new Date());
				m.setIsNew(0);
				m.setIsEnd(1);
//				m.setOrgId(vehicleMaintenance.getOwnerOrgId());
				String msg = "";
				//拼接内容
				msg = makeMaintenanceMileageAlertMsg(vehicleMaintenance.getCurTimeWarn(), vehicleMaintenance.getVehicleNumber(), vehicleMaintenance.getRemainingMileage());
				m.setMsg(msg);
				//获取消息中心接口的orgId
				Long messageOrgId = getMessageOrgId(vehicleMaintenance);
				if(messageOrgId == 0L) {
					continue;
				} else {
					m.setOrgId(messageOrgId);
				}
				
				//获取接收者手机号
				List<String> mobileList = getMobileList(messageOrgId);
//				System.out.println("mobileList:" + mobileList);
			//	sendPush(mobileList, "车辆保养提醒", "msg");
				String result = commService.sendPush(mobileList, "车辆保养提醒", msg, Constants.CARDAY_ADMIN);
				//更新推送状态
				modifyDataStatus(vehicleMaintenance.getCurTimeWarn(), "alert_mileage_warn" , vehicleMaintenance.getVehicleId());
				mList.add(m);
			}
			saveMessages(mList);
		}
	}
	
//	private List<String> getMobileList(VehicleMaintenance vehicleMaintenance) {
//		List<String> resultList = new ArrayList<String>();
//		Long ownerEntId = vehicleMaintenance.getOwnerEntId();
//		Map<String, Object> param = new HashMap<String, Object>();
//		//添加车辆来源企业管理员手机号
//		param.put("entId", vehicleMaintenance.getOwnerEntId());
//		User user= findEnterAdmin(param);
//		if(user != null) {
//			resultList.add(user.getPhone());
//		}
//		//添加部门管理员手机号
//		Organization org = oService.findById(vehicleMaintenance.getOwnerEntId());
//		String enterprisesType = org.getEnterprisesType();
//		if(enterprisesType.equals("1")) { // 用车企业
//			if(!(vehicleMaintenance.getOwnerOrgId() ==null || vehicleMaintenance.getOwnerOrgId() == 0L)) {//已分配
//				Long ownerOrgId = vehicleMaintenance.getOwnerOrgId();
//				List<User> userList = uService.queryUserListByOrgId(ownerOrgId);
//				for(User userModel : userList) {
//					resultList.add(userModel.getPhone());
//				}
//			}
//		}
//		return resultList;
//	}
	
	/**
	 * 车辆来源
	 * 0、租车企业    企业管理员
	 * 1、用车企业    未分配：企业管理员      已分配：企业管理员/部门管理员
	 */
	private List<String> getMobileList(Long ownerEntId) {
		List<Organization> orgList = oService.findUpOrganizationListByOrgId(ownerEntId);
		List<Long> orgIdList = new ArrayList<>();
		for(Organization org : orgList){
			orgIdList.add(org.getId());
		}	
		List<UserModel> admins = userDao.listByOrgId(orgIdList);  //查找所有的上级部门管理员
		List<String> mobiles = new ArrayList<>();
		if(!admins.isEmpty()){
			for(UserModel admin : admins){
				mobiles.add(admin.getPhone());
			}
		}
		return mobiles;
	}
	
	private Long getMessageOrgId(VehicleMaintenance vehicleMaintenance) {
		if(vehicleMaintenance.getOwnerOrgId() == null || vehicleMaintenance.getOwnerOrgId()== 0L) {
			return vehicleMaintenance.getOwnerEntId();
		}else{
			return vehicleMaintenance.getOwnerOrgId();
		}
		
		/*//判断车辆来源
		param.put("entId", vehicleMaintenance.getOwnerEntId());
		User user= findEnterAdmin(param);
		Organization org = oService.findById(vehicleMaintenance.getOwnerEntId());
		String enterprisesType = org.getEnterprisesType();
		if(enterprisesType.equals("1")) {// 用车企业
			if(!(vehicleMaintenance.getOwnerOrgId() ==null || vehicleMaintenance.getOwnerOrgId() == 0L)) {	//已分配部门
				return vehicleMaintenance.getOwnerOrgId();
			}
		}
		return vehicleMaintenance.getOwnerEntId();*/
	}
	
	private String makeMaintenanceMileageAlertMsg(int type,String vehicleNumber, Long remainingMileage) {
		String temp = type == 0 ? "即将到期" : "已经过期";
		return "车辆" + vehicleNumber + "目前剩余保养里程为" + remainingMileage + "公里，保养" 
		+ temp + "，请及时安排车辆保养";
	}
	
	public void excMaintenanceTimeAlert() {
		List<VehicleMaintenance> list= queryMaintenanceTimeAlert();
		List<Message> mList = new ArrayList<>();
		Message m;
		if(list != null) {
			for(VehicleMaintenance vehicleMaintenance : list) {
				m = new Message();
				m.setType(MessageType.MAINTAIN);
				m.setCarNo(vehicleMaintenance.getVehicleNumber());
				m.setTime(new Date());
				m.setIsNew(0);
				m.setIsEnd(1);
				m.setOrgId(vehicleMaintenance.getOwnerOrgId());
				String msg = "";
				msg = makeMaintenanceTimeAlertMsg(vehicleMaintenance.getAlertMileageWarn(), vehicleMaintenance.getVehicleNumber(), vehicleMaintenance.getMaintenanceDueTime());
				m.setMsg(msg);
				//获取消息中心接口的orgId
				Long messageOrgId = getMessageOrgId(vehicleMaintenance);
				if(messageOrgId == 0L) {
					continue;
				} else {
					m.setOrgId(messageOrgId);
				}
				
				//获取接收者手机号
				List<String> mobileList = getMobileList(messageOrgId);
//				System.out.println("mobileList:" + mobileList);
				//sendPush(mobileList, "车辆保养提醒", "msg");
				String result = commService.sendPush(mobileList, "车辆保养提醒", msg, Constants.CARDAY_ADMIN);
				modifyDataStatus(vehicleMaintenance.getAlertMileageWarn(), "cur_time_warn" , vehicleMaintenance.getVehicleId());
				mList.add(m);
			}
			saveMessages(mList);
		}
	}
	
	private String makeMaintenanceTimeAlertMsg(int type,String vehicleNumber, Date maintenanceDueTime) {
		String temp = type == 0 ? "即将到期" : "已经过期";
		String dateStr = DateUtils.date2String(maintenanceDueTime, "yyyy年MM月dd日");
        String nowStr = DateUtils.date2String(new Date(), "yyyy年MM月dd日");
		return "车辆" + vehicleNumber + "的下次保养日期为" + dateStr + "，当前时间是" + nowStr 
		+ temp + "，请及时安排车辆保养";
	}

}
