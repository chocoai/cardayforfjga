package com.cmdt.carday.microservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.cmdt.carday.microservice.storage.StorageProperties;
import com.cmdt.carday.microservice.storage.StorageService;

/**
 * Entrance class
 * @author senli
 *
 */
@SpringBootApplication(scanBasePackages = { "com.cmdt.carday.microservice",
    "com.cmdt.carrental.common"} )
@EnableConfigurationProperties(StorageProperties.class)
public class RestApplication {  
  
    public static void main(String[] args) {  
        SpringApplication.run(RestApplication.class, args);  
    }
    
    @Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
}  
