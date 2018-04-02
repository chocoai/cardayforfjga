package com.cmdt.carrental.common.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.VehicleAnnualInspectionDao;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.VehicleAnnualInspectionDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAnnualInspection;
import com.cmdt.carrental.common.util.JsonUtils;
import com.cmdt.carrental.common.util.TimeUtils;

@Service
public class VehicleAnnualInspectionServiceImpl implements VehicleAnnualInspectionService{

	@Autowired
	private VehicleAnnualInspectionDao vehicleAnnualInspectionDao;
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public PagModel listPage(Long entId, String json) {
//		PagModel pagModel = vehicleAnnualInspectionDao.listPage(entId, json);
		VehicleAnnualInspectionDto dto = JsonUtils.json2Object(json, VehicleAnnualInspectionDto.class);
		// 画面选择的部门ID
    	Long orgId = dto.getDeptId();
    	// 本部门范围
		Boolean selfDept = dto.getSelfDept();
		// 子部门范围
		Boolean childDept = dto.getChildDept();
		// 根据部门ID 获得关联的全部部门ID
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(orgId, selfDept, childDept);
		List<Long> orgIds = new ArrayList<>();
		for(Organization org : orgList){
			orgIds.add(org.getId());
		}
		// 设置全部部门ID
		dto.setDeptIdList(orgIds);
		
		if (null != orgList && orgList.isEmpty()) {
			return null;
		}
		
		PagModel pagModel = vehicleAnnualInspectionDao.listPage(dto);
		List<VehicleAnnualInspection> list = pagModel.getResultList();
		for(VehicleAnnualInspection annualInspection : list) {
			annualInspection.setInsuranceDueTimeFlag(false);
			annualInspection.setInspectionTimeFlag(false);
			Date insuranceDueTime = annualInspection.getInsuranceDueTime();
			int months = TimeUtils.calDiffMonth(new Date(), insuranceDueTime);
			if(months <= 1) {
				annualInspection.setInsuranceDueTimeFlag(true);//报警
			}
			Date inspectionNextTime = annualInspection.getInspectionNextTime();
			months = TimeUtils.calDiffMonth(new Date(), inspectionNextTime);
			if(months <= 1) {
				annualInspection.setInspectionTimeFlag(true);//报警
			}
		}
		return pagModel;
	}

	@Override
	public boolean resetInsuranceTime(Long id, String insuranceDueTime) {
		return vehicleAnnualInspectionDao.resetInsuranceTime(id, insuranceDueTime);
	}

	@Override
	public boolean resetInspectionTime(Long id, String inspectionNextTime) {
		return vehicleAnnualInspectionDao.resetInspectionTime(id, inspectionNextTime);
	}
	
	@Override
	public List<VehicleAnnualInspection> queryInsuranceTimeAlert() {
		return vehicleAnnualInspectionDao.queryInsuranceTimeAlert();
	}
			
	@Override
	public List<VehicleAnnualInspection> queryInspectionTimeAlert() {
		return vehicleAnnualInspectionDao.queryInspectionTimeAlert();
	}
	
	@Override
	public boolean modifyJobStatus(String filedName, int filedValue, Long id) {
		return vehicleAnnualInspectionDao.modifyJobStatus(filedName, filedValue, id);
	}

	@Override
	public int updateInspection(java.sql.Date date, Long vehicleId) {
		return vehicleAnnualInspectionDao.updateInspection(date, vehicleId);
	}

}
