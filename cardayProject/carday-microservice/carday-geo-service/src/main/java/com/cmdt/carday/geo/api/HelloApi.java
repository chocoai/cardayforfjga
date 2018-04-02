package com.cmdt.carday.geo.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
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
    
	
    
    @GetMapping("/hello/{myName}")
    public String testSpring(@PathVariable String myName){
    	return "Hello Hero ["+ myName +"]";
    }
    
    
}  