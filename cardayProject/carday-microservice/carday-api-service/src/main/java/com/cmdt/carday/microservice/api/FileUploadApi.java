package com.cmdt.carday.microservice.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cmdt.carday.microservice.dto.RespDto;
import com.cmdt.carday.microservice.storage.StorageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;


/**
 * File API Service
 * @author senli
 *
 */
@Api("/file")
@SwaggerDefinition(info = @Info(contact = @Contact(name = "Sen Li", email = "sen.li@cm-dt.com"), title = "File Upload API", version = "1.0.0"))
@Controller
public class FileUploadApi {
	
	private static final Logger LOG = LogManager.getLogger(HelloApi.class); 
	
    private final StorageService storageService;

    @Autowired
    public FileUploadApi(StorageService storageService) {
        this.storageService = storageService;
    }
    
    
    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    
    @PostMapping("/file")
    @ApiResponses(value = { 
    		@ApiResponse(code = 200, message = "Realtime data has been successfully processed"),
			@ApiResponse(code = 400, message = "Bad request"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
    public ResponseEntity<RespDto> handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
    	LOG.traceEntry("In FileUploadApi.handleFileUpload, request params: {}", file.getName());
    	
    	//==========业务逻辑调用==========
        storageService.store(file);
        
        return LOG.traceExit("In FileUploadApi.handleFileUpload, response: {}",  ResponseEntity.ok().body(new RespDto("You successfully uploaded " + file.getOriginalFilename() + "!")));
    }

}
