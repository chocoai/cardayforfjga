package com.cmdt.carrental.platform.service.api.portal;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.model.DriverModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformDriverService;
import com.cmdt.carrental.platform.service.biz.service.PlatformFileService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.driver.DriverCreateDto;
import com.cmdt.carrental.platform.service.model.request.driver.DriverDelToDep;
import com.cmdt.carrental.platform.service.model.request.driver.DriverListByUnallocatedDep;
import com.cmdt.carrental.platform.service.model.request.driver.DriverListDto;
import com.cmdt.carrental.platform.service.model.request.driver.DriverSetToDep;
import com.cmdt.carrental.platform.service.model.request.driver.DriverUpdateDto;

@Produces(MediaType.APPLICATION_JSON)
public class DriverApi extends BaseApi{
	private static final Logger LOG = LoggerFactory.getLogger(DriverApi.class);
	
	@Value("${service.imgeUrl}")
    private String imgUrl;
	
	@Autowired
    private PlatformDriverService driverService;
	
	@Autowired
	private PlatformFileService fileService;
	
	 /**
     * [超级管理员]查看所有司机
     * [企业管理员]查看企业下面所有司机
     * [部门管理员]查看部门下面所有司机
     * @return
     */
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listDriver(@Valid @NotNull DriverListDto driverDto){
		try{
			return driverService.findDriverPageByUser(getUserById(driverDto.getUserId()), driverDto);
		}catch(Exception e){
			LOG.error("DriverApi listDriver by userId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	 /**
     * [部门管理员]订单补录，查询当前订单用户所在部门下所有的司机
     * @return
     */
	@GET
	@Path("/listByDept/{userId}/{orderUserOrgId}")
	public List<DriverModel> listByDept(@PathParam("userId")Long userId, @PathParam("orderUserOrgId")Long orderUserOrgId){
		try{
			return driverService.listByDept(getUserById(userId), orderUserOrgId);
		}catch(Exception e){
			LOG.error("DriverApi listByDept by userId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * [企业管理员]添加司机
     * @return
     */
	@POST
	@Path("/create")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String,Object> createDriver(List<Attachment> attachments,
			@Multipart(value = "userId", type = "text/plain" , required = true) 
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId,
			@Multipart(value = "driverUsername", type = "text/plain" , required = true) String driverUsername,
			@Multipart(value = "driverPassword", type = "text/plain" , required = true) String driverPassword,
			@Multipart(value = "roleId", type = "text/plain" , required = true) Long roleId,
			@Multipart(value = "organizationId", type = "text/plain" , required = true) Long organizationId,
			@Multipart(value = "realname", type = "text/plain" , required = true) String realname,
			@Multipart(value = "phone", type = "text/plain" , required = true) String phone,
			@Multipart(value = "driverEmail", type = "text/plain" , required = false) String driverEmail,
			@Multipart(value = "licenseNumber", type = "text/plain" , required = true) String licenseNumber,
			@Multipart(value = "sex", type = "text/plain" , required = false) String sex,
			@Multipart(value = "birthday", type = "text/plain" , required = false) String birthday,
			@Multipart(value = "licenseType", type = "text/plain" , required = true) String licenseType,
			@Multipart(value = "licenseBegintime", type = "text/plain" , required = true) String licenseBegintime,
			@Multipart(value = "licenseExpiretime", type = "text/plain" , required = true) String licenseExpiretime,
			@Multipart(value = "drivingYears", type = "text/plain" , required = false) Integer drivingYears,
			@Multipart(value = "depId", type = "text/plain" , required = true) Long depId,
			@Multipart(value = "stationId", type = "text/plain" , required = true) Long stationId
			){
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
			Map<String,Object> result = null;
		try{
			result = driverService.doCreateOrUpdateDriver(attachments, super.getUserById(userId), dto, null);
		}catch(ServerException e){
			throw e;
		}catch(Exception e){
			LOG.error("DriverApi createDriver error, cause by: ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
		return result;
	}
	
	/**
     * [企业管理员]根据id查询司机
     * @return
     */
	@GET
	@Path("/show/{id}")
	public DriverModel showUpdateForm(@PathParam("id")Long id){
		try{
			return driverService.showUpdateForm(id);
		}catch(ServerException e){
			throw e;
		}catch(Exception e){
			LOG.error("DriverApi showUpdateForm by id error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	
    /**
     * [企业管理员]修改司机信息
     * @return     
     */
	@POST
	@Path("/update")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public Map<String,Object> updateDriver(List<Attachment> attachments,
			@Multipart(value = "userId", type = "text/plain" , required = true) 
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId,
			@Multipart(value = "id", type = "text/plain" , required = true) Long id,
			@Multipart(value = "driverUsername", type = "text/plain" , required = true) String driverUsername,
			@Multipart(value = "driverPassword", type = "text/plain" , required = true) String driverPassword,
			@Multipart(value = "roleId", type = "text/plain" , required = true) Long roleId,
			@Multipart(value = "organizationId", type = "text/plain" , required = true) Long organizationId,
			@Multipart(value = "realname", type = "text/plain" , required = true) String realname,
			@Multipart(value = "phone", type = "text/plain" , required = true) String phone,
			@Multipart(value = "driverEmail", type = "text/plain" , required = false) String driverEmail,
			@Multipart(value = "licenseNumber", type = "text/plain" , required = true) String licenseNumber,
			@Multipart(value = "sex", type = "text/plain" , required = false) String sex,
			@Multipart(value = "birthday", type = "text/plain" , required = false) String birthday,
			@Multipart(value = "licenseType", type = "text/plain" , required = true) String licenseType,
			@Multipart(value = "licenseBegintime", type = "text/plain" , required = true) String licenseBegintime,
			@Multipart(value = "licenseExpiretime", type = "text/plain" , required = true) String licenseExpiretime,
			@Multipart(value = "drivingYears", type = "text/plain" , required = false) Integer drivingYears,
			@Multipart(value = "depId", type = "text/plain" , required = true) Long depId,
			@Multipart(value = "stationId", type = "text/plain" , required = true) Long stationId
			){
		
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
		Map<String,Object> result = null;
		try{
			result = driverService.doCreateOrUpdateDriver(attachments, super.getUserById(userId), null, dto);
		}catch(ServerException e){
			throw e;
		}catch(Exception e){
			LOG.error("DriverApi updateDriver error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
		return result;
	}
	
	/**
     * [超级管理员,租户管理员,管理员]删除用户信息
     * @return
     */
	@POST
	@Path("/delete/{id}")
	@Consumes(MediaType.APPLICATION_JSON)  
	public String deleteDriver(@PathParam("id")Long id){
		try{
			driverService.delete(id);
			return Constants.API_MESSAGE_SUCCESS;
		}catch(Exception e){
			LOG.error("DriverApi deleteDriver error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * [超级管理员,租户管理员,管理员]模板下载
     * @return
     */
	@GET
	@Path("/loadTemplate")
	@Produces("application/vnd.ms-excel")
	public Response downloadFile() {
		String relativePath = File.separator+"template"+File.separator+"driver"+File.separator+"template.xls";
		try{
			String newFileName = URLEncoder.encode("template.xls","UTF-8");
			File file = fileService.downloadTempalte(relativePath);
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename="+newFileName);
			return response.build();
		}catch(Exception e){
			LOG.error("DriverApi downloadFile error, cause by: ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     *  [租户管理员,企业管理员]excel导入司机信息
     * @param 
     * @return map
     */
	@POST
	@Path("/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public String importDriver(List<Attachment> attachments,
			@Multipart(value = "userId", type = "text/plain" , required = true) 
			@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId){
		try{
			if(attachments.size()==2){
				String result = "";
				for (Attachment attach : attachments) {
					String extension = StringUtils.getFilenameExtension(attach.getDataHandler().getName());
					if(null != extension && ("xls".equalsIgnoreCase(extension) 
							|| "xlsx".equalsIgnoreCase(extension) || "csv".equalsIgnoreCase(extension))){
						DataHandler handler = attach.getDataHandler();
						result = driverService.importData(super.getUserById(userId),handler);
						break;
					} else {
						throw new ServerException("文件格式不正确！");
					}
				}
				return result;
			}else{
				throw new ServerException("必须上传且只能上传一个文件！");
			}
		}catch(ServerException e){
			throw e;
		}catch(Exception e){
			LOG.error("DriverApi importDriver error, cause by: ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
		
	}
	
	@GET
	@Path("/getImageUrl")
	public String getImageUrl() {
		try{
			return imgUrl;
		}catch(Exception e){
			LOG.error("DriverApi getImageUrl error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
    }
	
	/**
     * [企业管理员]查看指定部门下的司机列表。
     * [部门管理员]查看指定部门下的司机列表。
     * @return
     */
	@POST
	@Path("/listByDeptId")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listByDeptId(@Valid @NotNull DriverListDto driver){
		try{
			return driverService.listByDeptId(getUserById(driver.getUserId()), driver);
		}catch(Exception e){
			LOG.error("DriverApi listByDeptId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	  
    /**
     * 查看所有当前企业未分配部门的司机。
     * @return
     */
	@POST
	@Path("/listUnallocatedDepDriver")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel listUnallocatedDepDriver(@Valid @NotNull DriverListByUnallocatedDep driver){
		try{
			return driverService.listUnallocatedDepDriver(getUserById(driver.getUserId()), driver);
		}catch(Exception e){
			LOG.error("DriverApi listUnallocatedDepDriver error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * 添加司机到本部门。
     * @return
     */
	@POST
	@Path("/setDriverToDep")
	@Consumes(MediaType.APPLICATION_JSON)
	public String setDriverToDep(@Valid @NotNull DriverSetToDep setDriverDto){
		try{
			driverService.setDriverToDep(setDriverDto);
			return Constants.API_MESSAGE_SUCCESS;
		}catch(Exception e){
			LOG.error("DriverApi setDriverToDep error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * 将司机从部门中移除。
     * @return
     */
	@POST
	@Path("/deleteDriverToDep")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleAndOrderModel> deleteDriverToDep(@Valid @NotNull DriverDelToDep driverDelToDep){
		try{
			return driverService.deleteDriverToDep(driverDelToDep);
		}catch(Exception e){
			LOG.error("DriverApi deleteDriverToDep error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
}
