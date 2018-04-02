package com.cmdt.carrental.rt.data.service.api;



import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cmdt.carday.microservice.common.model.response.WsResponse;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carrental.rt.data.database.dto.DtcDataDTO;
import com.cmdt.carrental.rt.data.database.dto.EventDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeDataDTO;
import com.cmdt.carrental.rt.data.database.dto.RealtimeQueryDTO;
import com.cmdt.carrental.rt.data.service.common.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;



@Api("/rtData")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li",
        email = "sen.li@cm-dt.com"),
        title = "The data api for vehicle run-time",
        version = Constants.Project_Version))
@Validated
@RestController
@RequestMapping("/rtData")
public class RtDataApi extends BaseApi{	
	
    
    @ApiOperation(value = "保存车辆实时数据.", response = WsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/saveRealtime", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public void saveRealtime(@RequestBody @Valid @NotNull RealtimeDataDTO dto){
        LOG.info("Enter RtDataApi saveRealtime");
		dataService.persistRealtimeData(dto);
	}
	
	
	@ApiOperation(value = "保存设备事件数据.", response = WsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/saveEvent", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public void saveEvent(@RequestBody @Valid @NotNull EventDataDTO dto){
	    LOG.info("Enter RtDataApi saveEvent");
		dataService.persistEventData(dto);
	}
	
	
	
	@ApiOperation(value = "保存远程诊断数据DTC.", response = WsResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/saveDtc", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public void saveDtc(@RequestBody @Valid @NotNull DtcDataDTO dto){
	    LOG.info("Enter RtDataApi saveDtc");
	    dataService.persistDtcData(dto);	
	}
	
	
	
	@ApiOperation(value = "查询车辆实时数据.", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/list", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public List<RealtimeDataDTO> list(@RequestBody @Valid @NotNull RealtimeQueryDTO dto){
	    LOG.info("Enter RtDataApi list ["+dto+"]");
	    return dataService.queryRealtimeData(dto);
	}
	
	
	@ApiOperation(value = "查询车辆最新数据.", response = RealtimeDataDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/queryLatest", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public RealtimeDataDTO queryLatest(@RequestBody @Valid @NotNull RealtimeQueryDTO dto){
	    LOG.info("Enter RtDataApi queryLatest ["+dto+"]");
	    return dataService.queryLatestRealtime(dto);
	}
	
}
