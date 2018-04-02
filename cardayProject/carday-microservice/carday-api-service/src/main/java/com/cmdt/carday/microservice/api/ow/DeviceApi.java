package com.cmdt.carday.microservice.api.ow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cmdt.carday.microservice.api.BaseApi;
import com.cmdt.carday.microservice.biz.service.PlatformDeviceService;
import com.cmdt.carday.microservice.model.request.device.DeviceCreateDto;
import com.cmdt.carday.microservice.model.request.device.DeviceListDto;
import com.cmdt.carrental.common.model.PagModel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

@Api("/device")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Zhang Ying", email = "ying.zhang@cm-dt.com"), title = "The Device api for device-management", version = "1.0.0"))
@Validated
@RestController
@RequestMapping("/device")
public class DeviceApi extends BaseApi {
	@Autowired
	PlatformDeviceService platformDeviceService;
    
	@ApiOperation(value = "设备列表", response = PagModel.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PagModel deviceList(@ApiParam(value = "设备列表信息", required = true) @RequestBody DeviceListDto deviceListDto) {
		return platformDeviceService.deviceList(getUserById(deviceListDto.getUserId()), deviceListDto);
	}
	
	@ApiOperation(value = "新增设备",response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean create(@ApiParam(value = "新增设备", required = true) @RequestBody DeviceCreateDto deviceCreateDto) {
		platformDeviceService.create(deviceCreateDto);
		return true;
	}
	
	
}
