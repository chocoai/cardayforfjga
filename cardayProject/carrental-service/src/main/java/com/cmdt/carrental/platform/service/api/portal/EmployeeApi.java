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
import org.springframework.util.StringUtils;

import com.cmdt.carrental.common.model.EmployeeModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.VehicleAndOrderModel;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformEmployeeService;
import com.cmdt.carrental.platform.service.biz.service.PlatformFileService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.common.WsResponse;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeCreateDto;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeDelToDep;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeListUnallocatedDep;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeSearchDto;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeSetToDep;
import com.cmdt.carrental.platform.service.model.request.employee.EmployeeUpdateDto;

@Produces(MediaType.APPLICATION_JSON)
public class EmployeeApi extends BaseApi {
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeApi.class);
	
	@Autowired
	private PlatformEmployeeService employeeService;
	
	@Autowired
	private PlatformFileService fileService;
	
    /**
     * [超级管理员]查看所有员工
     * [企业管理员]查看企业下所有员工
     * [部门管理员]查看部门下所有员工
     * @return
     */
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel listEmployeeByOrgIdByPageInSearch(@Valid @NotNull EmployeeSearchDto dto){
		try{
			return employeeService.listEmployeeByOrgIdByPageInSearch(super.getUserById(dto.getUserId()), dto);
		}catch(Exception e){
			LOG.error("EmployeeApi listEmployeeByOrgIdByPageInSearch by userId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	 /**
     * [部门管理员]查看部门下所有员工,用于部门管理员补录订单使用
     * @return
     */
	@GET
	@Path("/listByDept/{userId}")
	public List<EmployeeModel> listByDept(@PathParam("userId")Long userId){
		try{
			return employeeService.listByDept(super.getUserById(userId));
		}catch(Exception e){
			LOG.error("EmployeeApi listByDept by userId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	 
    /**
     * [企业管理员]添加员工
     * @param username,password,roleId,organizationId,userCategory,realname,phone,email
     * @return
     */
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Map<String,String> createEmployee(@Valid @NotNull EmployeeCreateDto dto){
		try{
			return employeeService.createEmployee(super.getUserById(dto.getUserId()),dto);
		}catch(Exception e){
			LOG.error("EmployeeApi createEmployee error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * [企业管理员]根据id查询用户
	 * @param id
	 * @return
	 */
	@GET
	@Path("/show/{id}")
	public EmployeeModel showUpdateForm(@PathParam("id")Long id){
		try{
			return employeeService.findEmployeeModel(id);
		}catch(Exception e){
			LOG.error("EmployeeApi showUpdateForm error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * [企业管理员]修改用户信息
	 * @param id,username,roleId,userCategory,organizationId,realname,phone,email
	 * @return
	 */
	@POST
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)  
	public Map<String,String> updateEmployee(@Valid @NotNull EmployeeUpdateDto dto){
		try{
			return employeeService.updateEmployee(super.getUserById(dto.getUserId()),dto);
		}catch(Exception e){
			LOG.error("EmployeeApi updateEmployee error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * [超级管理员,租户管理员,管理员]删除用户信息
	 * @param id
	 * @return
	 */
	@GET
	@Path("/delete/{id}")
	public String deleteDepartment(@PathParam("id")Long id){
		try{
			employeeService.deleteEmployee(id);
			return Constants.API_MESSAGE_SUCCESS;
		}catch(Exception e){
			LOG.error("EmployeeApi deleteDepartment error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * excel模板下载
	 * @return
	 */
	@GET
	@Path("/loadTemplate")
	@Produces("application/vnd.ms-excel")
	public Response downloadFile() {
		String relativePath = File.separator+"template"+File.separator+"employee"+File.separator+"template.xls";
		try{
			String newFileName = URLEncoder.encode("template.xls","UTF-8");
			File file = fileService.downloadTempalte(relativePath);
			ResponseBuilder response = Response.ok((Object) file);
			response.header("Content-Disposition", "attachment; filename="+newFileName);
			return response.build();
		}catch(Exception e){
			LOG.error("EmployeeApi downloadFile error, cause by: ",e);
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok( wsResponse ).build();
		}
	}
	
	 
	/**
	 * [企业管理员]excel导入员工信息
	 * @param 
	 * @return map
	 */
	@POST
	@Path("/import")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({MediaType.APPLICATION_JSON})
	public Response importEmplyee(List<Attachment> attachments,
			@Multipart(value = "userId", type = "text/plain" , required = true) 
	@Digits(message="UserId格式错误", fraction = 0, integer = 10) Long userId){
		try{
			if(attachments.size()==2){
				String result = "";
				for (Attachment attach : attachments) {
					if(StringUtils.getFilenameExtension(attach.getDataHandler().getName())!=null
							&&!StringUtils.getFilenameExtension(attach.getDataHandler().getName()).equals("text/plain")){
						DataHandler handler = attach.getDataHandler();
						result = employeeService.importEmployeeFromFile(super.getUserById(userId),handler);
						break;
					}
				}
				WsResponse<String> wsResponse = new WsResponse<>();
				wsResponse.setResult(result);
				wsResponse.getMessages().add(Constants.API_MESSAGE_SUCCESS);
				wsResponse.setStatus(Constants.API_STATUS_SUCCESS);
				return Response.ok( wsResponse ).build();
			}else{
				WsResponse<String> wsResponse = new WsResponse<>();
				wsResponse.getMessages().add("必须上传且只能上传一个文件！");
				wsResponse.setStatus(Constants.API_STATUS_FAILURE);
				wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
				return Response.ok( wsResponse ).build();
			}
		}catch(Exception e){
			LOG.error("EmployeeApi importEmplyee error, cause by: ",e);
			e.printStackTrace();
			WsResponse<String> wsResponse = new WsResponse<>();
			wsResponse.setStatus(Constants.API_STATUS_FAILURE);
			wsResponse.getMessages().add(Constants.API_MESSAGE_FAILURE);
			return Response.ok( wsResponse ).build();
		}
		
	}
	
	/**
	 * [企业管理员]查看指定部门下的员工列表。
	 * [部门管理员]查看指定部门下的员工列表。
	 * @return
	 */
	@POST
	@Path("/listByDeptId")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel listByDeptId(@Valid @NotNull EmployeeSearchDto dto){
		try{
			return employeeService.listByDeptId(super.getUserById(dto.getUserId()), dto);
		}catch(Exception e){
			LOG.error("EmployeeApi listByDeptId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
	 * 查看所有当前企业未分配部门的员工。
	 * @return
	 */
	@POST
	@Path("/listUnallocatedDepEmp")
	@Consumes(MediaType.APPLICATION_JSON)  
	public PagModel listUnallocatedDepEmp(@Valid @NotNull EmployeeListUnallocatedDep dto){
		try{
			return employeeService.listUnallocatedDepEmp(super.getUserById(dto.getUserId()), dto);
		}catch(Exception e){
			LOG.error("EmployeeApi listUnallocatedDepEmp error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * 添加员工到本部门。
     * @return
     */
	@POST
	@Path("/setEmployeeToDep")
	@Consumes(MediaType.APPLICATION_JSON)
	public String setEmployeeToDep(@Valid @NotNull EmployeeSetToDep employeeSetToDep){
		try{
			employeeService.setEmployeeToDep(employeeSetToDep);
			return Constants.API_MESSAGE_SUCCESS;
		}catch(Exception e){
			LOG.error("EmployeeApi setEmployeeToDep error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * 将员工从部门中移除。
     * @return
     */
	@POST
	@Path("/deleteEmployeeToDep")
	@Consumes(MediaType.APPLICATION_JSON)
	public List<VehicleAndOrderModel> deleteEmployeeToDep(@Valid @NotNull EmployeeDelToDep employeeDelToDep){
		try{
			return employeeService.deleteEmployeeToDep(super.getUserById(employeeDelToDep.getUserId()),
					employeeDelToDep);
		}catch(Exception e){
			LOG.error("EmployeeApi deleteEmployeeToDep error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
}
