package com.cmdt.carrental.common.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cmdt.carrental.common.integration.model.ActionName;
import com.cmdt.carrental.common.model.BusiOrderDto;
import com.cmdt.carrental.common.model.BusiOrderQueryDto;
import com.cmdt.carrental.common.util.HttpClientUtil;
import com.cmdt.carrental.common.util.JsonUtils;

@Component
public class CarDayService {
	private static final Logger LOG = LoggerFactory.getLogger(CarDayService.class);
	
	//sys resource
	@Value("${service.resourceUrl}")
	private String resourceUrl;
	
	//busi order
	@Value("${service.busiOrderUrl}")
	private String busiOrderUrl;
	
	public Object action(ActionName actionName, Object[] params){
		if(actionName.equals(ActionName.ORDERLISTFORPORTAL)){
			return orderListForPortal((BusiOrderQueryDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERAUDITLIST)){
			return orderAuditList((BusiOrderQueryDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERALLOCATELIST)){
			return orderAllocateList((BusiOrderQueryDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERCREATE)){
			return orderCreate((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERUPDATE)){
			return orderUpdate((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERRECREATE)){
			return orderReCreate((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERRECREATEUPDATE)){
			return orderReCreateUpdate((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERVIEW)){
			return orderView((Long)params[0],(Long)params[1]);
		}else if(actionName.equals(ActionName.ORDERAUDIT)){
			return orderAudit((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERAUDITSENDMSG)){
			return orderAuditSendMsg((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERAUDITHISTORY)){
			return orderAuditHistory((Long)params[0],(Long)params[1]);
		}else if(actionName.equals(ActionName.ORDERAVAILABLEDRIVERS)){
			return orderAvailableDrivers((BusiOrderQueryDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERSCHEDULE)){
			return orderSchedule((Long)params[0],(Long)params[1]);
		}else if(actionName.equals(ActionName.ORDERALLOCATE)){
			return orderAllocate((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERDELETE)){
			return orderDelete((Long)params[0],(Long)params[1]);
		}else if(actionName.equals(ActionName.ORDERCANCEL)){
			return orderCancel((BusiOrderDto)params[0]);
		}else if(actionName.equals(ActionName.ORDERVEHICLESCHEDULE)){
			return orderVehicleSchedule((Long)params[0],(String)params[1]);
		}
		return null;
	}
	
	private String orderListForPortal(BusiOrderQueryDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/admin/list", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderListForPortal failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAuditList(BusiOrderQueryDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/auditList", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAuditList failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAllocateList(BusiOrderQueryDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/allocateList", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAllocateList failed, error :\n",e);
			return null;
		}
	}
	
	private String orderCreate(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/create", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderCreate failed, error :\n",e);
			return null;
		}
	}
	
	private String orderUpdate(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/update", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderUpdate failed, error :\n",e);
			return null;
		}
	}
	
	private String orderReCreate(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/recreate", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderReCreate failed, error :\n",e);
			return null;
		}
	}
	
	private String orderReCreateUpdate(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/recreateUpdate", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderReCreateUpdate failed, error :\n",e);
			return null;
		}
	}
	
	private String orderView(Long userId,Long orderId) {
		try {
			return HttpClientUtil.executeGet(busiOrderUrl + "/"+userId+"/"+orderId+"/view");
		} catch (Exception e) {
			LOG.error("CarDayService.orderView failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAudit(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/audit", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAudit failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAuditSendMsg(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/auditSendMsg", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAuditSendMsg failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAuditHistory(Long userId,Long orderId) {
		try {
			return HttpClientUtil.executeGet(busiOrderUrl + "/auditHistory/"+userId+"/"+orderId);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAuditHistory failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAvailableDrivers(BusiOrderQueryDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/availableDrivers", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAvailableDrivers failed, error :\n",e);
			return null;
		}
	}
	
	private String orderSchedule(Long vehicleId,Long orderId) {
		try {
			return HttpClientUtil.executeGet(busiOrderUrl + "/schedule/"+vehicleId+"/"+orderId);
		} catch (Exception e) {
			LOG.error("CarDayService.orderSchedule failed, error :\n",e);
			return null;
		}
	}
	
	private String orderAllocate(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/allocate", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderAllocate failed, error :\n",e);
			return null;
		}
	}
	
	private String orderDelete(Long userId,Long orderId) {
		try {
			return HttpClientUtil.executeGet(busiOrderUrl + "/delete/"+userId+"/"+orderId);
		} catch (Exception e) {
			LOG.error("CarDayService.orderDelete failed, error :\n",e);
			return null;
		}
	}
	
	private String orderCancel(BusiOrderDto dto) {
		try {
			String json=JsonUtils.object2Json(dto);
			return HttpClientUtil.sendJsonPost(busiOrderUrl + "/cancel", json);
		} catch (Exception e) {
			LOG.error("CarDayService.orderCancel failed, error :\n",e);
			return null;
		}
	}
	
	private String orderVehicleSchedule(Long vehicleId,String planStTimeF) {
		try {
			return HttpClientUtil.executeGet(busiOrderUrl + "/vehicleSchedule/"+vehicleId+"/"+planStTimeF);
		} catch (Exception e) {
			LOG.error("CarDayService.orderVehicleSchedule failed, error :\n",e);
			return null;
		}
	}
}
