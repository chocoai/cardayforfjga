package com.cmdt.carday.microservice.api.portal;


import java.util.List;

import javax.validation.constraints.NotNull;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformRentService;
import com.cmdt.carrental.common.entity.Rent;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;


@Api(value="/rent")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li", email = "sen.li@cm-dt.com"), title = "The rent api for rent-management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/rent")
public class RentApi extends BaseApi {
	
	private static final Logger LOG = LoggerFactory.getLogger(RentApi.class);
	
	@Autowired
	private PlatformRentService platformRentService;

	
	@ApiOperation(value="根据id查询租户信息", response=List.class )
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	//@RequestMapping(path="/list", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path="/list/{userId}", produces= MediaType.APPLICATION_JSON_VALUE)
	@ApiImplicitParam
	public List<Rent> showRentList(@ApiParam(value="用户id",required=true)  
			@PathVariable ("userId")  @NotNull Long userId){
		
		return platformRentService.showRentList(super.getUserById(userId));
		
	}
	
	
}
