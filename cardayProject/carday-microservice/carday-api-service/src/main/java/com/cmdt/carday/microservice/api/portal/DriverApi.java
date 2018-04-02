package com.cmdt.carday.microservice.api.portal;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.cmdt.carday.microservice.biz.service.PlatformDriverService;
import com.cmdt.carday.microservice.biz.service.PlatformFileService;
import com.cmdt.carday.microservice.common.model.response.WsResponse;
import com.cmdt.carday.microservice.model.request.driver.DriverCreateDto;
import com.cmdt.carday.microservice.model.request.driver.DriverDelToDep;
import com.cmdt.carday.microservice.model.request.driver.DriverListByUnallocatedDep;
import com.cmdt.carday.microservice.model.request.driver.DriverListDto;
import com.cmdt.carday.microservice.model.request.driver.DriverSetToDep;
import com.cmdt.carday.microservice.model.request.driver.DriverUpdateDto;
import com.cmdt.carday.microservice.storage.StorageService;
import com.cmdt.carrental.common.entity.User;
import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;


@Api("/driver")
@SwaggerDefinition(info = @Info(title = "driver API", version = "1.0.0"))
@Validated
@RestController  
@RequestMapping("/driver")
public class DriverApi extends BaseApi{
	
	@Value("${service.imgeUrl}")
    private String imgUrl;
	
	@Autowired
    private PlatformDriverService driverService;
	
	
	private final StorageService storageService;
	
	@Autowired
	private PlatformFileService fileService;
	
	@Autowired
	public DriverApi(StorageService storageService) {
		this.storageService = storageService;
	}
	
	 /**
     * [超级管理员]查看所有司机
     * [企业管理员]查看企业下面所有司机
     * [部门管理员]查看部门下面所有司机
     * @return
	 * @throws Exception 
     */
	@ApiOperation(value = "查看所有司机", response = WsResponse.class)
    @PostMapping("/list")
	public PagModel listDriver(@RequestBody @Valid @NotNull DriverListDto driverDto) throws Exception{
		return driverService.findDriverPageByUser(getUserById(driverDto.getUserId()), driverDto);
	}
	
	 /**
     * [部门管理员]订单补录，查询当前订单用户所在部门下所有的司机
     * @return
     */
	@ApiOperation(value = "订单补录，查询当前订单用户所在部门下所有的司机", response = WsResponse.class)
    @GetMapping("/listByDept/{userId}/{orderUserOrgId}")
	public List<DriverModel> listByDept(
			@ApiParam(value = "用户ID", required = true) @PathVariable("userId")Long userId,
			@ApiParam(value = "订单用户所在组织ID", required = true) @PathVariable("orderUserOrgId")Long orderUserOrgId){
		return driverService.listByDept(getUserById(userId), orderUserOrgId);
	}
	
	/**
     * [企业管理员]添加司机
     * @return
	 * @throws Exception 
     */
	@PostMapping("/create")
	@ApiOperation(value = "添加司机", response = WsResponse.class)
	public User createDriver(@RequestParam(value = "file", required = false)  MultipartFile file,
			@RequestParam(value = "userId", required = true) 
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId,
			@RequestParam(value = "driverUsername", required = true) String driverUsername,
			@RequestParam(value = "driverPassword", required = true) String driverPassword,
			@RequestParam(value = "roleId", required = true) Long roleId,
			@RequestParam(value = "organizationId", required = true) Long organizationId,
			@RequestParam(value = "realname", required = true) String realname,
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "driverEmail", required = false) String driverEmail,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "licenseType", required = true) String licenseType,
			@RequestParam(value = "licenseBegintime", required = true) String licenseBegintime,
			@RequestParam(value = "licenseExpiretime", required = true) String licenseExpiretime,
			@RequestParam(value = "drivingYears", required = false) Integer drivingYears,
			@RequestParam(value = "depId", required = true) Long depId,
			@RequestParam(value = "stationId", required = true) Long stationId
			) throws Exception{
			DriverCreateDto dto = new DriverCreateDto();
			dto.setDriverUsername(driverUsername);
			dto.setDriverPassword(driverPassword);
			dto.setRoleId(roleId);
			dto.setOrganizationId(organizationId);
			dto.setRealname(realname);
			dto.setPhone(phone);
			dto.setDriverEmail(driverEmail);
			dto.setLicenseNumber(licenseNumber);
			dto.setSex(sex);
			dto.setBirthday(birthday);
			dto.setLicenseType(licenseType);
			dto.setLicenseBegintime(licenseBegintime);
			dto.setLicenseExpiretime(licenseExpiretime);
			dto.setDrivingYears(drivingYears);
			dto.setDepId(depId);
			dto.setStationId(stationId);
			return driverService.doCreateOrUpdateDriver(file, super.getUserById(userId), dto, null);
	}
	
	/**
     * [企业管理员]根据id查询司机
     * @return
     */
	@ApiOperation(value = "根据id查询司机", response = WsResponse.class)
    @GetMapping("/show/{id}")
	public DriverModel showUpdateForm(@ApiParam(value = "司机ID", required = true)
		@PathVariable("id")Long id){
		return driverService.showUpdateForm(id);
	}
	
	
    /**
     * [企业管理员]修改司机信息
     * @return     
     * @throws Exception 
     */
	@PutMapping("/update")
	@ApiOperation(value = "修改司机信息", response = WsResponse.class)
	public boolean updateDriver(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "userId", required = true) 
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId,
			@RequestParam(value = "id", required = true) Long id,
			@RequestParam(value = "roleId", required = true) Long roleId,
			@RequestParam(value = "organizationId", required = true) Long organizationId,
			@RequestParam(value = "realname", required = true) String realname,
			@RequestParam(value = "phone", required = true) String phone,
			@RequestParam(value = "driverEmail", required = false) String driverEmail,
			@RequestParam(value = "licenseNumber", required = true) String licenseNumber,
			@RequestParam(value = "sex", required = false) String sex,
			@RequestParam(value = "birthday", required = false) String birthday,
			@RequestParam(value = "licenseType", required = true) String licenseType,
			@RequestParam(value = "licenseBegintime", required = true) String licenseBegintime,
			@RequestParam(value = "licenseExpiretime", required = true) String licenseExpiretime,
			@RequestParam(value = "drivingYears", required = false) Integer drivingYears,
			@RequestParam(value = "depId", required = true) Long depId,
			@RequestParam(value = "stationId", required = true) Long stationId
			) throws Exception{
		
		DriverUpdateDto dto = new DriverUpdateDto();
		dto.setId(id);
		dto.setRoleId(roleId);
		dto.setOrganizationId(organizationId);
		dto.setRealname(realname);
		dto.setPhone(phone);
		dto.setDriverEmail(driverEmail);
		dto.setLicenseNumber(licenseNumber);
		dto.setSex(sex);
		dto.setBirthday(birthday);
		dto.setLicenseType(licenseType);
		dto.setLicenseBegintime(licenseBegintime);
		dto.setLicenseExpiretime(licenseExpiretime);
		dto.setDrivingYears(drivingYears);
		dto.setDepId(depId);
		dto.setStationId(stationId);
		driverService.doCreateOrUpdateDriver(file, super.getUserById(userId), null, dto);
		return true;
	}
	
	/**
     * [超级管理员,租户管理员,管理员]删除用户信息
     * @return
     */
	@ApiOperation(value = "删除用户信息", response = WsResponse.class)
    @DeleteMapping("/delete/{id}")
	public boolean deleteDriver(@ApiParam(value = "司机ID", required = true)
		@PathVariable("id")Long id){
		driverService.delete(id);
		return true;
	}
	
	@ApiOperation(value = "司机导入模板下载", response = WsResponse.class)
    @GetMapping("/loadTemplate")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(HttpServletRequest request) throws UnsupportedEncodingException, FileNotFoundException {
		String relativePath = fileService.getTemplateFilePath("driver");
		Resource file = storageService.loadAsResource(request.getSession().getServletContext().getRealPath(relativePath));
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
				.body(file);
	}
	
	
	/**
     *  [租户管理员,企业管理员]excel导入司机信息
     * @param 
     * @return map
	 * @throws Exception 
     */
	@PostMapping("/import")
	@ApiOperation(value = "excel导入司机信息", response = WsResponse.class)
	public String importDriver(@RequestParam("file") MultipartFile file,
			@RequestParam(value = "userId", required = true)
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId) throws Exception{
		return driverService.importData(super.getUserById(userId), file);
	}
	
	@ApiOperation(value = "获取图片URL", response = WsResponse.class)
    @GetMapping("/getImageUrl")
	public String getImageUrl() {
		return imgUrl;
    }
	
	/**
     * [企业管理员]查看指定部门下的司机列表。
     * [部门管理员]查看指定部门下的司机列表。
     * @return
	 * @throws Exception 
     */
	@ApiOperation(value = "查看指定部门下的司机列表", response = WsResponse.class)
    @PostMapping("/listByDeptId")
	public PagModel listByDeptId(@RequestBody @Valid @NotNull DriverListDto driver) throws Exception{
		return driverService.listByDeptId(getUserById(driver.getUserId()), driver);
	}
	
	  
    /**
     * 查看所有当前企业未分配部门的司机。
     * @return
     * @throws Exception 
     */
	@ApiOperation(value = "查看所有当前企业未分配部门的司机", response = WsResponse.class)
    @PostMapping("/listUnallocatedDepDriver")
	public PagModel listUnallocatedDepDriver(@RequestBody @Valid @NotNull DriverListByUnallocatedDep driver) throws Exception{
		return driverService.listUnallocatedDepDriver(getUserById(driver.getUserId()), driver);
	}
	
	/**
     * 添加司机到本部门。
     * @return
	 * @throws Exception 
     */
	@ApiOperation(value = "添加司机到本部门", response = WsResponse.class)
    @PutMapping("/setDriverToDep")
	public boolean setDriverToDep(@RequestBody @Valid @NotNull DriverSetToDep setDriverDto) throws Exception{
		driverService.setDriverToDep(setDriverDto);
		return true;
	}
	
	/**
     * 将司机从部门中移除。
     * @return
	 * @throws Exception 
     */
	@ApiOperation(value = "将司机从部门中移除", response = WsResponse.class)
    @DeleteMapping("/deleteDriverToDep")
	public List<VehicleAndOrderModel> deleteDriverToDep(@RequestBody @Valid @NotNull DriverDelToDep driverDelToDep) throws Exception{
		return driverService.deleteDriverToDep(driverDelToDep);
	}
	
}
