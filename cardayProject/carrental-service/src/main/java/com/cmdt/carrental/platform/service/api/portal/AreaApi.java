package com.cmdt.carrental.platform.service.api.portal;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmdt.carrental.common.model.Area;
import com.cmdt.carrental.common.service.AreaService;
import com.cmdt.carrental.platform.service.api.BaseApi;
import com.cmdt.carrental.platform.service.common.Constants;
import com.cmdt.carrental.platform.service.exception.ServerException;

@Produces(MediaType.APPLICATION_JSON)
public class AreaApi extends BaseApi{
	
	private static final Logger LOG = LoggerFactory.getLogger(AreaApi.class);
	
	@Autowired
	private AreaService areaService;
	
	@GET
	@Path("/info/{parentId}")
	public List<Area> queryAreaInfo(@PathParam("parentId")Integer parentId){
		try{
			return areaService.findByParentId(parentId);
		}catch(Exception e){
			LOG.error("AreaApi queryAreaInfo by parentId error, cause by: : ",e);
			throw new ServerException(Constants.API_MESSAGE_FAILURE);
		}
	}

}
