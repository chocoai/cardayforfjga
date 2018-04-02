package com.cmdt.carrental.mobile.gateway.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.service.OrganizationService;
import com.cmdt.carrental.common.service.UserServiceImpl;
import com.cmdt.carrental.mobile.gateway.common.Constants;
import com.cmdt.carrental.mobile.gateway.common.WsResponse;
import com.cmdt.carrental.mobile.gateway.model.request.department.DepartmentDto;
import com.cmdt.carrental.mobile.gateway.model.response.department.DepartmentModel;
import com.cmdt.carrental.mobile.gateway.model.response.department.DepartmentRetModel;

@Service
public class DepartmentAppService {
	
	@Autowired
	private UserServiceImpl userService;

	@Autowired
    private OrganizationService organizationService;
	
	public WsResponse showDepartmentList(Long userId) {
		WsResponse<List<DepartmentModel>> wsResponse = new WsResponse<List<DepartmentModel>>();
		User loginUser = userService.findById(userId);
		List<DepartmentModel> resultList = new ArrayList<DepartmentModel>();
		if(loginUser != null) {
			//企业管理员
			List<Organization> list = new ArrayList<Organization>();
	  		if(loginUser.isEntAdmin()){
	  			list = organizationService.findByOrganizationId(loginUser.getOrganizationId());
	  		}
	  		//过滤企业信息
	  		for(int i=0;i<list.size();i++) {
	  			Organization organization = list.get(i);
	  			if(loginUser.getOrganizationId().equals(organization.getId())) {
	  				list.remove(i);
	  			}
	  		}
	  		if(list.size()>0) {
	  			resultList = departmentListTransform(list);
	  		}
	  		
	  		wsResponse.getMessages().add(Constants.MESSAGE_SUCCESS);
	  		wsResponse.setStatus(Constants.STATUS_SUCCESS);
	  		wsResponse.setResult(resultList);
		}else {
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add("该用户不存在");
		}
		return wsResponse;
	}
	
	private List<DepartmentModel> departmentListTransform(List<Organization> list) {
		List<DepartmentModel> resultList = new ArrayList<DepartmentModel>();
		DepartmentModel departmentModel;
		
		for(Organization org : list) {
			departmentModel = new DepartmentModel();
			departmentModel.setId(org.getId());
			departmentModel.setDepartment(org.getName());
			resultList.add(departmentModel);
		}
		return resultList;
	}
	
	
	public List<DepartmentRetModel> queryDepatmentList(DepartmentDto dto){
		
		List<Organization> orgList = organizationService.findDownOrganizationListByOrgId(dto.getOrgId(), dto.getStartDepth(), dto.getEndDepth());
		
		List<DepartmentRetModel> departList = new ArrayList<>();
		
		for(Organization org : orgList){
			departList.add(new DepartmentRetModel(org.getDepth(),org.getParentId(),org.getId(),org.getName()));
		}
		
		return departList;
		
	}
	
}
