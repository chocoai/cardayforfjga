package com.cmdt.carrental.platform.service.api.ow;

import java.util.List;

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
import com.cmdt.carrental.common.model.AuditInfoModel;
import com.cmdt.carrental.common.model.PagModel;
import com.cmdt.carrental.common.model.RelatedOrganizationInfo;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.biz.service.PlatformOrganizationService;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;
import com.cmdt.carrental.platform.service.model.request.organization.CheckOrganizationDto;
import com.cmdt.carrental.platform.service.model.request.organization.CreateOrganizationDto;
import com.cmdt.carrental.platform.service.model.request.organization.OrganizationListDto;
import com.cmdt.carrental.platform.service.model.request.organization.UpdateOrganizationDto;
import com.cmdt.carrental.platform.service.model.request.organization.UpdateRelatedRentCompanyDto;

@Produces(MediaType.APPLICATION_JSON)
public class OrganizationApi extends BaseApi {
	
	private static final Logger LOG = LoggerFactory.getLogger(OrganizationApi.class);
	
	@Autowired
    private PlatformOrganizationService organizationService;
	
	 /**
     * 查询用车企业
     * @return
     */
	@POST
	@Path("/list")
	@Consumes(MediaType.APPLICATION_JSON)
	public PagModel showList(@Valid @NotNull OrganizationListDto dto) {
		try{
	    	return organizationService.showList(dto);
		}catch(Exception e){
			LOG.error("OrganizationApi showList error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}
	
	/**
     * 查询所有已经审核过的企业
     * @return
     */
    @GET
	@Path("/audited/list/{userId}")
    public List<Organization> showAuditedList(@PathParam("userId")Long userId) {
    	try{
    		return organizationService.showAuditList(getUserById(userId));
    	}catch(Exception e){
    		LOG.error("OrganizationApi showAuditedList by userId error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
    /**
     * 增加企业信息
     * @return
     */
    @POST
	@Path("/addOrganization")
	@Consumes(MediaType.APPLICATION_JSON)
    public String addOrganization(@Valid @NotNull CreateOrganizationDto dto) {
    	try{
    		organizationService.addOrganization(dto);
    		return Constants.API_MESSAGE_SUCCESS;
    	}catch(ServerException e){
			throw e;
    	}catch(Exception e){
    		LOG.error("OrganizationApi addOrganization error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
    
    /**
     * 修改企业
     * @return
     */
    @POST
	@Path("/updateOrganization")
	@Consumes(MediaType.APPLICATION_JSON)
    public String updateOrganization(@Valid @NotNull UpdateOrganizationDto dto) {
    	try{
    		organizationService.update(dto);
    		return Constants.API_MESSAGE_SUCCESS;
    	}catch(ServerException e){
			throw e;
    	}catch(Exception e){
    		LOG.error("OrganizationApi updateOrganization error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
    /**
     * 查询所有已经审核过的企业
     * @return
     */
    @GET
	@Path("/showAuditInfo/{entId}")
    public List<AuditInfoModel> showAuditInfo(@PathParam("entId")Integer entId) {
    	try{
    	    return organizationService.showAuditInfo(entId);
    	}catch(Exception e){
    		LOG.error("OrganizationApi showAuditInfo error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
    /**
     * [系统运营管理员,业务运营管理员] 
     * @return
     */
    @GET
   	@Path("/findRelatedRentCompany/{entId}")
    public List<RelatedOrganizationInfo> findRelatedRentCompany(@PathParam("entId")Long entId) {
    	try{
    		return organizationService.findRelatedRentCompany(entId);
    	}catch(Exception e){
    		LOG.error("OrganizationApi findRelatedRentCompany error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
    /**
     * [系统运营管理员,业务运营管理员] 
     * @param id
     * @return
     */
    @POST
	@Path("/updateRelatedRentCompany/{entId}")
	@Consumes(MediaType.APPLICATION_JSON)
    public String updateRelatedRentCompany(@Valid @NotNull List<UpdateRelatedRentCompanyDto> relatedRentCompanyList,
    		@PathParam("entId")Long entId) {
    	try{
    		organizationService.updateRelatedRentCompany(relatedRentCompanyList,entId);
    		return Constants.API_MESSAGE_SUCCESS;
    	}catch(Exception e){
    		LOG.error("OrganizationApi updateRelatedRentCompany error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
    /**
     * [租户管理员] 审核企业
     * @param id,status,comments(可选)
     * @return
     */
    @POST
   	@Path("/audit/check")
   	@Consumes(MediaType.APPLICATION_JSON)
    public String checkOrganization(@Valid @NotNull CheckOrganizationDto dto) {
    	try{
    		organizationService.checkOrganization(dto);
    		return Constants.API_MESSAGE_SUCCESS;
    	}catch(Exception e){
    		LOG.error("OrganizationApi checkOrganization error, cause by: : ",e);
    		throw new ServerException(Constants.API_MESSAGE_FAILURE);
    	}
    }
    
}
