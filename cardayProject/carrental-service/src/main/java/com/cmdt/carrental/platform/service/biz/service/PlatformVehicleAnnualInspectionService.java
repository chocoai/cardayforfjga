package com.cmdt.carrental.platform.service.biz.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.dao.VehicleAnnualInspectionDao;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.entity.VehicleAnnualInspectionDto;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAnnualInspection;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserService;
import com.cmdt.carrental.common.service.VehicleAnnualInspectionService;
import com.cmdt.carrental.common.util.TimeUtils;
import com.cmdt.carrental.platform.service.model.request.annual.AnnualInspectionPortalDto;
import com.cmdt.carrental.platform.service.model.request.annual.ResetInspectionDto;
import com.cmdt.carrental.platform.service.model.request.annual.ResetInsuranceDto;

@Service
public class PlatformVehicleAnnualInspectionService {

	@Autowired
	private VehicleAnnualInspectionDao vehicleAnnualInspectionDao;
	
	@Autowired
	private VehicleAnnualInspectionService annualInspectionService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private UserService userService;
	
	public PagModel listPage(User user,AnnualInspectionPortalDto model) throws Exception {
		VehicleAnnualInspectionDto dto=new VehicleAnnualInspectionDto();
		BeanUtils.copyProperties(dto, model);
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
	
	
	public boolean  resetInsuranceTime(User user,ResetInsuranceDto dto){
		Long id = dto.getVehicleId();
		boolean status = false;
		String insuranceDueTime = dto.getInsuranceDueTime();
		// 企业管理员
		if (user.isEntAdmin()) {
			status = annualInspectionService.resetInsuranceTime(id, insuranceDueTime);
		}
        
		return status;
	}
	
	public boolean  resetInspectionTime(User user,ResetInspectionDto dto){
		Long id = dto.getId();
		boolean status = false;
		String inspectionNextTime = dto.getInspectionNextTime();
		// 企业管理员
		if (user.isEntAdmin()) {
			status = annualInspectionService.resetInspectionTime(id, inspectionNextTime);
		}
        
		return status;
	}
	
}
