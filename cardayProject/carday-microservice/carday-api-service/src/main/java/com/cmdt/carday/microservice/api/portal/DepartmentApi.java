package com.cmdt.carday.microservice.api.portal;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformDepartmentService;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.department.*;
import com.cmdt.carrental.common.entity.Organization;
import com.cmdt.carrental.common.model.OrgAndParentOrgByOrgIdModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.util.TreeNode;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api("/department")
@SwaggerDefinition(info = @Info(title = "department API", version = "1.0.0"))
@Validated
@RestController  
@RequestMapping("/department")
public class DepartmentApi extends BaseApi{

    @Autowired
    private PlatformDepartmentService organizationService;

	/**
	 * [企业管理员] 查询企业下面所有部门以及子部门
	 *
	 */
    @ApiOperation(value = "查询企业下面所有部门以及子部门", response = WsResponse.class)
    @GetMapping("/list/{userId}")
	public List<Organization> showList(@ApiParam(value = "用户ID", required = true)
		@PathVariable("userId") Long userId) {
    	return organizationService.showList(getUserById(userId));
	}

	/**
	 * [超级管理员] 查询所有企业以树形json返回 [企业管理员] 查询自己所在企业的部门以树形json返回
	 *
	 */
    @ApiOperation(value = "查询所有企业以树形json返回 [企业管理员] 查询自己所在企业的部门以树形json返回", response = WsResponse.class)
    @GetMapping("/tree/{userId}")
	public List<TreeNode> showTree(@ApiParam(value = "用户ID", required = true) 
		@PathVariable("userId")Long userId) {
    	return organizationService.showTree(getUserById(userId));
	}

	/**
	 * [超级管理员] 查询指定企业以树形json返回 [企业管理员] 查询指定企业的部门以树形json返回
	 *
	 */
    @ApiOperation(value = "查询指定企业以树形json返回 [企业管理员] 查询指定企业的部门以树形json返回", response = WsResponse.class)
    @GetMapping("/treeByOrgId/{orgId}")
	public List<TreeNode> showTreeByOrgId(@ApiParam(value = "组织ID", required = true)
			@PathVariable("orgId") Long orgId) {
    	return organizationService.showTreeByOrgId(orgId);
	}

	/**
	 * 根据组织机构Id查询组织信息 CR-1995
	 *
	 */
    @ApiOperation(value = "根据组织机构Id查询组织信息", response = WsResponse.class)
    @GetMapping("/findOrganizationByOrgId/{orgId}")
	public OrgAndParentOrgByOrgIdModel findOrganizationByOrgId(@ApiParam(value = "组织ID", required = true)
			@PathVariable("orgId") Long orgId) {
    	return organizationService.findOrganizationByOrgId(orgId);
	}

	/**
	 * 添加部门前的预操作
	 *
	 */
    @ApiOperation(value = "添加部门前的预操作", response = WsResponse.class)
    @GetMapping("/appendChild/show/{parentId}")
	public OrgAndParentOrgByOrgIdModel appendChildShowOrganizationByParentId(@ApiParam(value = "父机构ID", required = true)
			@PathVariable("parentId") Long parentId) {
    	return organizationService.appendChildShowOrganizationByParentId(parentId);
	}

	/**
	 * 添加部门
	 * @throws Exception 
	 *
	 */
    @ApiOperation(value = "添加部门", response = WsResponse.class)
    @PostMapping("/create")
	public Organization createDepartment(@RequestBody @Valid @NotNull DepartmentCreateDto departmentDto) throws Exception{
    	return organizationService.create(departmentDto);
	}

	/**
	 * 更新部门
	 * @throws Exception 
	 *
	 */
    @ApiOperation(value = "更新部门", response = WsResponse.class)
    @PutMapping("/update")
	public boolean updateOrganization(@RequestBody @Valid @NotNull DepartmentUpdateDto dto) throws Exception {
    	organizationService.updateOrganization(dto);
    	return true;
	}

	/**
	 * 删除部门
	 *
	 */
    @ApiOperation(value = "删除部门", response = WsResponse.class)
    @DeleteMapping("/delete")
	public boolean deleteDepartment(@RequestBody @Valid @NotNull DepartmentDeleteDto departmentDto){
    	organizationService.deleteOrganization(departmentDto.getId());
    	return true;
	}

	/**
	 * [企业管理员] 查询下级所有部门，并显示该部门的员工数量(管理员，员工)不包含司机
	 *
	 */
    @ApiOperation(value = "查询下级所有部门，并显示该部门的员工数量(管理员，员工)不包含司机", response = WsResponse.class)
    @PostMapping("/listDirectChildrenWithCount")
	public PagModel listDirectChildrenWithCount(@RequestBody @Valid @NotNull DepartmentPageDto departmentDto){
    	return organizationService.listDirectChildrenWithCount(getUserById(departmentDto.getUserId()),departmentDto);
	}

    @ApiOperation(value = "根据orgId查找额度树", response = WsResponse.class)
    @GetMapping("/findTreeCreditByOrgId/{orgId}")
	public List<TreeNode> findTreeCreditByOrgId(@ApiParam(value = "组织ID", required = true)
		@PathVariable("orgId") Long orgId) {
    	return organizationService.findTreeCreditByOrgId(orgId);
	}

    @ApiOperation(value = "修改额度", response = WsResponse.class)
    @PutMapping("/updateCredit")
	public boolean updateCredit(@RequestBody @Valid @NotNull List<DepartmentCreditModel> modelList) {
		organizationService.updateCredit(modelList);
		return true;
	}

    @ApiOperation(value = "根据orgId查找部门", response = WsResponse.class)
    @GetMapping("/findOrgById/{orgId}")
	public Organization findOrgById(@ApiParam(value = "组织ID", required = true)
			@PathVariable("orgId") Long orgId) {
		return organizationService.findOrgById(orgId);
	}

}
