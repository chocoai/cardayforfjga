package com.cmdt.carrental.quartz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.constants.Constants;
import com.cmdt.carrental.common.dao.UserDao;
import com.cmdt.carrental.common.entity.Message;
import com.cmdt.carrental.common.entity.Message.MessageType;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.integration.CommunicationService;
import com.cmdt.carrental.common.model.UserModel;
import com.cmdt.carrental.common.model.VehicleAnnualInspection;
import com.cmdt.carrental.common.service.MessageService;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleAnnualInspectionService;
import com.cmdt.carrental.common.util.MessageTemplateUtil;

@Service
public class VehicleAnnualInspectionJobServiceImpl<T> implements VehicleAnnualInspectionJobService<T>{
	
	private static final Logger LOG = LoggerFactory.getLogger(VehicleAnnualInspectionJobServiceImpl.class);

	@Autowired
	VehicleAnnualInspectionService vaService;
	
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
	
	public void excInsuranceTimeAlert() {
		List<VehicleAnnualInspection> list = vaService.queryInsuranceTimeAlert();
		List<Message> mList = new ArrayList<Message>();
		Message m;
		if(list != null) {
			for(VehicleAnnualInspection aInspection : list) {
				m = new Message();
				m.setType(MessageType.MAINTAIN);
				m.setCarNo(aInspection.getVehicleNumber());
				m.setTime(new Date());
				m.setIsNew(0);
				m.setIsEnd(1);
				String msg = "";
				MessageTemplateUtil mUtil = new MessageTemplateUtil();
				msg = mUtil.composeInsuranceAnnualInspectionMsg("INSURANCE", aInspection.getInspectionFlag(),
						aInspection.getVehicleNumber(), aInspection.getInsuranceDueTime());
				m.setMsg(msg);
				Long messageOrgId = getMessageOrgId(aInspection.getOwnerEntId(), aInspection.getOwnerOrgId());
				if(messageOrgId == 0L) {
					continue;
				} else {
					m.setOrgId(messageOrgId);
				}
				
				//获取接收者MOBILE集合
				List<String> mobileList = getMobileList(messageOrgId);
				String result = commService.sendPush(mobileList, "车辆保险到期提醒", msg, Constants.CARDAY_ADMIN);
				LOG.error("车牌号" + aInspection.getVehicleNumber() + "JPUSH----result-->" + result);
				modifyDataStatus(aInspection.getInspectionFlag(), "insurance_flag", aInspection.getId());
				
				mList.add(m);
			}
			mService.saveMessages(mList);
		}
	}
	
	public void excInspectionTimeAlert() {
		List<VehicleAnnualInspection> list = vaService.queryInspectionTimeAlert();
		List<Message> mList = new ArrayList<Message>();
		Message m;
		if(list != null) {
			for(VehicleAnnualInspection aInspection : list) {
				m = new Message();
				m.setType(MessageType.MAINTAIN);
				m.setCarNo(aInspection.getVehicleNumber());
				m.setTime(new Date());
				m.setIsNew(0);
				m.setIsEnd(1);
				String msg = "";
				MessageTemplateUtil mUtil = new MessageTemplateUtil();
				msg = mUtil.composeInsuranceAnnualInspectionMsg("INSPECTION", aInspection.getInsuranceFlag(),
						aInspection.getVehicleNumber(), aInspection.getInspectionNextTime());
				m.setMsg(msg);
				Long messageOrgId = getMessageOrgId(aInspection.getOwnerEntId(), aInspection.getOwnerOrgId());
				if(messageOrgId == 0L) {
					continue;
				} else {
					m.setOrgId(messageOrgId);
				}
				
				//获取接收者MOBILE集合
				List<String> mobileList = getMobileList(messageOrgId);
				String result = commService.sendPush(mobileList, "车辆年检到期提醒", msg, Constants.CARDAY_ADMIN);
				LOG.error("车牌号" + aInspection.getVehicleNumber() + "JPUSH----result-->" + result);
				modifyDataStatus(aInspection.getInsuranceFlag(), "inspection_flag", aInspection.getId());
				
				mList.add(m);
			}
			mService.saveMessages(mList);
		}
	}
	
	private Long getMessageOrgId(Long ownerEntId, Long ownerOrgId) {
		if(ownerOrgId == null ||  ownerOrgId == 0L) {
			return ownerEntId;
		}
		return ownerOrgId;
		/*//判断车辆来源
		Organization org = oService.findById(ownerEntId);
		String enterprisesType = org.getEnterprisesType();
		if(enterprisesType.equals("1")) {// 用车企业
			if(!(ownerOrgId == null ||  ownerOrgId == 0L)) {
				return ownerOrgId;
			}
		}
		return ownerEntId;*/
	}
	
	public boolean modifyDataStatus(int type,String filedName, Long id) {
		if(type == 0) {
			return vaService.modifyJobStatus(filedName, 1, id);
		}else if(type == 1) {
			return vaService.modifyJobStatus(filedName, 2, id);
		}
		return false;
	}
	
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
}
