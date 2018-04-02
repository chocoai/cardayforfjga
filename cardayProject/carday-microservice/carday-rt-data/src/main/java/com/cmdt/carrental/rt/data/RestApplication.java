package com.cmdt.carrental.rt.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Entrance class
 * @author senli
 *
 */
@SpringBootApplication(scanBasePackages = { "com.cmdt.carrental.rt.data", "com.cmdt.carrental.common"} )
@EnableConfigurationProperties
public class RestApplication {  
  
    public static void main(String[] args) {  
        SpringApplication.run(RestApplication.class, args);  
    }
    
}  
