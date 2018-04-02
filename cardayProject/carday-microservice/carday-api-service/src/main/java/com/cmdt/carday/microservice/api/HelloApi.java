package com.cmdt.carday.microservice.api;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmdt.carday.microservice.business.SampleService;
import com.cmdt.carday.microservice.dto.HelloDto;
import com.cmdt.carday.microservice.dto.HelloPutDto;
import com.cmdt.carday.microservice.dto.RespDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;

/**
 * Hello API for demo
 * @author senli
 *
 */
@Api("/hello")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li", email = "sen.li@cm-dt.com"), title = "The demo api for say hello", version = "1.0.0"))
@Validated
@RestController  
public class HelloApi {  
      
	private static final Logger LOG = LogManager.getLogger(HelloApi.class); 
    
	/**
	 * Service Injection
	 */
	@Autowired
	private SampleService samService;
	
	
	
	/**
	 * Get method for say hello to input name
	 * @param myName
	 * @return
	 */
	@ApiOperation(value = "get hello data.", response = RespDto.class)
	@ApiResponses(value = { 
    		@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
    @GetMapping("/hello/{myName}")
    ResponseEntity<RespDto> getHello(@PathVariable String myName) {
		LOG.traceEntry("In HelloApi.queryHello, request params: {}", myName);
		
		//==========Start 业务逻辑调用==========
		RespDto dto = samService.getHelloAsSuccess(myName);
		//============End 业务逻辑调用==========
		
		//check resp and return entity
		if (dto != null && dto.getErrorCode() == null){
            return LOG.traceExit("In HelloApi.getHello, success: {}",  ResponseEntity.ok().body(dto));
        }
		
        return LOG.traceExit("In HelloApi.getHello, failure: {}",  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto));
    }
	
	
	/**
	 * Query hello by pagination
	 * @param myName
	 * @return
	 */
	@ApiOperation(value = "query hello data with pagination", response = HelloPutDto.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(path="/helloWithPage", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RespDto> queryHelloWithPage(
    		@ApiParam(value = "pagenation limit", required = true) @RequestParam @Valid @NotNull  @Min(1) Integer limit, 
    		@ApiParam(value = "pagenation offset", required = true) @RequestParam @Valid @NotNull  @Max(100) Integer offset,
    		@ApiParam(value = "user name for say hello", required = false) @RequestParam(required=false) String name
    		) {
		LOG.traceEntry("In HelloApi.queryHello, request params: {},{},{}", name,limit,offset);    	
		//==========Start 业务逻辑调用==========
		RespDto dto = samService.getHelloListAsSuccess(name,limit,offset);
		//============End 业务逻辑调用==========
		
		//check resp and return entity
		if (dto != null && dto.getErrorCode() == null){
            return LOG.traceExit("In HelloApi.getHello with pagination, success: {}",  ResponseEntity.ok().body(dto));
        }
    	
		return LOG.traceExit("In HelloApi.getHello with pagination, failure: {}",  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto));
    }
	
    
    /**
     * delete hello data
     * @param myName
     * @return
     */
    @ApiOperation(value = "delete hello data.", response = RespDto.class)
    @ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
    @DeleteMapping("/hello/{myName}")
    ResponseEntity<RespDto> deleteHello(@PathVariable String myName) {
    	
    	//==========Start 业务逻辑调用==========
    	RespDto dto = samService.processHelloAsSuccess(myName);
    	//============End 业务逻辑调用==========
    	
    	//check resp and return entity
    	if (dto != null && dto.getErrorCode() == null){
            return LOG.traceExit("In HelloApi.deleteHello, success: {}",  ResponseEntity.ok().body(dto));
        }
    	
    	return LOG.traceExit("In HelloApi.deleteHello, failure: {}",  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto));
    }
    
    
    /**
     * Post method for post data
     * @param dto
     * @return
     */
    @ApiOperation(value = "Process realtime data.", response = RespDto.class)
    @ApiResponses(value = { 
    		@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
//    @RequestMapping(path="/hello", consumes = { "application/json" }, produces = { "application/json" }, method = POST)
    @RequestMapping(value = "hello", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RespDto> addHello(@ApiParam(value = "Hello data parameters.", required = true) @Valid @RequestBody HelloDto dto, BindingResult result) {  
    	
    	LOG.traceEntry("In HelloApi.postHello, request params: {}", dto);
    	
    	//==========Start 业务逻辑调用==========
    	RespDto respdto = samService.processHelloAsSuccess(dto);
    	//============End 业务逻辑调用==========
    	
    	//check resp and return entity
    	if (respdto != null && respdto.getErrorCode() == null){
            return LOG.traceExit("In HelloApi.postHello, success: {}",  ResponseEntity.ok().body(respdto));
        }
    	
    	return LOG.traceExit("In HelloApi.postHello, failure: {}",  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respdto));
    }
    
    
    /**
     * batch process hello data
     * @param dto
     * @param result
     * @return
     */
    @ApiOperation(value = "Batch process realtime data.", response = RespDto.class)
    @ApiResponses(value = { 
    		@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
    @RequestMapping(path="/hello", consumes = { "application/json" }, produces = { "application/json" }, method = PUT)
    ResponseEntity<RespDto> batchHello(@ApiParam(value = "Hello data parameters.", required = true) @Valid @RequestBody HelloPutDto dto, BindingResult result) {  
    	
    	LOG.traceEntry("In HelloApi.putHello, request params: {}", dto);
    	
    	//==========Start 业务逻辑调用==========
    	RespDto respdto = samService.processHelloAsSuccess(dto);
    	//============End 业务逻辑调用==========
    	
    	//check resp and return entity
    	if (respdto != null && respdto.getErrorCode() == null){
            return LOG.traceExit("In HelloApi.putHello, success: {}",  ResponseEntity.ok().body(respdto));
        }
    	
    	return LOG.traceExit("In HelloApi.putHello, failure: {}",  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respdto));
    }
    
    @GetMapping("/helloString/{myName}")
    public HelloDto testSpring(@PathVariable String myName){
    	HelloDto dto =new HelloDto();
    	dto.setDate("2017-07-11");
    	dto.setId(123L);
    	dto.setName("1234567");
    	return dto;
    }
    
    
}  