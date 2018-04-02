package com.cmdt.carrental.rt.data.service.api;



import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carrental.rt.data.database.dto.RealtimeDataDTO;
import com.cmdt.carrental.rt.data.database.dto.TrackDataDTO;
import com.cmdt.carrental.rt.data.database.dto.TrackDataQueryDTO;
import com.cmdt.carrental.rt.data.service.biz.TrackDataService;
import com.cmdt.carrental.rt.data.service.common.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;



@Api("/trackData")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Wei Cheng",
        email = "wei.cheng@cm-dt.com"),
        title = "The data api for vehicle track data",
        version = Constants.Project_Version))
@Validated
@RestController
@RequestMapping("/trackData")
public class TrackDataApi extends BaseApi{	
	
	@Autowired
    protected TrackDataService trackDataService;
	
	@ApiOperation(value = "查询车辆轨迹.", response = TrackDataDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server Error")})
    @PostMapping(value = "/getTrackData", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
	public List<TrackDataDTO> getTrackData(@RequestBody @Valid @NotNull TrackDataQueryDTO dto){
	    LOG.info("Enter TrackDataApi getTrackData ["+dto+"]");
	    return trackDataService.getTrackData(dto);
	}
	
}
