package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.OrgAndParentOrgByOrgIdModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.TreeNode;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformDepartmentService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentCreateDto;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentCreditModel;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentDeleteDto;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentPageDto;
import com.cmdt.carrental.platform.service.model.request.department.DepartmentUpdateDto;

@Produces(MediaType.APPLICATION_JSON)
public class DepartmentApi extends BaseApi{
	
private static final Logger LOG = LoggerFactory.getLogger(DepartmentApi.class);
	
    @Autowired
    private PlatformDepartmentService organizationService;
	
	/**
	 * [企业管理员] 查询企业下面所有部门以及子部门
	 * 
	 */
	@GET
	@Path("/list/{userId}")
	public List<Organization> showList(@PathParam("userId")Long userId) {
		try {
			return organizationService.showList(getUserById(userId));
		} catch (Exception e) {
			LOG.error("DepartmentController.showList", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * [超级管理员] 查询所有企业以树形json返回 [企业管理员] 查询自己所在企业的部门以树形json返回
	 * 
	 */
	@GET
	@Path("/tree/{userId}")
	public List<TreeNode> showTree(@PathParam("userId")Long userId) {
		try {
			return organizationService.showTree(getUserById(userId));
		}catch(ServerException e){
			throw e;
		} catch (Exception e) {
			LOG.error("DepartmentController.showTree failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * [超级管理员] 查询指定企业以树形json返回 [企业管理员] 查询指定企业的部门以树形json返回
	 * 
	 */
	@GET
	@Path("/treeByOrgId/{orgId}")
	public List<TreeNode> showTreeByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return organizationService.showTreeByOrgId(orgId);
		} catch (Exception e) {
			LOG.error("DepartmentController.showTreeByOrgId failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 根据组织机构Id查询组织信息 CR-1995
	 * 
	 */
	@GET
	@Path("/findOrganizationByOrgId/{orgId}")
	public OrgAndParentOrgByOrgIdModel findOrganizationByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return organizationService.findOrganizationByOrgId(orgId);
		} catch (Exception e) {
			LOG.error("DepartmentController.findOrganizationByOrgId failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 添加部门前的预操作
	 * 
	 */
	@GET
	@Path("/appendChild/show/{parentId}")
	public OrgAndParentOrgByOrgIdModel appendChildShowOrganizationByParentId(@PathParam("parentId") Long parentId) {
		try {
			return organizationService.appendChildShowOrganizationByParentId(parentId);
		} catch (Exception e) {
			LOG.error("DepartmentController.appendChildShowOrganizationByParentId failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 添加部门
	 * 
	 */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)  
	public String createDepartment(@Valid @NotNull DepartmentCreateDto departmentDto){
		try{
			organizationService.create(departmentDto);
			return Constants.API_MESSAGE_SUCCESS;
		}catch(ServerException e){
			throw e;
		}catch(Exception e){
			LOG.error("DepartmentApi createDepartment error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 更新部门
	 * 
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)  
	public String updateOrganization(@Valid @NotNull DepartmentUpdateDto dto) {
		try {
			organizationService.updateOrganization(dto);
			return Constants.API_MESSAGE_SUCCESS;
		}catch(ServerException e){
			throw e;
		} catch (Exception e) {
			LOG.error("department updateOrganization failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 删除部门
	 * 
	 */
	@POST
	@Path("/delete")
	@Consumes(MediaType.APPLICATION_JSON)  
	public String deleteDepartment(@Valid @NotNull DepartmentDeleteDto departmentDto){
		try{
			organizationService.deleteOrganization(departmentDto.getId());
			return Constants.API_MESSAGE_SUCCESS;
		}catch(ServerException e){
			throw e;
		}catch(Exception e){
			LOG.error("DepartmentApi deleteDepartment by parentId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * [企业管理员] 查询下级所有部门，并显示该部门的员工数量(管理员，员工)不包含司机
	 * 
	 */
	@POST
	@Path("/listDirectChildrenWithCount")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel listDirectChildrenWithCount(@Valid @NotNull DepartmentPageDto departmentDto){
		try{
			return organizationService.listDirectChildrenWithCount(getUserById(departmentDto.getUserId()),departmentDto);
		}catch(Exception e){
			LOG.error("DepartmentApi listDirectChildrenWithCount error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	@GET
	@Path("/findTreeCreditByOrgId/{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)  
	public List<TreeNode> findTreeCreditByOrgId(@PathParam("orgId") Long orgId) {
		try {
			return organizationService.findTreeCreditByOrgId(orgId);
		} catch (Exception e) {
			LOG.error("DepartmentController.findTreeCreditByOrgId failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	@POST
	@Path("/updateCredit")
	@Consumes(MediaType.APPLICATION_JSON)  
	public String updateCredit(@Valid @NotNull List<DepartmentCreditModel> modelList) {
		try {
			organizationService.updateCredit(modelList);
			return Constants.API_MESSAGE_SUCCESS;
		} catch (Exception e) {
			LOG.error("DepartmentController.updateCredit failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	@GET
	@Path("/findOrgById/{orgId}")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Organization findOrgById(@PathParam("orgId") Long orgId) {
		try {
			return organizationService.findOrgById(orgId);
		} catch (Exception e) {
			LOG.error("DepartmentController.findOrgById failed, caused by:", e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

}
