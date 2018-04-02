package com.cmdt.carday.microservice.api.portal;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformEmployeeService;
import com.cmdt.carday.microservice.biz.service.PlatformFileService;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.employee.EmployeeCreateDto;
import com.cmdt.carday.microservice.model.request.employee.EmployeeDelToDep;
import com.cmdt.carday.microservice.model.request.employee.EmployeeListUnallocatedDep;
import com.cmdt.carday.microservice.model.request.employee.EmployeeSearchDto;
import com.cmdt.carday.microservice.model.request.employee.EmployeeSetToDep;
import com.cmdt.carday.microservice.model.request.employee.EmployeeUpdateDto;
import com.cmdt.carday.microservice.storage.StorageService;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/employee")
@SwaggerDefinition(info = @Info(title = "employee API", version = "1.0.0"))
@Validated
@RestController  
@RequestMapping("/employee")
public class EmployeeApi extends BaseApi {
	
	@Autowired
	private PlatformEmployeeService employeeService;
	
	
	@Autowired
	private PlatformFileService fileService;
	
	private final StorageService storageService;
	
	@Autowired
	public EmployeeApi(StorageService storageService) {
		this.storageService = storageService;
	}
	
    /**
     * [超级管理员]查看所有员工
     * [企业管理员]查看企业下所有员工
     * [部门管理员]查看部门下所有员工
     * @return
     */
	@PostMapping("/search")
	@ApiOperation(value = "查看所有员工", response = WsResponse.class)
	public PagModel listEmployeeByOrgIdByPageInSearch(@RequestBody @Valid @NotNull EmployeeSearchDto dto){
		return employeeService.listEmployeeByOrgIdByPageInSearch(super.getUserById(dto.getUserId()), dto);
	}
	
	 /**
     * [部门管理员]查看部门下所有员工,用于部门管理员补录订单使用
     * @return
     */
	@GetMapping("/listByDept/{userId}")
	@ApiOperation(value = "查看部门下所有员工,用于部门管理员补录订单使用", response = WsResponse.class)
	public List<EmployeeModel> listByDept(
			@ApiParam(value = "用户ID", required = true) @PathVariable("userId")Long userId){
		return employeeService.listByDept(super.getUserById(userId));
	}
	 
    /**
     * [企业管理员]添加员工
     * @param username,password,roleId,organizationId,userCategory,realname,phone,email
     * @return
     */
	@PostMapping("/create")
	@ApiOperation(value = "添加员工", response = WsResponse.class)
	public User createEmployee(@RequestBody @Valid @NotNull EmployeeCreateDto dto){
		return employeeService.createEmployee(super.getUserById(dto.getUserId()),dto);
	}
	
	/**
	 * [企业管理员]根据id查询用户
	 * @param id
	 * @return
	 */
	@GetMapping("/show/{id}")
	@ApiOperation(value = "根据id查询用户", response = WsResponse.class)
	public EmployeeModel showUpdateForm(
			@ApiParam(value = "员工ID", required = true) @PathVariable("id")Long id){
		return employeeService.findEmployeeModel(id);
	}
	
	/**
	 * [企业管理员]修改用户信息
	 * @param id,username,roleId,userCategory,organizationId,realname,phone,email
	 * @return
	 */
	@PutMapping("/update")
	@ApiOperation(value = "修改用户信息", response = WsResponse.class)
	public boolean updateEmployee(@RequestBody @Valid @NotNull EmployeeUpdateDto dto){
		return employeeService.updateEmployee(super.getUserById(dto.getUserId()),dto);
	}
	
	/**
	 * [超级管理员,租户管理员,管理员]删除用户信息
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete/{id}")
	@ApiOperation(value = "删除用户信息", response = WsResponse.class)
	public boolean deleteEmployee(
			@ApiParam(value = "员工ID", required = true) @PathVariable("id")Long id){
		employeeService.deleteEmployee(id);
		return true;
	}
	
	/**
	 * excel模板下载
	 * @return
	 */
	@GetMapping("/loadTemplate")
	@ApiOperation(value = "用户导入模板下载", response = WsResponse.class)
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
		String relativePath = fileService.getTemplateFilePath("employee");
		Resource file = storageService.loadAsResource(request.getSession().getServletContext().getRealPath(relativePath));
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
				.body(file);
	}
	
	 
	/**
	 * [企业管理员]excel导入员工信息
	 * @param 
	 * @return map
	 * @throws Exception 
	 */
	@PostMapping("/import")
	@ApiOperation(value = "导入员工信息", response = WsResponse.class)
	public String importEmplyee(@RequestParam("file") MultipartFile file,
			@ApiParam(value = "用户ID", required = true) 
			@RequestParam(value = "userId", required = true) 
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId) throws Exception{
		return employeeService.importEmployeeFromFile(super.getUserById(userId), file);
	}
	
	/**
	 * [企业管理员]查看指定部门下的员工列表。
	 * [部门管理员]查看指定部门下的员工列表。
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/listByDeptId")
	@ApiOperation(value = "查看指定部门下的员工列表", response = WsResponse.class)
	public PagModel listByDeptId(@RequestBody @Valid @NotNull EmployeeSearchDto dto) throws Exception{
		return employeeService.listByDeptId(super.getUserById(dto.getUserId()), dto);
	}
	
	/**
	 * 查看所有当前企业未分配部门的员工。
	 * @return
	 * @throws Exception 
	 */
	@PostMapping("/listUnallocatedDepEmp")
	@ApiOperation(value = "查看所有当前企业未分配部门的员工", response = WsResponse.class)
	public PagModel listUnallocatedDepEmp(@RequestBody @Valid @NotNull EmployeeListUnallocatedDep dto) throws Exception{
		return employeeService.listUnallocatedDepEmp(super.getUserById(dto.getUserId()), dto);
	}
	
	/**
     * 添加员工到本部门。
     * @return
	 * @throws Exception 
     */
	@PutMapping("/setEmployeeToDep")
	@ApiOperation(value = "添加员工到本部门", response = WsResponse.class)
	public boolean setEmployeeToDep(@RequestBody @Valid @NotNull EmployeeSetToDep employeeSetToDep) throws Exception{
		employeeService.setEmployeeToDep(employeeSetToDep);
		return true;
	}
	
	/**
     * 将员工从部门中移除。
     * @return
	 * @throws Exception 
     */
	@DeleteMapping("/deleteEmployeeToDep")
	@ApiOperation(value = "将员工从部门中移除", response = WsResponse.class)
	public List<VehicleAndOrderModel> deleteEmployeeToDep(@RequestBody @Valid @NotNull EmployeeDelToDep employeeDelToDep) throws Exception{
		return employeeService.deleteEmployeeToDep(super.getUserById(employeeDelToDep.getUserId()),
				employeeDelToDep);
	}
}
